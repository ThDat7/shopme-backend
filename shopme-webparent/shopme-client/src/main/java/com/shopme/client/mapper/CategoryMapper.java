package com.shopme.client.mapper;

import com.shopme.client.dto.response.CategoryBreadcrumbResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryBreadcrumbResponse toCategoryBreadcrumbResponse(Integer id, String name);
}
