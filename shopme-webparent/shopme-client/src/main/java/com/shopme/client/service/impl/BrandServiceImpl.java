package com.shopme.client.service.impl;

import com.shopme.client.dto.response.BrandResponse;
import com.shopme.client.dto.response.ListResponse;
import com.shopme.client.mapper.BrandMapper;
import com.shopme.client.repository.BrandRepository;
import com.shopme.client.service.BrandService;
import com.shopme.client.service.FileUploadService;
import com.shopme.common.entity.Brand_;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;
    private final FileUploadService fileUploadService;

    private String getBrandLogoUrl(Integer brandId, String logo) {
        return fileUploadService.getBrandLogoUrl(brandId, logo);
    }

    @Override
    public ListResponse<BrandResponse> getBrands(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "0"));
        int size = Integer.parseInt(params.getOrDefault("size", String.valueOf(10)));
        String sortField = params.getOrDefault("sortField", Brand_.NAME);
        String sortDirection = params.getOrDefault("sortDirection", "asc");
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Object[]> brandPage = brandRepository.findAllWithProductCount(pageable);
        List<BrandResponse> brandResponses = brandPage.getContent().stream()
                .map(brand -> brandMapper.toBrandResponse(
                        (Integer) brand[0],
                        (String) brand[1],
                        getBrandLogoUrl((Integer) brand[0], (String) brand[2]),
                        (Long) brand[3])).toList();

        return ListResponse.<BrandResponse>builder()
                .content(brandResponses)
                .totalPages(brandPage.getTotalPages())
                .build();
    }
}
