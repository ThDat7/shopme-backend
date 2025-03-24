package com.shopme.admin.controller;

import com.shopme.admin.dto.response.ListResponse;
import com.shopme.admin.dto.response.OrderDetailResponse;
import com.shopme.admin.dto.response.OrderListResponse;
import com.shopme.admin.service.OrderService;
import com.shopme.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ApiResponse<ListResponse<OrderListResponse>> listByPage(@RequestParam Map<String, String> params) {
        ListResponse<OrderListResponse> listResponse = orderService.listByPage(params);
        return ApiResponse.ok(listResponse);
    }

    @GetMapping("{id}")
    public ApiResponse<OrderDetailResponse> getOrder(@PathVariable Integer id) {
        OrderDetailResponse order = orderService.getOrderById(id);
        return ApiResponse.ok(order);
    }
}
