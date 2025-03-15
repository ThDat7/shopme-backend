package com.shopme.admin.controller;

import com.shopme.admin.dto.response.ApiResponse;
import com.shopme.admin.dto.response.ListResponse;
import com.shopme.admin.dto.response.ProductExportResponse;
import com.shopme.admin.dto.response.ProductListResponse;
import com.shopme.admin.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ApiResponse<ListResponse<ProductListResponse>> listByPage(@RequestParam Map<String, String> params) {
        ListResponse<ProductListResponse> listResponse = productService.listByPage(params);
        return ApiResponse.ok(listResponse);
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ApiResponse.ok();
    }

    @GetMapping("/all")
    public ApiResponse<List<ProductExportResponse>> listAllForExport() {
        var products = productService.listAllForExport();
        return ApiResponse.ok(products);
    }

    @GetMapping("/{id}/enable/{status}")
    public ApiResponse updateProductStatus(@PathVariable Integer id, @PathVariable boolean status) {
        productService.updateProductStatus(id, status);
        return ApiResponse.ok();
    }
}
