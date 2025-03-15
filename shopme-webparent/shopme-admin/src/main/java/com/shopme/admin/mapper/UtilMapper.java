package com.shopme.admin.mapper;

import com.shopme.admin.dto.response.FormSelectResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UtilMapper {
    FormSelectResponse toFormSelectResponse(String key, String value);
}
