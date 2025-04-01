package com.shopme.admin.service;

import com.shopme.admin.dto.request.ProvinceCreateRequest;
import com.shopme.admin.dto.request.ProvinceUpdateRequest;
import com.shopme.admin.dto.response.ProvinceDetailResponse;
import com.shopme.admin.dto.response.ProvinceListResponse;
import com.shopme.admin.dto.response.FormSelectResponse;
import com.shopme.admin.dto.response.ListResponse;

import java.util.List;
import java.util.Map;

public interface ProvinceService {
    ListResponse<ProvinceListResponse> listByPage(Map<String, String> params);

    ProvinceDetailResponse createProvince(ProvinceCreateRequest request);

    void deleteProvince(Integer id);

    ProvinceDetailResponse getProvinceById(Integer id);

    ProvinceDetailResponse updateProvince(Integer id, ProvinceUpdateRequest request);

    List<FormSelectResponse> listAllForFormSelection();
}
