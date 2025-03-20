package com.shopme.admin.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShippingRateCreateRequest {
    private Integer id;
    private Integer countryId;
    private String state;
    private float rate;
    private int days;
    private boolean codSupported;
}
