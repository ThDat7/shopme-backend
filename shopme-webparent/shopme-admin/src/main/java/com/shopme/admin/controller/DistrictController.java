package com.shopme.admin.controller;

import com.shopme.admin.dto.request.DistrictCreateRequest;
import com.shopme.admin.dto.request.DistrictUpdateRequest;
import com.shopme.admin.dto.response.DistrictDetailResponse;
import com.shopme.admin.dto.response.DistrictListResponse;
import com.shopme.admin.dto.response.FormSelectResponse;
import com.shopme.admin.dto.response.ListResponse;
import com.shopme.admin.service.DistrictService;
import com.shopme.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/districts")
@RequiredArgsConstructor
public class DistrictController {

    private final DistrictService districtService;

    @GetMapping
    public ApiResponse<ListResponse<DistrictListResponse>> listByPage(@RequestParam Map<String, String> params) {
        ListResponse<DistrictListResponse> listResponse = districtService.listByPage(params);
        return ApiResponse.ok(listResponse);
    }

    @PostMapping
    public ApiResponse<DistrictDetailResponse> createDistrict(@RequestBody DistrictCreateRequest request) {
        return ApiResponse.ok(districtService.createDistrict(request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteDistrict(@PathVariable Integer id) {
        districtService.deleteDistrict(id);
        return ApiResponse.ok();
    }

    @GetMapping("/{id}")
    public ApiResponse<DistrictDetailResponse> getDistrict(@PathVariable Integer id) {
        DistrictDetailResponse district = districtService.getDistrictById(id);
        return ApiResponse.ok(district);
    }

    @PutMapping(value = "/{id}")
    public ApiResponse<DistrictDetailResponse> updateDistrict(@PathVariable Integer id,
                                                        @RequestBody DistrictUpdateRequest request) {
        return ApiResponse.ok(districtService.updateDistrict(id, request));
    }

    @GetMapping("/province/{provinceId}/form-select")
    public ApiResponse<List<FormSelectResponse>> listForFormSelectionByProvinceId(@PathVariable Integer provinceId) {
        List<FormSelectResponse> districts = districtService.listForFormSelectionByProvinceId(provinceId);
        return ApiResponse.ok(districts);
    }
}
