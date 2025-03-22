package com.shopme.client.mapper;

import com.shopme.client.dto.response.*;
import com.shopme.common.entity.Order;
import com.shopme.common.entity.OrderDetail;
import com.shopme.common.entity.OrderTrack;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "createdAt", source = "orderTime")
    @Mapping(target = "totalPrice", source = "total")
    OrderListResponse toOrderListResponse(Order order);

    @Mapping(target = "createdAt", source = "orderTime")
    @Mapping(target = "shippingAddress", source = ".", qualifiedByName = "toShippingAddressResponse")
    @Mapping(target = "orderItems", source = "orderDetails")
    @Mapping(target = "totalPrice", source = "total")
    OrderDetailResponse toOrderDetailResponse(Order order);


    @Mapping(target = "phone", source = "phoneNumber")
    @Mapping(target = "address", expression = "java(order.getAddressLine1() + \" \" + order.getAddressLine2())")
    @Named("toShippingAddressResponse")
    ShippingAddressResponse toShippingAddressResponse(Order order);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productMainImage", source = "product.mainImage")
    @Mapping(target = "unitPrice", source = "unitPrice")
    OrderItemResponse toOrderItemResponse(OrderDetail orderDetail);

    OrderTrackResponse toOrderTrackResponse(OrderTrack orderTrack);
}
