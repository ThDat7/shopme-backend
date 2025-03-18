package com.shopme.admin.mapper;

import com.shopme.admin.dto.request.StateCreateRequest;
import com.shopme.admin.dto.response.StateDetailResponse;
import com.shopme.admin.dto.response.StateListResponse;
import com.shopme.common.entity.State;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StateMapper {
    @Mapping(target = "country", source = "country.name")
    StateListResponse toStateListResponse(State state);

    @Mapping(target = "countryId", source = "country.id")
    StateDetailResponse toStateDetailResponse(State state);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "country", ignore = true)
    State toEntity(StateCreateRequest request);
}
