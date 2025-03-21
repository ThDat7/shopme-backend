package com.shopme.client.service;

import com.shopme.client.dto.request.PlaceOrderRequest;

import java.util.List;

public interface CheckoutService {
    void placeOrder(PlaceOrderRequest request);
}
