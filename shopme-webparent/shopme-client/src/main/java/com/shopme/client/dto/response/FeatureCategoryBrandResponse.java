package com.shopme.client.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FeatureCategoryBrandResponse {
    private Integer id;
    private String name;
    private Integer order;
    private Integer categoryId;
    private Integer brandId;
    private Integer parentId;
    private String icon;
}
