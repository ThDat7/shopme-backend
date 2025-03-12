package com.shopme.admin.controller;

import com.shopme.admin.dto.response.ApiResponse;
import com.shopme.admin.dto.response.CategoryListResponse;
import com.shopme.admin.dto.response.CategorySearchResponse;
import com.shopme.admin.dto.response.ListResponse;
import com.shopme.admin.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ApiResponse<ListResponse<CategoryListResponse>> listByPage(@RequestParam Map<String, String> params) {
        ListResponse<CategoryListResponse> listResponse = categoryService.listByPage(params);
        return ApiResponse.ok(listResponse);
    }

    @GetMapping("/search")
    public ApiResponse<ListResponse<CategorySearchResponse>> search(@RequestParam Map<String, String> params) {
        return ApiResponse.ok(categoryService.search(params));
    }

    @GetMapping("/{id}/children")
    public ApiResponse<List<CategoryListResponse>> listChildren(@PathVariable Integer id) {
        return ApiResponse.ok(categoryService.listChildren(id));
    }
}
