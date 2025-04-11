package com.shopme.client.controller;


import com.shopme.client.dto.response.FeatureCategoryBrandResponse;
import com.shopme.client.service.FeatureCategoryBrandService;
import com.shopme.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feature-category-brand")
@RequiredArgsConstructor
public class FeatureCategoryBrandController {
    private final FeatureCategoryBrandService featureCategoryBrandService;

    @GetMapping
    public ApiResponse<List<FeatureCategoryBrandResponse>> getAll() {
        return ApiResponse.ok(featureCategoryBrandService.getAll());
    }
}
