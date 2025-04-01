package com.shopme.client.service;

import com.shopme.client.dto.request.CalculateShippingRequest;
import com.shopme.client.dto.response.CalculateShippingResponse;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.ShippingRate;

public interface ShippingService {
    CalculateShippingResponse calculateShipping(CalculateShippingRequest request);
    int calculateShippingCost(CartItem cartItem, ShippingRate shippingRate);

    ShippingRate getShippingRateByAddressId(Integer id);
}
