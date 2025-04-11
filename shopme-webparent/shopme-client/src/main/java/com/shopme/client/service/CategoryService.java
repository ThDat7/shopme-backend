package com.shopme.client.service;

import com.shopme.client.dto.response.CategoryBreadcrumbResponse;
import com.shopme.client.dto.response.CategoryResponse;
import com.shopme.client.dto.response.ListCategoryResponse;
import com.shopme.client.dto.response.ListResponse;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CategoryService {
    List<CategoryResponse> getChildCategories(Integer id);
    Set<CategoryBreadcrumbResponse> findParentCategories(Integer id);

    ListResponse<CategoryResponse> listRootCategories(Map<String, String> params);

    CategoryResponse getCategory(Integer id);
}
