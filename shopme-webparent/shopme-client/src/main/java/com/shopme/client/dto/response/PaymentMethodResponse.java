package com.shopme.client.dto.response;

import com.shopme.common.entity.PaymentMethod;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentMethodResponse {
    private PaymentMethod method;
    private String displayName;
    private String description;
    private String icon;
}
