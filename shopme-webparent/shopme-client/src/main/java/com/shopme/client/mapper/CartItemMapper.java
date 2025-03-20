package com.shopme.client.mapper;

import com.shopme.client.dto.response.CartItemResponse;
import com.shopme.common.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "mainImage", source = "product.mainImage")
    @Mapping(target = "price", source = "product.price")
    @Mapping(target = "discount", source = "product.discountPercent")
    CartItemResponse toCartItemResponse(CartItem cartItem);
}
