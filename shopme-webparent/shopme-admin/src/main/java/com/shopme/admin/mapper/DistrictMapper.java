package com.shopme.admin.mapper;

import com.shopme.admin.dto.request.DistrictCreateRequest;
import com.shopme.admin.dto.response.DistrictDetailResponse;
import com.shopme.admin.dto.response.DistrictListResponse;
import com.shopme.common.entity.District;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DistrictMapper {
    @Mapping(target = "province", source = "province.name")
    DistrictListResponse toDistrictListResponse(District district);

    @Mapping(target = "provinceId", source = "province.id")
    DistrictDetailResponse toDistrictDetailResponse(District district);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "province", ignore = true)
    District toEntity(DistrictCreateRequest request);
}
