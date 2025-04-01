package com.shopme.admin.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShippingRateListResponse {
    private Integer id;
    private String province;
    private String district;
    private float rate;
    private int days;
    private boolean codSupported;
}
