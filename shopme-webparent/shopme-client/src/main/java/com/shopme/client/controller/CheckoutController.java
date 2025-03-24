package com.shopme.client.controller;

import com.shopme.client.dto.request.CalculateShippingRequest;
import com.shopme.client.dto.request.PlaceOrderCODRequest;
import com.shopme.client.dto.request.PlaceOrderPayOSRequest;
import com.shopme.client.dto.response.CalculateShippingResponse;
import com.shopme.client.dto.response.PayOSACKResponse;
import com.shopme.client.dto.response.PlaceOrderPayOSResponse;
import com.shopme.client.dto.response.PaymentMethodResponse;
import com.shopme.client.service.CheckoutService;
import com.shopme.client.service.ShippingService;
import com.shopme.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vn.payos.type.Webhook;

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
    public ApiResponse<Void> placeOrderCOD(@RequestBody PlaceOrderCODRequest request) {
        checkoutService.placeOrderCOD(request);
        return ApiResponse.ok();
    }

    @PostMapping("/payment/PAY_OS")
    public ApiResponse<PlaceOrderPayOSResponse> placeOrderPayOS(@RequestBody PlaceOrderPayOSRequest request) {
        return ApiResponse.ok(checkoutService.placeOrderPayOS(request));
    }

    @PostMapping("/payment/PAY_OS/handler")
    public ApiResponse<PayOSACKResponse> payosTransferHandler(@RequestBody Webhook webhookBody) {
        return ApiResponse.ok(checkoutService.payosTransferHandler(webhookBody));
    }
}
