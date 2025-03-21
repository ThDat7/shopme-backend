package com.shopme.client.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class PlaceOrderRequest {
    private List<Integer> cartItemIds;
    private Integer addressId;
}
