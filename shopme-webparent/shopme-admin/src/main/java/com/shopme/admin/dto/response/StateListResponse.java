package com.shopme.admin.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StateListResponse {
    private Integer id;
    private String name;
    private String country;
}
