package com.shopme.client.controller;

import com.shopme.client.dto.request.CartItemRequest;
import com.shopme.client.dto.response.CartItemResponse;
import com.shopme.client.service.ShoppingCartService;
import com.shopme.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    public ApiResponse<List<CartItemResponse>> getAll() {
        return ApiResponse.ok(shoppingCartService.getAll());
    }

    @PostMapping
    public ApiResponse<CartItemResponse> addProductToCart(@RequestBody CartItemRequest request) {
        return ApiResponse.ok(shoppingCartService.addProductToCart(request));
    }

    @PutMapping
    public ApiResponse<CartItemResponse> updateQuantity(@RequestBody CartItemRequest request) {
        return ApiResponse.ok(shoppingCartService.updateQuantity(request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteCartItem(@PathVariable Integer id) {
        shoppingCartService.deleteCartItem(id);
        return ApiResponse.ok();
    }
}
