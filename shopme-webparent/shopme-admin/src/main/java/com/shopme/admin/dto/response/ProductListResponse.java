package com.shopme.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductListResponse {
    private Integer id;
    private String name;
    private String brand;
    private String category;
    private boolean enabled;
    private String mainImage;
}
