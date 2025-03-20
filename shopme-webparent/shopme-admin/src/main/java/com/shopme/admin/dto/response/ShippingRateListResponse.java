package com.shopme.admin.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShippingRateListResponse {
    private Integer id;
    private String country;
    private String state;
    private float rate;
    private int days;
    private boolean codSupported;
}
