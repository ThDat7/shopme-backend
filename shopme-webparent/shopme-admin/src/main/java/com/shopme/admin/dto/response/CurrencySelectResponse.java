package com.shopme.admin.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CurrencySelectResponse {
    private Integer id;
    private String name;
    private String symbol;
    private String code;
}
