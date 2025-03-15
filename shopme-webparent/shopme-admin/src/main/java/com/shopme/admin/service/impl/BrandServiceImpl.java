package com.shopme.admin.service.impl;

import com.shopme.admin.dto.request.BrandCreateRequest;
import com.shopme.admin.dto.request.BrandUpdateRequest;
import com.shopme.admin.dto.response.*;
import com.shopme.admin.mapper.BrandMapper;
import com.shopme.admin.mapper.UtilMapper;
import com.shopme.admin.repository.BrandRepository;
import com.shopme.admin.repository.CategoryRepository;
import com.shopme.admin.service.BrandService;
import com.shopme.admin.service.FileUploadService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private static final String DEFAULT_SORT_FIELD = "name";
    private static final String DEFAULT_SORT_DIRECTION = "asc";
    private static final int DEFAULT_BRANDS_PER_PAGE = 4;

    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final BrandMapper brandMapper;
    private final UtilMapper utilMapper;
    private final FileUploadService fileUploadService;

    private String getBrandLogoURL(Brand brand) {
        return fileUploadService.getBrandLogoUrl(brand.getId(), brand.getLogo());
    }

    private Pageable getPageableFromParams(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "0"));
        int size = Integer.parseInt(params.getOrDefault("size", String.valueOf(DEFAULT_BRANDS_PER_PAGE)));
        String sortField = params.getOrDefault("sortField", DEFAULT_SORT_FIELD);
        String sortDirection = params.getOrDefault("sortDirection", DEFAULT_SORT_DIRECTION);

        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        return PageRequest.of(page, size, sort);
    }

    @Override
    public ListResponse<BrandListResponse> listByPage(Map<String, String> params) {
        Pageable pageable = getPageableFromParams(params);
        String keyword = params.getOrDefault("keyword", "");

        Page<Brand> brandPage = brandRepository.findAll(keyword, pageable);
        List<BrandListResponse> brandListResponses = brandPage.getContent().stream()
                .map(brand -> {
                    var brandDto = brandMapper.toBrandListResponse(brand);
                    brandDto.setLogo(getBrandLogoURL(brand));
                    return brandDto;
                })
                .collect(Collectors.toList());

        return ListResponse.<BrandListResponse>builder()
                .content(brandListResponses)
                .totalPages(brandPage.getTotalPages())
                .build();
    }

    @Override
    @Transactional
    public BrandDetailResponse createBrand(BrandCreateRequest request) {
        if (request.getImage() == null || request.getImage().isEmpty())
            throw new RuntimeException("Brand logo is required");

        if (brandRepository.existsByName(request.getName()))
            throw new RuntimeException("Brand already exists");

        Brand brand = brandMapper.toEntity(request);

        // Set categories
        Set<Category> categories = request.getCategoryIds().stream()
                .map(categoryRepository::findById)
//                exception category not found
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        brand.setCategories(categories);


        String fileName = StringUtils.cleanPath(Objects.requireNonNull(request.getImage().getOriginalFilename()));
        brand.setLogo(fileName);
        brandRepository.save(brand);
        fileUploadService.brandLogoUpload(request.getImage(), brand.getId());

        return brandMapper.toBrandDetailResponse(brand);
    }


    @Override
    @Transactional
    public BrandDetailResponse updateBrand(Integer id, BrandUpdateRequest request) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        boolean isNameUnique = !brandRepository.existsByNameAndIdNot(request.getName(), id);
        if (!isNameUnique)
            throw new RuntimeException("Brand already exists");

        brand.setName(request.getName());

        // Set categories
        Set<Category> categories = request.getCategoryIds().stream()
                .map(categoryRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        brand.setCategories(categories);

        // Handle image upload if present
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(request.getImage().getOriginalFilename()));
            fileUploadService.brandLogoUpload(request.getImage(), brand.getId());
            brand.setLogo(fileName);
        }

        brandRepository.save(brand);
        return brandMapper.toBrandDetailResponse(brand);
    }

    @Override
    public void deleteBrand(Integer id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new RuntimeException("Brand not found"));
        brandRepository.delete(brand);
    }

    @Override
    public BrandDetailResponse getBrandById(Integer id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new RuntimeException("Brand not found"));
        var brandDto = brandMapper.toBrandDetailResponse(brand);
        brandDto.setLogo(getBrandLogoURL(brand));
        return brandDto;
    }

    @Override
    public List<BrandExportResponse> listAllForExport() {
        List<Brand> brands = brandRepository.findAll();
        return brands.stream()
                .map(brandMapper::toBrandExportResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<FormSelectResponse> listCategoriesFormSelectByBrand(Integer id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found"));
        return brand.getCategories().stream()
                .map(i -> utilMapper.toFormSelectResponse(i.getId().toString(), i.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<FormSelectResponse> listAllForFormSelection() {
        List<Brand> brands = brandRepository.findAll();
        return brands.stream()
                .map(i -> utilMapper.toFormSelectResponse(i.getId().toString(), i.getName()))
                .collect(Collectors.toList());
    }
}
