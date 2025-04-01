package com.shopme.client.mapper;

import com.shopme.client.dto.response.ProductDetailResponse;
import com.shopme.client.dto.response.ProductListResponse;
import com.shopme.client.dto.response.ProductSpecificResponse;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductDetail;
import com.shopme.common.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = { ReviewMapper.class })
public interface ProductMapper {
    @Mapping(target = "mainImage", ignore = true)
    @Mapping(target = "averageRating", ignore = true)
    @Mapping(target = "reviewCount", ignore = true)
    @Mapping(target = "saleCount", ignore = true)
    @Mapping(target = "discountPrice", source = ".", qualifiedByName = "calculateDiscountPrice")
    ProductListResponse toProductListResponse(Product product);

    @Named("calculateDiscountPrice")
    default int calculateDiscountPrice(Product product) {
        return Math.round(product.getPrice() * (1 - product.getDiscountPercent() / 100));
    }

    @Mapping(target = "mainImage", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "breadcrumbs", ignore = true)
    @Mapping(target = "brand", source = "brand.name")
    @Mapping(target = "category", source = "category.name")
    ProductDetailResponse toProductDetailResponse(Product product);

    ProductSpecificResponse toProductSpecificResponse(ProductDetail productDetail);
}
