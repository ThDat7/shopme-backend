package com.shopme.admin.mapper;

import com.shopme.admin.dto.request.ProductCreateRequest;
import com.shopme.admin.dto.request.ProductSpecificRequest;
import com.shopme.admin.dto.response.ProductDetailResponse;
import com.shopme.admin.dto.response.ProductExportResponse;
import com.shopme.admin.dto.response.ProductListResponse;
import com.shopme.admin.dto.response.ProductSpecificResponse;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "brandId", source = "brand.id")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "images", ignore = true)
    ProductDetailResponse toProductDetailResponse(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "mainImage", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "details", ignore = true)
    Product toEntity(ProductCreateRequest productCreateRequest);

    @Mapping(target = "brand", source = "brand.name")
    @Mapping(target = "category", source = "category.name")
    ProductListResponse toProductListResponse(Product product);

    ProductExportResponse toProductExportResponse(Product product);

    ProductSpecificResponse toProductSpecificResponse(ProductDetail productDetail);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    ProductDetail toProductDetail(ProductSpecificRequest productSpecificRequest);
}
