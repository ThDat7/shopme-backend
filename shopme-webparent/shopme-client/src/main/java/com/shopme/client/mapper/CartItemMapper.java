package com.shopme.client.mapper;

import com.shopme.client.dto.response.CartItemResponse;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "mainImage", source = "product.mainImage")
    @Mapping(target = "price", source = "product.price")
    @Mapping(target = "discountPercent", source = "product.discountPercent")
    @Mapping(target = "discountPrice", source="product", qualifiedByName = "calculateDiscountPrice")
    CartItemResponse toCartItemResponse(CartItem cartItem);

    @Named("calculateDiscountPrice")
    default float calculateDiscountPrice(Product product) {
        return (float) Math.round(product.getPrice() * (1 - product.getDiscountPercent() / 100) * 100) / 100;
    }
}
