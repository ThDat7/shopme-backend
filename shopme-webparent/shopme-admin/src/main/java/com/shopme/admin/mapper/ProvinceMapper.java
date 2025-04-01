package com.shopme.admin.mapper;

import com.shopme.admin.dto.request.ProvinceCreateRequest;
import com.shopme.admin.dto.response.ProvinceDetailResponse;
import com.shopme.admin.dto.response.ProvinceListResponse;
import com.shopme.common.entity.Province;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProvinceMapper {
    ProvinceListResponse toProvinceListResponse(Province province);

    ProvinceDetailResponse toProvinceDetailResponse(Province province);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "districts", ignore = true)
    Province toEntity(ProvinceCreateRequest request);
}
