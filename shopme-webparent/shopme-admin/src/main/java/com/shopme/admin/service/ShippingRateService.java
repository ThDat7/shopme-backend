package com.shopme.admin.service;

import com.shopme.admin.dto.request.ShippingRateCreateRequest;
import com.shopme.admin.dto.request.ShippingRateUpdateRequest;
import com.shopme.admin.dto.response.*;

import java.util.List;
import java.util.Map;

public interface ShippingRateService {
    ListResponse<ShippingRateListResponse> listByPage(Map<String, String> params);

    ShippingRateDetailResponse createShippingRate(ShippingRateCreateRequest request);

    void deleteShippingRate(Integer id);

    ShippingRateDetailResponse getShippingRateById(Integer id);

    ShippingRateDetailResponse updateShippingRate(Integer id, ShippingRateUpdateRequest request);
}
