package com.shopme.admin.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShippingRateUpdateRequest {
    private Integer id;
    private Integer districtId;
    private float rate;
    private int days;
    private boolean codSupported;
}
