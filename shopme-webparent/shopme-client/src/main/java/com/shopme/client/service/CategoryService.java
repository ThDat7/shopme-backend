package com.shopme.client.service;

import com.shopme.client.dto.response.CategoryBreadcrumbResponse;

import java.util.Set;

public interface CategoryService {
    Set<CategoryBreadcrumbResponse> findParentCategories(Integer id);
}
