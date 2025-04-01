package com.shopme.client.service.impl;

import com.shopme.client.dto.response.CategoryBreadcrumbResponse;
import com.shopme.client.dto.response.CategoryResponse;
import com.shopme.client.dto.response.ListCategoryResponse;
import com.shopme.client.mapper.CategoryMapper;
import com.shopme.client.repository.CategoryRepository;
import com.shopme.client.service.CategoryService;
import com.shopme.client.service.FileUploadService;
import com.shopme.common.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final FileUploadService fileUploadService;

    private String getCategoryImageURL(Category category) {
        return fileUploadService.getCategoryImageUrl(category.getId(), category.getImage());
    }

    @Override
    public List<CategoryResponse> listLeafCategories() {
        List<Category> leafCategories = categoryRepository.findLeafCategories();
        return leafCategories.stream()
                .map(category -> {
                    CategoryResponse categoryResponse = categoryMapper.toCategoryResponse(category);
                    categoryResponse.setImage(getCategoryImageURL(category));
                    return categoryResponse;
                })
                .toList();
    }

    @Override
    public Set<CategoryBreadcrumbResponse> findParentCategories(Integer id) {
        List<Object[]> categories = categoryRepository.findParentCategories(id);

        return categories.stream()
                .map(category -> categoryMapper
                        .toCategoryBreadcrumbResponse((Integer) category[0], (String) category[1], (String) category[2]))
                .collect(Collectors.toSet());
    }

    @Override
    public ListCategoryResponse getChildCategories(Integer id) {
        List<Category> childCategories = categoryRepository.findByParentId(id);
        Set<CategoryResponse> categoryResponses = childCategories.stream()
                .map(category -> {
                    CategoryResponse categoryResponse = categoryMapper.toCategoryResponse(category);
                    categoryResponse.setImage(getCategoryImageURL(category));
                    return categoryResponse;
                })
                .collect(Collectors.toSet());
        Set<CategoryBreadcrumbResponse> breadcrumbs = findParentCategories(id);
        return categoryMapper.toListCategoryResponse(categoryResponses, breadcrumbs);
    }
}
