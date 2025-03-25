package com.shopme.client.mapper;

import com.shopme.client.dto.request.CustomerRegistrationRequest;
import com.shopme.common.entity.Customer;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @BeanMapping(nullValuePropertyMappingStrategy =
        NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", ignore = true)
    Customer toCustomer(CustomerRegistrationRequest request);
}
