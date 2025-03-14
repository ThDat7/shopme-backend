package com.shopme.admin.mapper;

import com.shopme.admin.dto.request.CategoryCreateRequest;
import com.shopme.admin.dto.response.CategoryDetailResponse;
import com.shopme.admin.dto.response.CategoryExportResponse;
import com.shopme.admin.dto.response.CategoryListResponse;
import com.shopme.admin.dto.response.CategorySearchResponse;
import com.shopme.common.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryListResponse toCategoryListResponse(Category category, boolean hasChildren);

    CategorySearchResponse toCategorySearchResponse(Category category, String breadcrumb);

    CategoryDetailResponse toCategoryDetailResponse(Category category, Integer parentID);

    CategoryExportResponse toCategoryExportResponse(Category category);

    @Mapping(target = "image", ignore = true)
    Category toEntity(CategoryCreateRequest request);


    @Named("mapCategoryIds")
    default Set<Integer> mapCategoryIds(Set<Category> categories) {
        if (categories == null) {
            return null;
        }
        return categories.stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
    }

    @Named("mapCategoryNames")
    default Set<String> mapCategoryNames(Set<Category> categories) {
        if (categories == null) {
            return null;
        }
        return categories.stream()
                .map(Category::getName)
                .collect(Collectors.toSet());
    }

}
