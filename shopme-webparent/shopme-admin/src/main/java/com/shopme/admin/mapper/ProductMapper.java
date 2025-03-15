package com.shopme.admin.mapper;

import com.shopme.admin.dto.response.ProductExportResponse;
import com.shopme.admin.dto.response.ProductListResponse;
import com.shopme.common.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "brand", source = "brand.name")
    @Mapping(target = "category", source = "category.name")
    ProductListResponse toProductListResponse(Product product);

    ProductExportResponse toProductExportResponse(Product product);
}
