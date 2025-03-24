package com.shopme.client.controller;

import com.shopme.client.service.CheckoutService;
import com.shopme.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test-env")
@RequiredArgsConstructor
public class TestENVController {
    private final CheckoutService checkoutService;

    @PostMapping("/checkout/order/{orderId}/by-pass-payment")
    public ApiResponse<Void> payosTransferHandler(@PathVariable Integer orderId) {
        checkoutService.bypassPayment(orderId);
        return ApiResponse.ok();
    }
}
