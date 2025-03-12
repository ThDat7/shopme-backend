package com.shopme.admin.controller;

import com.shopme.admin.dto.request.CategoryCreateRequest;
import com.shopme.admin.dto.request.CategoryUpdateRequest;
import com.shopme.admin.dto.response.*;
import com.shopme.admin.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<CategoryDetailResponse> createCategory(@ModelAttribute CategoryCreateRequest request) {
        return ApiResponse.ok(categoryService.createCategory(request));
    }

    @GetMapping("/{id}/children")
    public ApiResponse<List<CategoryListResponse>> listChildren(@PathVariable Integer id) {
        return ApiResponse.ok(categoryService.listChildren(id));
    }

    @GetMapping("/all-in-form")
    public ApiResponse<List<CategorySelectResponse>> listAllInForm() {
        return ApiResponse.ok(categoryService.getAllInForm());
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ApiResponse.ok();
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryDetailResponse> getCategory(@PathVariable Integer id) {
        CategoryDetailResponse category = categoryService.getCategoryById(id);
        return ApiResponse.ok(category);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<CategoryDetailResponse> updateCategory(@PathVariable Integer id,
                                                              @ModelAttribute CategoryUpdateRequest request) {
        return ApiResponse.ok(categoryService.updateCategory(id, request));
    }

    @PutMapping("/{id}/enable/{status}")
    public ApiResponse updateCategoryStatus(@PathVariable Integer id, @PathVariable boolean status) {
        categoryService.updateCategoryStatus(id, status);
        return ApiResponse.ok();
    }

    @GetMapping("/all")
    public ApiResponse<List<CategoryExportResponse>> listAllForExport() {
        var categories = categoryService.listAllForExport();
        return ApiResponse.ok(categories);
    }
}
