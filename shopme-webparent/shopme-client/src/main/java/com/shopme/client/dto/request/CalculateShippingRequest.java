package com.shopme.client.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CalculateShippingRequest {
    private Integer addressId;
    private List<Integer> cartItemIds;
}
