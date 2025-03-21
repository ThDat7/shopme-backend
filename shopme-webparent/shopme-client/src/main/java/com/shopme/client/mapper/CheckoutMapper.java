package com.shopme.client.mapper;

import com.shopme.client.dto.response.PayOSACKResponse;
import com.shopme.client.dto.response.PlaceOrderPayOSResponse;
import com.shopme.client.dto.response.PaymentMethodResponse;
import com.shopme.common.entity.PaymentMethod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.payos.type.CheckoutResponseData;

@Mapper(componentModel = "spring")
public interface CheckoutMapper {
    PlaceOrderPayOSResponse toPlaceOrderPayOSResponse(CheckoutResponseData data);

    @Mapping(target = "method", source = ".")
    PaymentMethodResponse toPaymentMethodResponse(PaymentMethod paymentMethod);
}
