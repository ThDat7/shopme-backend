package com.shopme.admin.service.impl;

import com.shopme.admin.dto.response.ListResponse;
import com.shopme.admin.dto.response.ProductExportResponse;
import com.shopme.admin.dto.response.ProductListResponse;
import com.shopme.admin.mapper.ProductMapper;
import com.shopme.admin.repository.ProductRepository;
import com.shopme.admin.service.FileUploadService;
import com.shopme.admin.service.ProductService;
import com.shopme.common.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final String DEFAULT_SORT_FIELD = "name";
    private static final String DEFAULT_SORT_DIRECTION = "asc";
    private static final int DEFAULT_PRODUCTS_PER_PAGE = 4;

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;
    private final FileUploadService fileUploadService;

    private String getProductMainImageURL(Product product) {
        return fileUploadService.getProductMainImageUrl(product.getId(), product.getMainImage());
    }

    private Pageable getPageableFromParams(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "0"));
        int size = Integer.parseInt(params.getOrDefault("size", String.valueOf(DEFAULT_PRODUCTS_PER_PAGE)));
        String sortField = params.getOrDefault("sortField", DEFAULT_SORT_FIELD);
        String sortDirection = params.getOrDefault("sortDirection", DEFAULT_SORT_DIRECTION);

        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        return PageRequest.of(page, size, sort);
    }

    @Override
    public ListResponse<ProductListResponse> listByPage(Map<String, String> params) {
        Pageable pageable = getPageableFromParams(params);
        String keyword = params.getOrDefault("keyword", null);
        Integer categoryId = params.containsKey("categoryId") ? Integer.parseInt(params.get("categoryId")) : null;

        Page<Product> productPage = productRepository.findAll(keyword, categoryId, pageable);
        List<ProductListResponse> productListResponses = productPage.getContent().stream()
                .map(product -> {
                    var productDto = productMapper.toProductListResponse(product);
                    productDto.setMainImage(getProductMainImageURL(product));
                    return productDto;
                })
                .collect(Collectors.toList());

        return ListResponse.<ProductListResponse>builder()
                .content(productListResponses)
                .totalPages(productPage.getTotalPages())
                .build();
    }

    @Override
    public void deleteProduct(Integer id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }

    @Override
    public List<ProductExportResponse> listAllForExport() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::toProductExportResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void updateProductStatus(Integer id, boolean status) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setEnabled(status);
        productRepository.save(product);
    }
}
