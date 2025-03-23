package com.shopme.client.service;

import com.shopme.client.dto.request.OrderReviewRequest;
import com.shopme.client.dto.response.ListResponse;
import com.shopme.client.dto.response.OrderReviewResponse;
import com.shopme.client.dto.response.ProductReviewResponse;

import java.util.Map;

public interface ReviewService {
    ListResponse<ProductReviewResponse> listPageByProduct(Map<String, String> params, Integer productId);

    void deleteReview(Integer orderDetailId);

    OrderReviewResponse updateReview(Integer orderDetailId, OrderReviewRequest request);

    OrderReviewResponse writeReview(Integer orderDetailId, OrderReviewRequest request);
}
