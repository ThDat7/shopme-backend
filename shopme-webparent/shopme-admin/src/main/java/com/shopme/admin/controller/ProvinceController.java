package com.shopme.admin.controller;


import com.shopme.admin.dto.request.ProvinceCreateRequest;
import com.shopme.admin.dto.request.ProvinceUpdateRequest;
import com.shopme.admin.dto.response.ProvinceDetailResponse;
import com.shopme.admin.dto.response.ProvinceListResponse;
import com.shopme.admin.dto.response.FormSelectResponse;
import com.shopme.admin.dto.response.ListResponse;
import com.shopme.admin.service.ProvinceService;
import com.shopme.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/provinces")
@RequiredArgsConstructor
public class ProvinceController {
    private final ProvinceService provinceService;

    @GetMapping
    public ApiResponse<ListResponse<ProvinceListResponse>> listByPage(@RequestParam Map<String, String> params) {
        ListResponse<ProvinceListResponse> listResponse = provinceService.listByPage(params);
        return ApiResponse.ok(listResponse);
    }

    @PostMapping
    public ApiResponse<ProvinceDetailResponse> createProvince(@RequestBody ProvinceCreateRequest request) {
        return ApiResponse.ok(provinceService.createProvince(request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteProvince(@PathVariable Integer id) {
        provinceService.deleteProvince(id);
        return ApiResponse.ok();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProvinceDetailResponse> getProvince(@PathVariable Integer id) {
        ProvinceDetailResponse province = provinceService.getProvinceById(id);
        return ApiResponse.ok(province);
    }

    @PutMapping(value = "/{id}")
    public ApiResponse<ProvinceDetailResponse> updateProvince(@PathVariable Integer id,
                                                             @RequestBody ProvinceUpdateRequest request) {
        return ApiResponse.ok(provinceService.updateProvince(id, request));
    }

    @GetMapping("/form-select")
    public ApiResponse<List<FormSelectResponse>> listAllForFormSelection() {
        List<FormSelectResponse> provinces = provinceService.listAllForFormSelection();
        return ApiResponse.ok(provinces);
    }
}
