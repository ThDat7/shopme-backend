package com.shopme.client.service.impl;

import com.shopme.client.dto.response.CategoryBreadcrumbResponse;
import com.shopme.client.dto.response.CategoryResponse;
import com.shopme.client.dto.response.ListCategoryResponse;
import com.shopme.client.dto.response.ListResponse;
import com.shopme.client.exception.type.CategoryNotFoundException;
import com.shopme.client.mapper.CategoryMapper;
import com.shopme.client.repository.CategoryRepository;
import com.shopme.client.service.CategoryService;
import com.shopme.client.service.FileUploadService;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Category_;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final FileUploadService fileUploadService;

    private String getCategoryImageURL(Integer categoryId,  String image) {
        return fileUploadService.getCategoryImageUrl(categoryId, image);
    }

    private CategoryResponse toCategoryResponse(Object[] categoryData) {
        Integer parentId = null;
        if (categoryData[3] instanceof Integer) {
            parentId = (Integer) categoryData[3];
        }

        Boolean hasChildren = false;
        if (categoryData[5] instanceof Boolean) {
            hasChildren = (Boolean) categoryData[5];
        } else if (categoryData[5] instanceof Number) {
            hasChildren = ((Number) categoryData[5]).intValue() > 0;
        }

        return CategoryResponse.builder()
                .id((Integer) categoryData[0])
                .name((String) categoryData[1])
                .image(getCategoryImageURL((Integer) categoryData[0], (String) categoryData[2]))
                .parentId(parentId)
                .productCount(categoryData[4] instanceof Long ? (Long) categoryData[4] : ((Number) categoryData[4]).longValue())
                .hasChildren(hasChildren)
                .build();
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
    public ListResponse<CategoryResponse> listRootCategories(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "0"));
        int size = Integer.parseInt(params.getOrDefault("size", String.valueOf(10)));
        String sortField = params.getOrDefault("sortField", Category_.NAME);
        String sortDirection = params.getOrDefault("sortDirection", "asc");
        
        // Calculate offset for native query pagination
        int offset = page * size;
        
        // Execute queries
        List<Object[]> categoryData = categoryRepository.findRootCategories(size, offset);
        long totalRootCategories = categoryRepository.countRootCategories();
        
        // Map results to response objects
        List<CategoryResponse> categoryListResponses = categoryData.stream()
                .map(this::toCategoryResponse)
                .toList();
        
        // Calculate total pages
        int totalPages = (int) Math.ceil((double) totalRootCategories / size);

        return ListResponse.<CategoryResponse>builder()
                .content(categoryListResponses)
                .totalPages(totalPages)
                .build();
    }

    @Override
    public CategoryResponse getCategory(Integer id) {
        Object[] category = categoryRepository.findCategoriesWithProductCount(id)
                .orElseThrow(CategoryNotFoundException::new);
        return toCategoryResponse(category);
    }

    @Override
    public List<CategoryResponse> getChildCategories(Integer id) {
        // First find all direct child category IDs of the given category
        List<Integer> childCategoryIds = categoryRepository.findAllChildCategoryIds(id);
        
        // If there are no children, return empty list
        if (childCategoryIds == null || childCategoryIds.isEmpty()) {
            return List.of();
        }
        
        // Find child categories with accurate product counts (including their own children's products)
        List<Object[]> childCategories = categoryRepository.findCategoriesWithProductCount(childCategoryIds);
        
        return childCategories.stream()
                .map(this::toCategoryResponse)
                .toList();
    }
}
