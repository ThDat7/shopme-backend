package com.shopme.admin.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DistrictCreateRequest {
    private String name;
    private Integer provinceId;
}
