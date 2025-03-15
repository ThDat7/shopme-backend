package com.shopme.admin.controller;


import com.shopme.admin.dto.request.BrandCreateRequest;
import com.shopme.admin.dto.request.BrandUpdateRequest;
import com.shopme.admin.dto.response.*;
import com.shopme.admin.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @GetMapping
    public ApiResponse<ListResponse<BrandListResponse>> listByPage(@RequestParam Map<String, String> params) {
        ListResponse<BrandListResponse> listResponse = brandService.listByPage(params);
        return ApiResponse.ok(listResponse);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<BrandDetailResponse> createBrand(@ModelAttribute BrandCreateRequest request) {
        return ApiResponse.ok(brandService.createBrand(request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteBrand(@PathVariable Integer id) {
        brandService.deleteBrand(id);
        return ApiResponse.ok();
    }

    @GetMapping("/{id}")
    public ApiResponse<BrandDetailResponse> getBrand(@PathVariable Integer id) {
        BrandDetailResponse brand = brandService.getBrandById(id);
        return ApiResponse.ok(brand);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<BrandDetailResponse> updateBrand(@PathVariable Integer id,
                                                        @ModelAttribute BrandUpdateRequest request) {
        return ApiResponse.ok(brandService.updateBrand(id, request));
    }

    @GetMapping("/all")
    public ApiResponse<List<BrandExportResponse>> listAllForExport() {
        var brands = brandService.listAllForExport();
        return ApiResponse.ok(brands);
    }

    @GetMapping("/form-select")
    public ApiResponse<List<FormSelectResponse>> listAllForFormSelection() {
        List<FormSelectResponse> brands = brandService.listAllForFormSelection();
        return ApiResponse.ok(brands);
    }

    @GetMapping("/{id}/categories")
    public ApiResponse<List<FormSelectResponse>> listCategoriesByBrand(@PathVariable Integer id) {
        List<FormSelectResponse> categories = brandService.listCategoriesFormSelectByBrand(id);
        return ApiResponse.ok(categories);
    }
}
