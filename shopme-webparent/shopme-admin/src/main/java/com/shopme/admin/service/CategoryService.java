package com.shopme.admin.service;

import com.shopme.admin.dto.request.CategoryCreateRequest;
import com.shopme.admin.dto.request.CategoryUpdateRequest;
import com.shopme.admin.dto.response.*;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    ListResponse<CategoryListResponse> listByPage(Map<String, String> params);

    ListResponse<CategorySearchResponse> search(Map<String, String> params);

    CategoryDetailResponse createCategory(CategoryCreateRequest request);

    void deleteCategory(Integer id);

    CategoryDetailResponse getCategoryById(Integer id);

    CategoryDetailResponse updateCategory(Integer id, CategoryUpdateRequest request);

    void updateCategoryStatus(Integer id, boolean status);

    List<CategoryExportResponse> listAllForExport();

    List<CategoryListResponse> listChildren(Integer id);

    List<CategorySelectResponse> getAllInForm();
}
