package com.shopme.admin.service.impl;

import com.shopme.admin.dto.response.CategoryListResponse;
import com.shopme.admin.dto.response.CategorySearchResponse;
import com.shopme.admin.dto.response.ListResponse;
import com.shopme.admin.mapper.CategoryMapper;
import com.shopme.admin.repository.CategoryRepository;
import com.shopme.admin.service.CategoryService;
import com.shopme.admin.service.FileUploadService;
import com.shopme.common.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private static final String DEFAULT_SORT_FIELD = "name";
    private static final String DEFAULT_SORT_DIRECTION = "asc";
    private static final int DEFAULT_USERS_PER_PAGE = 4;

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final FileUploadService fileUploadService;

    private String getCategoryImageURL(Category category) {
        return fileUploadService.getCategoryImageUrl(category.getId(), category.getImage());
    }

    private Pageable getPageableFromParams(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "0"));
        int size = Integer.parseInt(params.getOrDefault("size", String.valueOf(DEFAULT_USERS_PER_PAGE)));
        String sortField = params.getOrDefault("sortField", DEFAULT_SORT_FIELD);
        String sortDirection = params.getOrDefault("sortDirection", DEFAULT_SORT_DIRECTION);

        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        return PageRequest.of(page, size, sort);
    }

    @Override
    public ListResponse<CategoryListResponse> listByPage(Map<String, String> params) {
        Pageable pageable = getPageableFromParams(params);

        Page<Category> categoryPage = categoryRepository.findRootCategories(pageable);
        List<CategoryListResponse> categoryListResponses = categoryPage.getContent().stream()
                .map(category -> {
                    boolean hasChildren = categoryRepository.hasChildren(category.getId());
                    var categoryDto = categoryMapper.toCategoryListResponse(category, hasChildren);
                    categoryDto.setImage(getCategoryImageURL(category));
                    return categoryDto;
                })
                .collect(Collectors.toList());

        return ListResponse.<CategoryListResponse>builder()
                .content(categoryListResponses)
                .totalPages(categoryPage.getTotalPages())
                .build();
    }

    @Override
    public ListResponse<CategorySearchResponse> search(Map<String, String> params) {
        String keyword = params.get("keyword");
        if (keyword == null)
            throw new IllegalArgumentException("Keyword cannot be null");

        Pageable pageable = getPageableFromParams(params);

        Page<Category> categoryPage = categoryRepository.findAll(keyword, pageable);
        List<CategorySearchResponse> categorySearchResponses = categoryPage.getContent().stream()
                .map(category -> {
                    String breadcrumb = categoryRepository.findBreadcrumbById(category.getId());
                    var categoryDto = categoryMapper.toCategorySearchResponse(category, breadcrumb);
                    categoryDto.setImage(getCategoryImageURL(category));
                    return categoryDto;
                })
                .collect(Collectors.toList());

        return ListResponse.<CategorySearchResponse>builder()
                .content(categorySearchResponses)
                .totalPages(categoryPage.getTotalPages())
                .build();
    }

    @Override
    public List<CategoryListResponse> listChildren(Integer id) {
        List<Category> children = categoryRepository.findChildren(id);
        return children.stream()
                .map(category -> {
                    boolean hasChildren = categoryRepository.hasChildren(category.getId());
                    var categoryDto = categoryMapper.toCategoryListResponse(category, hasChildren);
                    categoryDto.setImage(getCategoryImageURL(category));
                    return categoryDto;
                })
                .collect(Collectors.toList());
    }
}
