package com.shopme.admin.controller;

import com.shopme.admin.dto.request.ProductCreateRequest;
import com.shopme.admin.dto.request.ProductUpdateRequest;
import com.shopme.admin.dto.response.*;
import com.shopme.admin.service.ProductService;
import com.shopme.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ProductDetailResponse> createProduct(@ModelAttribute ProductCreateRequest request) {
        return ApiResponse.ok(productService.createProduct(request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ApiResponse.ok();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductDetailResponse> getProduct(@PathVariable Integer id) {
        ProductDetailResponse product = productService.getProductById(id);
        return ApiResponse.ok(product);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ProductDetailResponse> updateProduct(@PathVariable Integer id,
                                                            @ModelAttribute ProductUpdateRequest request) {
        return ApiResponse.ok(productService.updateProduct(id, request));
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
