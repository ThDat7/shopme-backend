package com.shopme.client.mapper;

import com.shopme.client.dto.response.CalculateShippingResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShippingMapper {
    CalculateShippingResponse toCalculateShippingResponse(Integer shippingCost);
}
