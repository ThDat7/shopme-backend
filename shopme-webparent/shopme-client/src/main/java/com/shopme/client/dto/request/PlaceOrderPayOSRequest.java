package com.shopme.client.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceOrderPayOSRequest extends AbstractPlaceOrderRequest {
    private String returnUrl;
    private String cancelUrl;
}
