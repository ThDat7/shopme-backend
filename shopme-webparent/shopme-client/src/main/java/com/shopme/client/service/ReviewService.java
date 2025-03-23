package com.shopme.client.service;

import com.shopme.client.dto.response.ProductReviewResponse;

import java.util.Map;

public interface ReviewService {
    ListResponse<ProductReviewResponse> listPageByProduct(Map<String, String> params, Integer productId);
}
