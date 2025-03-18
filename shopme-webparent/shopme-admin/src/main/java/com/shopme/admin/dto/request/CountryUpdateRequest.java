package com.shopme.admin.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CountryUpdateRequest {
    private Integer id;
    private String name;
    private String code;
}
