package com.shopme.admin.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShippingRateDetailResponse {
    private Integer id;
    private Integer countryId;
    private String state;
    private float rate;
    private int days;
    private boolean codSupported;
}
