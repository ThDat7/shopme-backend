package com.shopme.client.controller;

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
}
