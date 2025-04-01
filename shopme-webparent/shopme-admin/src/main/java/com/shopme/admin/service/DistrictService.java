package com.shopme.admin.service;

import com.shopme.admin.dto.request.DistrictCreateRequest;
import com.shopme.admin.dto.request.DistrictUpdateRequest;
import com.shopme.admin.dto.response.FormSelectResponse;
import com.shopme.admin.dto.response.ListResponse;
import com.shopme.admin.dto.response.DistrictDetailResponse;
import com.shopme.admin.dto.response.DistrictListResponse;

import java.util.List;
import java.util.Map;

public interface DistrictService {
    DistrictDetailResponse updateDistrict(Integer id, DistrictUpdateRequest request);

    DistrictDetailResponse getDistrictById(Integer id);

    void deleteDistrict(Integer id);

    DistrictDetailResponse createDistrict(DistrictCreateRequest request);

    ListResponse<DistrictListResponse> listByPage(Map<String, String> params);

    List<FormSelectResponse> listForFormSelectionByProvinceId(Integer provinceId);
}
