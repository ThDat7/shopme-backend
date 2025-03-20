package com.shopme.client.service;

import com.shopme.client.dto.request.CartItemRequest;
import com.shopme.client.dto.response.CartItemResponse;

import java.util.List;

public interface ShoppingCartService {
    List<CartItemResponse> getAll();

    CartItemResponse addProductToCart(CartItemRequest request);

    CartItemResponse updateQuantity(CartItemRequest request);

    void deleteCartItem(Integer id);
}
