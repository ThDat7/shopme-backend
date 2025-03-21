package com.shopme.client.dto.response;

import lombok.*;
import vn.payos.type.CheckoutResponseData;

@Data
@NoArgsConstructor
public class PlaceOrderPayOSResponse {
    private CheckoutResponseData data;
}
