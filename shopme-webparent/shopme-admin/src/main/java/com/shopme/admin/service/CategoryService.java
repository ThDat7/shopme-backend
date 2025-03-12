package com.shopme.admin.service;

import com.shopme.admin.dto.response.CategoryListResponse;
import com.shopme.admin.dto.response.CategorySearchResponse;
import com.shopme.admin.dto.response.ListResponse;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    ListResponse<CategoryListResponse> listByPage(Map<String, String> params);

    ListResponse<CategorySearchResponse> search(Map<String, String> params);

    List<CategoryListResponse> listChildren(Integer id);
}
