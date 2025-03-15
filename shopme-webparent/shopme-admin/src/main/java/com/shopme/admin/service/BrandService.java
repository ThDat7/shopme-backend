package com.shopme.admin.service;

import com.shopme.admin.dto.request.BrandCreateRequest;
import com.shopme.admin.dto.request.BrandUpdateRequest;
import com.shopme.admin.dto.response.*;

import java.util.List;
import java.util.Map;

public interface BrandService {
    ListResponse<BrandListResponse> listByPage(Map<String, String> params);

    BrandDetailResponse createBrand(BrandCreateRequest request);

    void deleteBrand(Integer id);

    BrandDetailResponse getBrandById(Integer id);

    BrandDetailResponse updateBrand(Integer id, BrandUpdateRequest request);

    List<BrandExportResponse> listAllForExport();

    List<FormSelectResponse> listCategoriesFormSelectByBrand(Integer id);

    List<FormSelectResponse> listAllForFormSelection();
}
