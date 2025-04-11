package com.shopme.client.controller;

import com.shopme.client.dto.response.CategoryResponse;
import com.shopme.client.dto.response.ListCategoryResponse;
import com.shopme.client.dto.response.ListResponse;
import com.shopme.client.service.CategoryService;
import com.shopme.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/root")
    public ApiResponse<ListResponse<CategoryResponse>> listRootCategories(@RequestParam Map<String, String> params) {
        ListResponse<CategoryResponse> categories = categoryService.listRootCategories(params);
        return ApiResponse.ok(categories);
    }

    @GetMapping("/{id}/children")
    public ApiResponse<List<CategoryResponse>> getChildCategories(@PathVariable Integer id) {
        return ApiResponse.ok(categoryService.getChildCategories(id));
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getCategory(@PathVariable Integer id) {
        return ApiResponse.ok(categoryService.getCategory(id));
    }

}
