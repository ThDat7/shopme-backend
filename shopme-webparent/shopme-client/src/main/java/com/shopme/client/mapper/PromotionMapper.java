package com.shopme.client.mapper;

import com.shopme.client.dto.response.PromotionDetailResponse;
import com.shopme.client.dto.response.PromotionHeaderResponse;
import com.shopme.client.dto.response.PromotionIdWithTypeResponse;
import com.shopme.client.dto.response.PromotionProductResponse;
import com.shopme.common.entity.Promotion;
import com.shopme.common.entity.PromotionProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PromotionMapper {
    PromotionDetailResponse toPromotionDetailResponse(Promotion promotion);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "price", source = "product.price")
    @Mapping(target = "productImage", ignore = true)
    @Mapping(target = "discountPrice", source = "promotionProduct", qualifiedByName = "calculateDiscountPrice")
    PromotionProductResponse toPromotionProductResponse(PromotionProduct promotionProduct);

    @Named("calculateDiscountPrice")
    default int calculateDiscountPrice(PromotionProduct promotionProduct) {
        return Math.round(promotionProduct.getProduct().getPrice() * (1 - promotionProduct.getDiscountPercent() / 100));
    }

    PromotionIdWithTypeResponse toPromotionIdWithTypeResponse(Promotion promotion);
}
