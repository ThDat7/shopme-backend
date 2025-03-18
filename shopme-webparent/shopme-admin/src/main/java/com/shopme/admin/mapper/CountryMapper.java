package com.shopme.admin.mapper;

import com.shopme.admin.dto.request.CountryCreateRequest;
import com.shopme.admin.dto.response.CountryDetailResponse;
import com.shopme.admin.dto.response.CountryListResponse;
import com.shopme.common.entity.Country;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CountryMapper {
    CountryListResponse toCountryListResponse(Country country);

    CountryDetailResponse toCountryDetailResponse(Country country);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "states", ignore = true)
    Country toEntity(CountryCreateRequest request);
}
