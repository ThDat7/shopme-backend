package com.shopme.client.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AbstractPlaceOrderRequest {
    private List<Integer> cartItemIds;
    private Integer addressId;
}
