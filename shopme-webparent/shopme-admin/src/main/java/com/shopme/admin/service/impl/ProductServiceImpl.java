package com.shopme.admin.service.impl;

import com.shopme.admin.dto.request.ProductCreateRequest;
import com.shopme.admin.dto.request.ProductSpecificRequest;
import com.shopme.admin.dto.request.ProductUpdateRequest;
import com.shopme.admin.dto.response.ListResponse;
import com.shopme.admin.dto.response.ProductDetailResponse;
import com.shopme.admin.dto.response.ProductExportResponse;
import com.shopme.admin.dto.response.ProductListResponse;
import com.shopme.admin.mapper.ProductMapper;
import com.shopme.admin.repository.*;
import com.shopme.admin.service.FileUploadService;
import com.shopme.admin.service.ProductService;
import com.shopme.common.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final String DEFAULT_SORT_FIELD = "name";
    private static final String DEFAULT_SORT_DIRECTION = "asc";
    private static final int DEFAULT_PRODUCTS_PER_PAGE = 4;

    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;
    private final ProductImageRepository productImageRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    private final ProductMapper productMapper;
    private final FileUploadService fileUploadService;

    private String getProductMainImageURL(Product product) {
        return fileUploadService.getProductMainImageUrl(product.getId(), product.getMainImage());
    }

    private Set<String> getProductImageURLs(Product product) {
        return product.getImages().stream()
                .map(image -> fileUploadService.getProductImagesUrl(product.getId(), image.getName()))
                .collect(Collectors.toSet());
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
    @Transactional
    public ProductDetailResponse createProduct(ProductCreateRequest request) {
        if (productRepository.existsByName(request.getName()))
            throw new RuntimeException("Product already exists");

        Product product = productMapper.toEntity(request);
        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());

        setBrandAndCategory(product, request.getBrandId(), request.getCategoryId());
        setProductDetails(product, request.getDetails());

        if (request.getMainImage() == null || request.getMainImage().isEmpty())
            throw new RuntimeException("Product main image is required");
        String mainImageName = request.getMainImage().getOriginalFilename();
        product.setMainImage(mainImageName);
        productRepository.save(product);
        setProductMainImage(product, request.getMainImage());
        setProductImages(product, request.getImages());

        return productMapper.toProductDetailResponse(product);
    }

    @Override
    @Transactional
    public ProductDetailResponse updateProduct(Integer id, ProductUpdateRequest request) {
        if (productRepository.existsByNameAndIdNot(request.getName(), id))
            throw new RuntimeException("Product already exists");

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        updateProductField(request, product);
        product.setUpdatedTime(new Date());
        setBrandAndCategory(product, request.getBrandId(), request.getCategoryId());
        setProductDetails(product, request.getDetails());

        if (request.getMainImage() != null && !request.getMainImage().isEmpty())
            setProductMainImage(product, request.getMainImage());

        if (request.getImages() != null && !request.getImages().isEmpty())
            setProductImages(product, request.getImages());

        productRepository.save(product);
        return productMapper.toProductDetailResponse(product);
    }

    @Override
    public void deleteProduct(Integer id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }

    @Override
    public ProductDetailResponse getProductById(Integer id) {
        Product product = productRepository.findByIdWithImages(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        // Product product = productRepository.findById(id).orElseThrow(() -> new
        // RuntimeException("Product not
        // found"));
        var productDto = productMapper.toProductDetailResponse(product);
        productDto.setMainImage(getProductMainImageURL(product));
        productDto.setImages(getProductImageURLs(product));
        return productDto;
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

    private void setBrandAndCategory(Product product, Integer brandId, Integer categoryId) {
        if (categoryId == null || brandId == null)
            throw new RuntimeException("Category and Brand are required");

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);

        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new RuntimeException("Brand not found"));
        product.setBrand(brand);
    }

    private void setProductImages(Product product, Set<MultipartFile> images) {
        if (images == null || images.isEmpty())
            return;

        // Get existing images
        Set<String> existingImageNames = product.getImages() != null ? product.getImages().stream()
                .map(ProductImage::getName)
                .collect(Collectors.toSet()) : new HashSet<>();

        for (MultipartFile image : images) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));

            if (!existingImageNames.contains(fileName)) {
                ProductImage productImage = new ProductImage();
                productImage.setName(fileName);
                productImage.setProduct(product);

                if (product.getImages() == null) {
                    product.setImages(new HashSet<>());
                }
                product.getImages().add(productImage);

                fileUploadService.productImagesUpload(image, product.getId());
            }
        }
    }

    private void setProductMainImage(Product product, MultipartFile mainImage) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(mainImage.getOriginalFilename()));
        product.setMainImage(fileName);
        fileUploadService.productMainImageUpload(mainImage, product.getId());
    }

    private void setProductDetails(Product product, List<ProductSpecificRequest> details) {
        if (details == null || details.isEmpty())
            return;

        List<ProductDetail> existingDetails = product.getDetails();

        Map<String, String> detailsMap = details.stream()
                .collect(Collectors.toMap(ProductSpecificRequest::getName, ProductSpecificRequest::getValue));

        existingDetails.removeIf(detail -> !detailsMap.containsKey(detail.getName()));

        for (ProductSpecificRequest request : details) {
            Optional<ProductDetail> existingDetail = existingDetails.stream()
                    .filter(detail -> detail.getName().equals(request.getName()))
                    .findFirst();

            if (existingDetail.isPresent()) {
                existingDetail.get().setValue(request.getValue()); // Cập nhật giá trị
            } else {
                ProductDetail newDetail = productMapper.toProductDetail(request);
                newDetail.setProduct(product);
                existingDetails.add(newDetail); // Thêm mới
            }
        }
    }

    private void updateProductField(ProductUpdateRequest request, Product product) {
        product.setName(request.getName());
        product.setAlias(request.getAlias());
        product.setShortDescription(request.getShortDescription());
        product.setFullDescription(request.getFullDescription());
        product.setEnabled(request.isEnabled());
        product.setInStock(request.isInStock());
        product.setCost(request.getCost());
        product.setPrice(request.getPrice());
        product.setDiscountPercent(request.getDiscountPercent());
        product.setLength(request.getLength());
        product.setWidth(request.getWidth());
        product.setHeight(request.getHeight());
        product.setWeight(request.getWeight());
    }
}
