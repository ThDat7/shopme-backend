package com.shopme.client.service;

import com.shopme.client.dto.response.ListResponse;
import com.shopme.client.dto.response.ProductDetailResponse;
import com.shopme.client.dto.response.ProductListResponse;

import java.util.Map;

public interface ProductService {
    ListResponse<ProductListResponse> listByPage(Map<String, String> params);

    ProductDetailResponse getProductDetail(Integer id);

    ListResponse<ProductListResponse> getBestSellerProducts();

    ListResponse<ProductListResponse> getTrendingProducts(Map<String, String> params);

    ListResponse<ProductListResponse> getTopRatedProducts(Map<String, String> params);

    ListResponse<ProductListResponse> getTopDiscountedProducts(Map<String, String> params);
}
