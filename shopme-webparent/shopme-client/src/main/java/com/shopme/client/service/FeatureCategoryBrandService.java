package com.shopme.client.service;

import com.shopme.client.dto.response.FeatureCategoryBrandResponse;

import java.util.List;

public interface FeatureCategoryBrandService {
    List<FeatureCategoryBrandResponse> getAll();
}
