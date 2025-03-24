package com.shopme.admin.service;

import com.shopme.admin.dto.response.ListResponse;
import com.shopme.admin.dto.response.OrderDetailResponse;
import com.shopme.admin.dto.response.OrderListResponse;

import java.util.Map;

public interface OrderService {
    ListResponse<OrderListResponse> listByPage(Map<String, String> params);

    OrderDetailResponse getOrderById(Integer id);
}
