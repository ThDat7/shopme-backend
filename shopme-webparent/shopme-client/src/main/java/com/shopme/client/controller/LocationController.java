package com.shopme.client.controller;

import com.shopme.client.dto.response.FormSelectResponse;
import com.shopme.client.service.LocationService;
import com.shopme.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @GetMapping("/provinces/form-select")
    public ApiResponse<List<FormSelectResponse>> listAllForFormSelection() {
        List<FormSelectResponse> provinces = locationService.listProvinces();
        return ApiResponse.ok(provinces);
    }

    @GetMapping("/provinces/{provinceId}/districts/form-select")
    public ApiResponse<List<FormSelectResponse>> listDistrictsByProvince(@PathVariable Integer provinceId) {
        List<FormSelectResponse> districts = locationService.listDistrictsByProvinceId(provinceId);
        return ApiResponse.ok(districts);
    }

    @GetMapping("/districts/{districtId}/wards/form-select")
    public ApiResponse<List<FormSelectResponse>> listWardsByDistrict(@PathVariable Integer districtId) {
        List<FormSelectResponse> wards = locationService.listWardsByDistrictId(districtId);
        return ApiResponse.ok(wards);
    }

}
