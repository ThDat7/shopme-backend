package com.shopme.client.controller;

import com.shopme.client.dto.request.CalculateShippingRequest;
import com.shopme.client.dto.request.PlaceOrderRequest;
import com.shopme.client.dto.response.CalculateShippingResponse;
import com.shopme.client.service.CheckoutService;
import com.shopme.client.service.ShippingService;
import com.shopme.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/checkout")
@RequiredArgsConstructor
public class CheckoutController {
    private final CheckoutService checkoutService;
    private final ShippingService shippingService;

    @PostMapping("/calculate-shipping")
    public ApiResponse<CalculateShippingResponse> calculateShipping(@RequestBody CalculateShippingRequest request) {
        return ApiResponse.ok(shippingService.calculateShipping(request));
    }

    @PostMapping("/payment/COD")
    public ApiResponse<Void> placeOrder(@RequestBody PlaceOrderRequest request) {
        checkoutService.placeOrder(request);
        return ApiResponse.ok();
    }
}
