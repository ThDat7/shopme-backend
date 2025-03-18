package com.shopme.admin.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StateDetailResponse {
    private Integer id;
    private String name;
    private Integer countryId;
}
