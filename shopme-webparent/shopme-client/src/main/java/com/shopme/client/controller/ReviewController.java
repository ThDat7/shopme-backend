package com.shopme.client.controller;

import com.shopme.client.dto.request.OrderReviewRequest;
import com.shopme.client.dto.response.ListResponse;
import com.shopme.client.dto.response.OrderReviewResponse;
import com.shopme.client.dto.response.ProductReviewResponse;
import com.shopme.client.service.ReviewService;
import com.shopme.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/product/{productId}")
    public ApiResponse<ListResponse<ProductReviewResponse>> listPageByProduct(Map<String, String> params,
                                                                              @PathVariable("productId")
                                                                              Integer productId) {
        return ApiResponse.ok(reviewService.listPageByProduct(params, productId));
    }

    @PostMapping("/order-detail/{orderDetailId}")
    public ApiResponse<OrderReviewResponse> writeReview(@PathVariable("orderDetailId") Integer orderDetailId,
                                                      @RequestBody OrderReviewRequest request) {
        return ApiResponse.ok(reviewService.writeReview(orderDetailId, request));
    }

    @PutMapping("/order-detail/{orderDetailId}")
    public ApiResponse<OrderReviewResponse> updateReview(@PathVariable("orderDetailId") Integer orderDetailId,
                                                         @RequestBody OrderReviewRequest request) {
        return ApiResponse.ok(reviewService.updateReview(orderDetailId, request));
    }

    @DeleteMapping("/order-detail/{orderDetailId}")
    public ApiResponse<Void> deleteReview(@PathVariable("orderDetailId") Integer orderDetailId) {
        reviewService.deleteReview(orderDetailId);
        return ApiResponse.ok();
    }
}
