package com.shopme.admin.mapper;

import com.shopme.admin.dto.request.BrandCreateRequest;
import com.shopme.admin.dto.response.BrandDetailResponse;
import com.shopme.admin.dto.response.BrandExportResponse;
import com.shopme.admin.dto.response.BrandListResponse;
import com.shopme.common.entity.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface BrandMapper {
    @Mapping(target = "categories", source = "categories", qualifiedByName = "mapCategoryNames")
    BrandListResponse toBrandListResponse(Brand brand);

    @Mapping(target = "categoryIds", source = "categories", qualifiedByName = "mapCategoryIds")
    BrandDetailResponse toBrandDetailResponse(Brand brand);

    @Mapping(target = "categories", source = "categories", qualifiedByName = "mapCategoryNames")
    BrandExportResponse toBrandExportResponse(Brand brand);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "logo", ignore = true)
    @Mapping(target = "categories", ignore = true)
    Brand toEntity(BrandCreateRequest request);
}
