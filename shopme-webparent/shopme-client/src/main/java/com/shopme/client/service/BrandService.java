package com.shopme.client.service;

import com.shopme.client.dto.response.BrandResponse;
import com.shopme.client.dto.response.ListResponse;

import java.util.Map;

public interface BrandService {
    ListResponse<BrandResponse> getBrands(Map<String, String> params);
}
