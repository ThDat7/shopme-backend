package com.shopme.client.mapper;

import com.shopme.client.dto.response.ProductDetailResponse;
import com.shopme.client.dto.response.ProductListResponse;
import com.shopme.client.dto.response.ProductSpecificResponse;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "mainImage", ignore = true)
    ProductListResponse toProductListResponse(Product product);

    @Mapping(target = "mainImage", source = "mainImage")
    @Mapping(target = "brand", source = "brand.name")
    @Mapping(target = "category", source = "category.name")
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "breadcrumbs", ignore = true)
    ProductDetailResponse toProductDetailResponse(Product product);

    ProductSpecificResponse toProductSpecificResponse(ProductDetail productDetail);
}
