package com.shopme.admin.mapper;

import com.shopme.admin.dto.response.OrderDetailResponse;
import com.shopme.admin.dto.response.OrderListResponse;
import com.shopme.admin.dto.response.OrderSpecificResponse;
import com.shopme.common.entity.Order;
import com.shopme.common.entity.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "address", source = ".", qualifiedByName = "mapAddress")
    @Mapping(target = "customerName", source = ".", qualifiedByName = "mapCustomerName")
    @Mapping(target = "customerId", source = "customer.id")
    OrderListResponse toOrderListResponse(Order order);

    OrderDetailResponse toOrderDetailResponse(Order order);

    @Named("mapAddress")
    default String mapAddress(Order order) {
        return order.getAddressLine() + ", " + order.getProvince() + ", " + order.getDistrict() + ", " + order.getWard();
    }

    @Named("mapCustomerName")
    default String mapCustomerName(Order order) {
        return order.getFirstName() + " " + order.getLastName();
    }

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    OrderSpecificResponse toOrderSpecificResponse(OrderDetail orderDetail);
}
