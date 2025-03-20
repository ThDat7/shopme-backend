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
        String addressDetail = address.getAddressLine1() + ", " + address.getCity() + ", " + address.getCountry().getName();
        String recipientName = address.getFirstName() + " " + address.getLastName();
        return AddressResponse.builder()
                .id(address.getId())
                .recipientName(recipientName)
                .phoneNumber(address.getPhoneNumber())
                .address(addressDetail)
                .defaultForShipping(address.isDefaultForShipping())
                .build();
    }

    @Mapping(target = "countryName", source = "country.name")
    AddressDetailResponse toAddressDetailResponse(Address address);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "country", ignore = true)
    Address toAddress(AddressRequest addressRequest);
}
