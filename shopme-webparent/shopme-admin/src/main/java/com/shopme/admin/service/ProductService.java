package com.shopme.admin.service;

import com.shopme.admin.dto.request.ProductCreateRequest;
import com.shopme.admin.dto.request.ProductUpdateRequest;
import com.shopme.admin.dto.response.ListResponse;
import com.shopme.admin.dto.response.ProductDetailResponse;
import com.shopme.admin.dto.response.ProductExportResponse;
import com.shopme.admin.dto.response.ProductListResponse;

import java.util.List;
import java.util.Map;

public interface ProductService {
    ListResponse<ProductListResponse> listByPage(Map<String, String> params);

    ProductDetailResponse createProduct(ProductCreateRequest request);

    void deleteProduct(Integer id);

    ProductDetailResponse getProductById(Integer id);

    ProductDetailResponse updateProduct(Integer id, ProductUpdateRequest request);

    List<ProductExportResponse> listAllForExport();

    void updateProductStatus(Integer id, boolean status);
}
