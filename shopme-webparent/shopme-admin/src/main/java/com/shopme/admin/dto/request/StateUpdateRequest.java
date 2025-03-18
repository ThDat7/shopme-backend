package com.shopme.admin.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StateUpdateRequest {
    private Integer id;
    private String name;
    private Integer countryId;
}
