package com.shopme.client.service;

import com.shopme.client.dto.response.CategoryBreadcrumbResponse;
import com.shopme.client.dto.response.CategoryResponse;
import com.shopme.client.dto.response.ListCategoryResponse;

import java.util.List;
import java.util.Set;

public interface CategoryService {
    List<CategoryResponse> listLeafCategories();

    ListCategoryResponse getChildCategories(Integer id);
    Set<CategoryBreadcrumbResponse> findParentCategories(Integer id);
}
