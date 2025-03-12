package com.shopme.admin.mapper;

import com.shopme.admin.dto.response.CategoryListResponse;
import com.shopme.admin.dto.response.CategorySearchResponse;
import com.shopme.common.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryListResponse toCategoryListResponse(Category category, boolean hasChildren);

    CategorySearchResponse toCategorySearchResponse(Category category, String breadcrumb);

}
