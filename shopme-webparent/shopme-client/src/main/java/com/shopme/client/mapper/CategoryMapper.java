package com.shopme.client.mapper;

import com.shopme.client.dto.response.CategoryBreadcrumbResponse;
import com.shopme.client.dto.response.CategoryResponse;
import com.shopme.client.dto.response.ListCategoryResponse;
import com.shopme.common.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "image", ignore = true)
    CategoryResponse toCategoryResponse(Category category);

    ListCategoryResponse toListCategoryResponse(Set<CategoryResponse> categories, Set<CategoryBreadcrumbResponse> breadcrumbs);

    CategoryBreadcrumbResponse toCategoryBreadcrumbResponse(Category category);
    CategoryBreadcrumbResponse toCategoryBreadcrumbResponse(Integer id, String name, String alias);
}
