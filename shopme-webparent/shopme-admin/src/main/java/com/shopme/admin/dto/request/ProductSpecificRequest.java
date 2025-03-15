package com.shopme.admin.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSpecificRequest {
    private Integer id;
    private String name;
    private String value;
}
