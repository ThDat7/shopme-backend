package com.shopme.client.controller;

import com.shopme.client.dto.response.ListResponse;
import com.shopme.client.dto.response.ProductDetailResponse;
import com.shopme.client.dto.response.ProductListResponse;
import com.shopme.client.service.ProductService;
import com.shopme.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ApiResponse<ProductDetailResponse> getProduct(@PathVariable Integer id) {
        ProductDetailResponse product = productService.getProductDetail(id);
        return ApiResponse.ok(product);
    }

    @GetMapping("/best-seller")
    public ApiResponse<ListResponse<ProductListResponse>> getBestSellerProducts() {
        ListResponse<ProductListResponse> products = productService.getBestSellerProducts();
        return ApiResponse.ok(products);
    }

    @GetMapping("/trending")
    public ApiResponse<ListResponse<ProductListResponse>> getTrendingProducts(@RequestParam Map<String, String> params) {
        ListResponse<ProductListResponse> products = productService.getTrendingProducts(params);
        return ApiResponse.ok(products);
    }
}
