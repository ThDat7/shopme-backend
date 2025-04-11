package com.shopme.client.mapper;

import com.shopme.client.dto.response.FeatureCategoryBrandResponse;
import com.shopme.common.entity.FeatureCategoryBrand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FeatureCategoryBrandMapper {
    @Mapping(target = "icon", ignore = true)
    @Mapping(target = "brandId", source = "brand.id")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "parentId", source = "parent.id")
    FeatureCategoryBrandResponse toFeatureCategoryBrandResponse(FeatureCategoryBrand featureCategoryBrand);
}
