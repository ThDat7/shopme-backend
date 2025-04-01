package com.shopme.admin.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BaseProductRequest {
    protected String name;
    protected String alias;

    protected String shortDescription;
    protected String fullDescription;

    protected boolean enabled;
    protected boolean inStock;

    protected int cost;
    protected int price;
    protected float discountPercent;

    protected float length;
    protected float width;
    protected float height;
    protected float weight;

    protected Integer categoryId;
    protected Integer brandId;

    protected List<ProductSpecificRequest> details;
}
