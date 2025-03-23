package com.shopme.client.mapper;

import com.shopme.client.dto.request.OrderReviewRequest;
import com.shopme.client.dto.response.OrderReviewResponse;
import com.shopme.client.dto.response.ProductReviewResponse;
import com.shopme.common.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    OrderReviewResponse toOrderReviewResponse(Review review);

    @Mapping(target = "firstName", source = "orderDetail.order.customer.firstName")
    @Mapping(target = "lastName", source = "orderDetail.order.customer.lastName")
    ProductReviewResponse toProductReviewResponse(Review review);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reviewTime", ignore = true)
    @Mapping(target = "orderDetail", ignore = true)
    Review toReview(OrderReviewRequest orderReviewRequest);
}
