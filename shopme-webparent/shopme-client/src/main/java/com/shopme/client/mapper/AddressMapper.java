package com.shopme.client.mapper;

import com.shopme.client.dto.request.AddressRequest;
import com.shopme.client.dto.response.AddressDetailResponse;
import com.shopme.client.dto.response.AddressResponse;
import com.shopme.common.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    default AddressResponse toAddressResponse(Address address) {
        String addressDetail = address.getAddressLine() + ", " + address.getWard().getName() + ", " +
                address.getWard().getDistrict().getName() + ", " + address.getWard().getDistrict().getProvince().getName();
        String recipientName = address.getFirstName() + " " + address.getLastName();
        return AddressResponse.builder()
                .id(address.getId())
                .recipientName(recipientName)
                .phoneNumber(address.getPhoneNumber())
                .address(addressDetail)
                .defaultForShipping(address.isDefaultForShipping())
                .build();
    }

    @Mapping(target = "wardId", source = "ward.id")
    AddressDetailResponse toAddressDetailResponse(Address address);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "ward", ignore = true)
    Address toAddress(AddressRequest addressRequest);
}
