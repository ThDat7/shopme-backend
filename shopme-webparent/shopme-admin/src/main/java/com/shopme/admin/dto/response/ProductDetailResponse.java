package com.shopme.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailResponse {
    private Integer id;
    private String name;
    private String alias;

    private String shortDescription;
    private String fullDescription;

    private Date createdTime;
    private Date updatedTime;

    private boolean enabled;
    private boolean inStock;

    private float cost;
    private float price;
    private float discountPercent;

    private float length;
    private float width;
    private float height;
    private float weight;

    private String mainImage;
    private Integer categoryId;
    private Integer brandId;

    private Set<ProductImageResponse> images;
    private List<ProductSpecificResponse> details;
}
