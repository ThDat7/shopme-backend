package com.shopme.admin.controller;


import com.shopme.admin.dto.request.ShippingRateCreateRequest;
import com.shopme.admin.dto.request.ShippingRateUpdateRequest;
import com.shopme.admin.dto.response.*;
import com.shopme.admin.service.ShippingRateService;
import com.shopme.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/shipping-rates")
@RequiredArgsConstructor
public class ShippingRateController {
    private final ShippingRateService shippingRateService;

    @GetMapping
    public ApiResponse<ListResponse<ShippingRateListResponse>> listByPage(@RequestParam Map<String, String> params) {
        ListResponse<ShippingRateListResponse> listResponse = shippingRateService.listByPage(params);
        return ApiResponse.ok(listResponse);
    }

    @PostMapping
    public ApiResponse<ShippingRateDetailResponse> createShippingRate(@RequestBody ShippingRateCreateRequest request) {
        return ApiResponse.ok(shippingRateService.createShippingRate(request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteShippingRate(@PathVariable Integer id) {
        shippingRateService.deleteShippingRate(id);
        return ApiResponse.ok();
    }

    @GetMapping("/{id}")
    public ApiResponse<ShippingRateDetailResponse> getShippingRate(@PathVariable Integer id) {
        ShippingRateDetailResponse shippingRate = shippingRateService.getShippingRateById(id);
        return ApiResponse.ok(shippingRate);
    }

    @PutMapping(value = "/{id}")
    public ApiResponse<ShippingRateDetailResponse> updateShippingRate(@PathVariable Integer id,
                                                        @RequestBody ShippingRateUpdateRequest request) {
        return ApiResponse.ok(shippingRateService.updateShippingRate(id, request));
    }
}
