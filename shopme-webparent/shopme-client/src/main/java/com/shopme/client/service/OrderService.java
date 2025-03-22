package com.shopme.client.service;

import com.shopme.client.dto.response.ListResponse;
import com.shopme.client.dto.response.OrderDetailResponse;
import com.shopme.client.dto.response.OrderListResponse;
import com.shopme.common.entity.OrderStatus;

import java.util.Map;

public interface OrderService {
    void cancelOrder(Integer orderId);

    OrderStatus getOrderStatus(Integer orderId);

    ListResponse<OrderListResponse> getOrders(Map<String, String> params);

    OrderDetailResponse getOrder(Integer orderId);
}
