package com.shopme.admin.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DistrictDetailResponse {
    private Integer id;
    private String name;
    private Integer provinceId;
}
