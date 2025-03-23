package com.shopme.client.mapper;

import com.shopme.client.dto.response.ProductReviewResponse;
import com.shopme.common.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(target = "firstName", source = "orderDetail.order.customer.firstName")
    @Mapping(target = "lastName", source = "orderDetail.order.customer.lastName")
    ProductReviewResponse toProductReviewResponse(Review review);
}
