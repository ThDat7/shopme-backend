package com.shopme.client.service;

import com.shopme.common.entity.OrderStatus;

import java.util.Map;

public interface OrderService {
    void cancelOrder(Integer orderId);

    OrderStatus getOrderStatus(Integer orderId);
}
