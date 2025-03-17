package com.shopme.client.controller;

import com.shopme.client.dto.response.CategoryResponse;
import com.shopme.client.dto.response.ListCategoryResponse;
import com.shopme.client.service.CategoryService;
import com.shopme.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/leaf-categories")
    public ApiResponse<List<CategoryResponse>> listLeafCategories() {
        List<CategoryResponse> categories = categoryService.listLeafCategories();
        return ApiResponse.ok(categories);
    }

    @GetMapping("/{id}/children")
    public ApiResponse<ListCategoryResponse> getChildCategories(@PathVariable Integer id) {
        ListCategoryResponse category = categoryService.getChildCategories(id);
        return ApiResponse.ok(category);
    }

}
