package com.shopme.client.mapper;

import com.shopme.client.dto.response.AuthenticationResponse;
import com.shopme.client.dto.response.CustomerInfoResponse;
import com.shopme.client.dto.response.IntrospectResponse;
import com.shopme.common.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthenticationMapper {
    AuthenticationResponse toAuthenticationResponse(String token, Customer customer);

    @Mapping(target = "valid", source = "valid")
    IntrospectResponse toIntrospectResponse(Boolean valid);

    CustomerInfoResponse toCustomerInfoResponse(Customer customer);
}
