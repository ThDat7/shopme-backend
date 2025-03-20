package com.shopme.client.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartItemRequest {
    private Integer productId;
    private Integer quantity;
}
