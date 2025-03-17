package com.shopme.client.dto.response;

import com.shopme.common.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class ListCategoryResponse {
    private Set<CategoryResponse> categories;
    private Set<CategoryBreadcrumbResponse> breadcrumbs;
}
