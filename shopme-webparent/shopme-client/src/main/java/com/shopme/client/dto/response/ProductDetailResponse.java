package com.shopme.client.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

//    private Date createdTime;
//    private Date updatedTime;

    private boolean inStock;

    private float cost;
    private float price;
    private float discountPercent;

    private float length;
    private float width;
    private float height;
    private float weight;

    private String mainImage;
    private String category;
    private String brand;

    private Set<String> images;
    private List<ProductSpecificResponse> details;
    private Set<CategoryBreadcrumbResponse> breadcrumbs;
}
