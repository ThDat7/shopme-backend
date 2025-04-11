package com.shopme.client.controller;

import com.shopme.client.dto.response.BrandResponse;
import com.shopme.client.dto.response.ListResponse;
import com.shopme.client.service.BrandService;
import com.shopme.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @GetMapping
    public ApiResponse<ListResponse<BrandResponse>> getBrands(@RequestParam Map<String, String> params) {
        return ApiResponse.ok(brandService.getBrands(params));
    }
}
