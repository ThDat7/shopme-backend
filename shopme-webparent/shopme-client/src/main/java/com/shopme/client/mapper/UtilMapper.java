package com.shopme.client.mapper;

import com.shopme.client.dto.response.FormSelectResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UtilMapper {
    FormSelectResponse toFormSelectResponse(String key, String value);
}
