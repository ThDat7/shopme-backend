package com.shopme.admin.service;

import com.shopme.admin.dto.request.StateCreateRequest;
import com.shopme.admin.dto.request.StateUpdateRequest;
import com.shopme.admin.dto.response.ListResponse;
import com.shopme.admin.dto.response.StateDetailResponse;
import com.shopme.admin.dto.response.StateListResponse;

import java.util.Map;

public interface StateService {
    StateDetailResponse updateState(Integer id, StateUpdateRequest request);

    StateDetailResponse getStateById(Integer id);

    void deleteState(Integer id);

    StateDetailResponse createState(StateCreateRequest request);

    ListResponse<StateListResponse> listByPage(Map<String, String> params);
}
