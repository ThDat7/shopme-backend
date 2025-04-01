package com.shopme.admin.mapper;

import com.shopme.admin.dto.request.ShippingRateCreateRequest;
import com.shopme.admin.dto.response.ShippingRateDetailResponse;
import com.shopme.admin.dto.response.ShippingRateListResponse;
import com.shopme.common.entity.ShippingRate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShippingRateMapper {
    @Mapping(target = "province", source = "district.province.name")
    @Mapping(target = "district", source = "district.name")
    ShippingRateListResponse toShippingRateListResponse(ShippingRate shippingRate);

    @Mapping(target = "districtId", source = "district.id")
    ShippingRateDetailResponse toShippingRateDetailResponse(ShippingRate shippingRate);

    @Mapping(target = "district", ignore = true)
    ShippingRate toEntity(ShippingRateCreateRequest request);
}
