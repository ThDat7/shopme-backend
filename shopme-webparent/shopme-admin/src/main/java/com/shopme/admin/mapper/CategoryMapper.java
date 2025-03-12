package com.shopme.admin.mapper;

import com.shopme.admin.dto.request.CategoryCreateRequest;
import com.shopme.admin.dto.response.CategoryDetailResponse;
import com.shopme.admin.dto.response.CategoryExportResponse;
import com.shopme.admin.dto.response.CategoryListResponse;
import com.shopme.admin.dto.response.CategorySearchResponse;
import com.shopme.common.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryListResponse toCategoryListResponse(Category category, boolean hasChildren);

    CategorySearchResponse toCategorySearchResponse(Category category, String breadcrumb);

    CategoryDetailResponse toCategoryDetailResponse(Category category, Integer parentID);

    CategoryExportResponse toCategoryExportResponse(Category category);

    @Mapping(target = "image", ignore = true)
    Category toEntity(CategoryCreateRequest request);

}
