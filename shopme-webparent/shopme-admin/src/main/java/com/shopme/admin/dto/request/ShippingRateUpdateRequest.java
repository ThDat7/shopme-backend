package com.shopme.admin.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShippingRateUpdateRequest {
    private Integer id;
    private Integer districtId;
    private int rate;
    private int days;
    private boolean codSupported;
}
