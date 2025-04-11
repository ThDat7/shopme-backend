package com.shopme.client.controller;

import com.shopme.client.dto.response.*;
import com.shopme.client.service.PromotionService;
import com.shopme.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    @GetMapping("/types")
    public ApiResponse<List<PromotionIdWithTypeResponse>> getTypeActive() {
        List<PromotionIdWithTypeResponse> types = promotionService.getPromotionTypeActive();
        return ApiResponse.ok(types);
    }

    @GetMapping("/{id}")
    public ApiResponse<PromotionDetailResponse> getCurrentPromotionActiveById(@PathVariable Integer id) {
        PromotionDetailResponse promotion = promotionService.getCurrentPromotionActiveById(id);
        return ApiResponse.ok(promotion);
    }

    @GetMapping("/{id}/products")
    public ApiResponse<ListResponse<ProductListResponse>> getProductsByPromotionId(@PathVariable Integer id,
                                                                                 @RequestParam Map<String, String> params) {
        ListResponse<ProductListResponse> products = promotionService.getProductsByPromotionId(id, params);
        return ApiResponse.ok(products);
    }
}
