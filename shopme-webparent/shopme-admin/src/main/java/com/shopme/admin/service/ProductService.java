package com.shopme.admin.service;

import com.shopme.admin.dto.response.ListResponse;
import com.shopme.admin.dto.response.ProductExportResponse;
import com.shopme.admin.dto.response.ProductListResponse;

import java.util.List;
import java.util.Map;

public interface ProductService {
    ListResponse<ProductListResponse> listByPage(Map<String, String> params);

    void deleteProduct(Integer id);

    List<ProductExportResponse> listAllForExport();

    void updateProductStatus(Integer id, boolean status);
}
