package com.shopme.client.controller;

import com.shopme.client.service.OrderService;
import com.shopme.common.dto.response.ApiResponse;
import com.shopme.common.entity.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/{orderId}/cancel")
    public ApiResponse<Void> cancelOrder(@PathVariable Integer orderId) {
        orderService.cancelOrder(orderId);
        return ApiResponse.ok();
    }

    @GetMapping("/{orderId}/status")
    public ApiResponse<OrderStatus> getOrderStatus(@PathVariable Integer orderId) {
        return ApiResponse.ok(orderService.getOrderStatus(orderId));
    }
}
