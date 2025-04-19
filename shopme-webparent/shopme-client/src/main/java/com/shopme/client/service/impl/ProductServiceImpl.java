package com.shopme.client.service.impl;

import com.nimbusds.jose.util.Pair;
import com.shopme.client.dto.request.ProductFilterType;
import com.shopme.client.dto.response.*;
import com.shopme.client.exception.type.ProductNotFoundException;
import com.shopme.client.mapper.ProductMapper;
import com.shopme.client.repository.ProductDetailRepository;
import com.shopme.client.repository.ProductImageRepository;
import com.shopme.client.repository.ProductRepository;
import com.shopme.client.repository.projection.ProductDetailProjection;
import com.shopme.client.service.CategoryService;
import com.shopme.client.service.FileUploadService;
import com.shopme.client.service.ProductPriceService;
import com.shopme.client.service.ProductService;
import com.shopme.client.specification.JoinHelper;
import com.shopme.client.specification.ProductSpecification;
import com.shopme.client.specification.RatingSortHelper;
import com.shopme.common.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private static final String DEFAULT_SORT_FIELD = Product_.CREATED_TIME;
    private static final String DEFAULT_SORT_DIRECTION = "asc";
    private static final int DEFAULT_PRODUCTS_PER_PAGE = 4;

    private final EntityManager em;
    private final FileUploadService fileUploadService;
    private final ProductPriceService productPriceService;
    private final CategoryService categoryService;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductDetailRepository productDetailRepository;
    private final ProductMapper productMapper;

    private Pageable getPageableFromParams(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "0"));
        int size = Integer.parseInt(params.getOrDefault("size", String.valueOf(DEFAULT_PRODUCTS_PER_PAGE)));
        String sortField = params.getOrDefault("sortField", DEFAULT_SORT_FIELD);
        String sortDirection = params.getOrDefault("sortDirection", DEFAULT_SORT_DIRECTION);

        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        return PageRequest.of(page, size, sort);
    }

    private String getProductMainImageURL(Integer productId, String mainImage) {
        return fileUploadService.getProductMainImageUrl(productId, mainImage);
    }

    private Set<String> getProductImageResponses(Integer productId, Set<ProductImage> images) {
        return images.stream()
                .map(image -> fileUploadService.getProductImagesUrl(productId, image.getName()))
                .collect(Collectors.toSet());
    }

    private Predicate conjunctWherePredicate(Map<String, String> params, Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        boolean haveHaving = false;
        // Parse filter parameters
        List<Integer> categoryIds = params.get("categoryIds") != null
                ? Stream.of(params.get("categoryIds").split(","))
                .map(Integer::parseInt)
                .toList()
                : null;

        List<Integer> brandIds = params.get("brandIds") != null
                ? Stream.of(params.get("brandIds").split(","))
                .map(Integer::parseInt)
                .toList()
                : null;

        ProductFilterType filterType = params.get("filterType") != null
                ? ProductFilterType.valueOf(params.get("filterType"))
                : ProductFilterType.ALL;

        Integer minPrice = params.get("minPrice") != null
                ? Integer.parseInt(params.get("minPrice"))
                : null;

        Integer maxPrice = params.get("maxPrice") != null
                ? Integer.parseInt(params.get("maxPrice"))
                : null;

        String keyword = params.get("keyword");

        Boolean inStock = params.get("inStock") != null
                ? Boolean.parseBoolean(params.get("inStock"))
                : null;

        Boolean hasPromotion = params.get("hasPromotion") != null
                ? Boolean.parseBoolean(params.get("hasPromotion"))
                : null;

        Integer promotionId = params.get("promotionId") != null
                ? Integer.parseInt(params.get("promotionId"))
                : null;

        // Build base specification
        Specification<Product> spec = Specification.where(ProductSpecification.isEnabled());

        // Apply filters
        if (categoryIds != null && !categoryIds.isEmpty()) {
            spec = spec.and(ProductSpecification.inCategoryTree(categoryIds));
        }

        if (brandIds != null && !brandIds.isEmpty()) {
            spec = spec.and(ProductSpecification.inBrands(brandIds));
        }

        if (keyword != null && !keyword.trim().isEmpty()) {
            spec = spec.and(ProductSpecification.hasKeyword(keyword));
        }

        if (minPrice != null) {
            spec = spec.and(ProductSpecification.priceGreaterThanOrEqual(minPrice));
        }

        if (maxPrice != null) {
            spec = spec.and(ProductSpecification.priceLessThanOrEqual(maxPrice));
        }

        if (inStock != null && inStock) {
            spec = spec.and(ProductSpecification.inStock());
        }

        if (hasPromotion != null) {
            if (hasPromotion) {
                spec = spec.and(ProductSpecification.hasActivePromotion());
            } else {
                // Negating hasActivePromotion would be complex with JPA Criteria,
                // so we'll handle this case in the repository with a custom query if needed
            }
        }

        if (promotionId != null) {
            spec = spec.and(ProductSpecification.byPromotionId(promotionId));
        }



        // Apply filter type specific logic
        if (filterType != null && filterType != ProductFilterType.ALL) {
            switch (filterType) {
                case BEST_SELLER:
                    // Filter for products with successful orders
                    spec = spec.and(ProductSpecification.withSuccessfulOrders());
                    // Sorting will be handled separately
                    break;

                case TRENDING:
                    // Filter for products with recent successful orders (last 7 days)
                    spec = spec.and(ProductSpecification.withSuccessfulOrders());
                    spec = spec.and(ProductSpecification.orderDateBetween(
                            Date.from(LocalDateTime.now().minusDays(7).atZone(ZoneId.systemDefault()).toInstant()),
                            Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())
                    ));
                    // Additional time-based filtering would be handled in repository
                    break;

                case HIGH_RATED:
                    // Filter for products with ratings
                    // No additional filtering needed, sorting will handle this
                    break;

                case DISCOUNTED:
                    // Products with discount
                    break;

                case ALL:
                default:
                    // No additional filter
                    break;
            }
        }

        return spec.toPredicate(root, query, cb);
    }

    private Predicate conjunctHavingPredicate(Map<String, String> params, Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Integer minRating = params.get("minRating") != null
                ? Integer.parseInt(params.get("minRating"))
                : null;

        if (minRating != null)
            return ProductSpecification.avgRatingGreaterThan(minRating)
                    .toPredicate(root, query, cb);

        return null;
    }

    @Override
    @Cacheable(value = "productListings", key = "T(org.springframework.util.StringUtils).collectionToCommaDelimitedString(#params.entrySet())")
    public ListResponse<ProductListResponse> listByPage(Map<String, String> params) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ProductListResponse> cq = cb.createQuery(ProductListResponse.class);
        Root<Product> root = cq.from(Product.class);

        // Get pageable from parameters
        Pageable pageable = getPageableFromParams(params);

        // JOINs
        Join<Product, OrderDetail> orderDetails = root.join(Product_.orderDetails, JoinType.LEFT);
        Join<OrderDetail, Review> reviews = orderDetails.join(OrderDetail_.review, JoinType.LEFT);

        // SELECT fields
        cq.multiselect(
                root.get(Product_.id).alias("id"),
                root.get(Product_.name).alias("name"),
                root.get(Product_.price).alias("price"),
                root.get(Product_.discountPercent).alias("discountPercent"),
                root.get(Product_.mainImage).alias("mainImage"),
                cb.coalesce(cb.avg(reviews.get(Review_.rating)), 0.0).alias("averageRating"),
                cb.countDistinct(reviews.get(Review_.id)).alias("reviewCount"),
                cb.countDistinct(orderDetails.get(OrderDetail_.id)).alias("saleCount")
        );

        // Apply filters
        cq.where(conjunctWherePredicate(params, root, cq, cb));

        // GROUP BY
        cq.groupBy(root.get(Product_.id));
        Predicate havingPredicate = conjunctHavingPredicate(params, root, cq, cb);
        boolean hasHaving = havingPredicate != null;
        if (hasHaving)
            cq.having(havingPredicate);

        // Apply sorting
        applySorting(cq, cb, root, params);

        // Execute query with pagination
        TypedQuery<ProductListResponse> query = em.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<ProductListResponse> results = query.getResultList();

        // Count total records for pagination
        Long total;
        if (hasHaving)
            total = countWithHaving(cb, params);
        else
            total = countWithoutHaving(cb, params);

        // Create page object
        Page<ProductListResponse> page = new PageImpl<>(results, pageable, total);
        return temporaryMapper(page);
    }

    private void applySorting(CriteriaQuery<?> cq, CriteriaBuilder cb, Root<Product> root,
                              Map<String, String> params) {

        ProductFilterType filterType = params.get("filterType") != null
                ? ProductFilterType.valueOf(params.get("filterType"))
                : ProductFilterType.ALL;

        String sortField = params.getOrDefault("sortField", DEFAULT_SORT_FIELD);
        String sortDirection = params.getOrDefault("sortDirection", DEFAULT_SORT_DIRECTION);

        // Apply sort based on filter type if no explicit sort is provided
    Join<Product, OrderDetail> orderDetailJoin = JoinHelper.join(root, Product_.orderDetails, JoinType.LEFT);
    Join<OrderDetail, Review> reviewJoin = JoinHelper.join(orderDetailJoin, OrderDetail_.review, JoinType.LEFT);

    switch (filterType) {
        case BEST_SELLER:
            cq.orderBy(cb.desc(cb.countDistinct(orderDetailJoin.get(OrderDetail_.id))));
            break;

        case TRENDING:
            cq.orderBy(cb.desc(cb.countDistinct(orderDetailJoin.get(OrderDetail_.id))));
            break;

        case HIGH_RATED:
            RatingSortHelper.applyBayesianRatingSort(cq, cb, reviewJoin);
            break;

        case DISCOUNTED:
            // CHECK THEM PROMOTION =)))
            cq.orderBy(cb.desc(root.get(Product_.discountPercent)));
            break;

        default:
            if (sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())) {
                cq.orderBy(cb.asc(root.get(sortField)));
            } else {
                cq.orderBy(cb.desc(root.get(sortField)));
            }
            break;
    }
    }

    private Long countWithHaving(CriteriaBuilder cb, Map<String, String> params) {
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Product> root = countQuery.from(Product.class);

        // Tạo subquery để lấy các product IDs thỏa mãn điều kiện
        Subquery<Integer> subquery = countQuery.subquery(Integer.class);
        Root<Product> subRoot = subquery.from(Product.class);
        Join<Product, OrderDetail> orderDetails = subRoot.join(Product_.orderDetails, JoinType.LEFT);
        Join<OrderDetail, Review> reviews = orderDetails.join(OrderDetail_.review, JoinType.LEFT);

        // Áp dụng điều kiện WHERE
        Predicate wherePredicate = conjunctWherePredicate(params, subRoot, countQuery, cb);
        subquery.where(wherePredicate);

        // Nhóm và lọc theo HAVING
        subquery.groupBy(subRoot.get(Product_.id));

        Predicate havingPredicate = conjunctHavingPredicate(params, subRoot, countQuery, cb);
        if (havingPredicate != null) {
            subquery.having(havingPredicate);
        }

        // Chọn product IDs
        subquery.select(subRoot.get(Product_.id));

        // Đếm số lượng product IDs distinct từ subquery
        countQuery.select(cb.countDistinct(root.get(Product_.id)))
                .where(root.get(Product_.id).in(subquery));

        return em.createQuery(countQuery).getSingleResult();
    }

    private Long countWithoutHaving(CriteriaBuilder cb, Map<String, String> params) {
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Product> root = query.from(Product.class);

        query.select(cb.countDistinct(root));
        query.where(conjunctWherePredicate(params, root, query, cb));

        return em.createQuery(query).getSingleResult();
    }

    private ListResponse<ProductListResponse> temporaryMapper(Page<ProductListResponse> productListResponses) {
        List<ProductListResponse> fullInfoDto = productListResponses.stream()
                .map(product -> {
                    product.setMainImage(getProductMainImageURL(product.getId(), product.getMainImage()));
                    ProductPriceResponse productPriceResponse = productPriceService
                            .calculateProductPriceResponse(product.getId());
                    product.setPrice(productPriceResponse.getPrice());
                    product.setDiscountPercent(productPriceResponse.getDiscountPercent());
                    product.setDiscountPrice(productPriceResponse.getDiscountPrice());

                    return product;
                }).toList();

        return ListResponse.<ProductListResponse>builder()
                .content(fullInfoDto)
                .totalPages(productListResponses.getTotalPages())
                .build();
    }

    private ListResponse<ProductListResponse> productPageToListProductResponse(Page<Product> productPage) {
        List<ProductListResponse> productListResponses = productPage.getContent().stream()
                .map(product -> {
                    var productDto = productMapper.toProductListResponse(product);
                    productDto.setMainImage(getProductMainImageURL(product.getId(), product.getMainImage()));

                    ProductPriceResponse productPriceResponse = productPriceService
                            .calculateProductPriceResponse(product.getId());
                    productDto.setPrice(productPriceResponse.getPrice());
                    productDto.setDiscountPercent(productPriceResponse.getDiscountPercent());
                    productDto.setDiscountPrice(productPriceResponse.getDiscountPrice());

                    return productDto;
                })
                .collect(Collectors.toList());

        return ListResponse.<ProductListResponse>builder()
                .content(productListResponses)
                .totalPages(productPage.getTotalPages())
                .build();
    }

    @Override
    @Cacheable(value = "productDetail", key = "#id")
    public ProductDetailResponse getProductDetail(Integer id) {
        ProductDetailProjection productProject = productRepository.getProductDetail(id)
                .orElseThrow(ProductNotFoundException::new);
        Set<CategoryBreadcrumbResponse> categoryBreadcrumbs = categoryService
                .findParentCategories(productProject.getCategoryId());

        var productDto = productMapper.toProductDetailResponse(productProject);

        var productImages = productImageRepository.findAllByProductId(id);
        productDto.setImages(productImages.stream()
                .map(image -> fileUploadService.getProductImagesUrl(id, image.getName()))
                .collect(Collectors.toSet()));
        var productDetails = productDetailRepository.findAllByProductId(id);
        productDto.setDetails(productDetails.stream()
                .map(productMapper::toProductSpecificResponse)
                .toList());

        productDto.setMainImage(getProductMainImageURL(productProject.getId(), productProject.getMainImage()));
        productDto.setBreadcrumbs(categoryBreadcrumbs);

        ProductPriceResponse productPriceResponse = productPriceService
                .calculateProductPriceResponse(productProject.getId());
        productDto.setPrice(productPriceResponse.getPrice());
        productDto.setDiscountPercent(productPriceResponse.getDiscountPercent());
        productDto.setDiscountPrice(productPriceResponse.getDiscountPrice());

        return productDto;
    }

    private List<ProductListResponse> toProductListResponses(List<Object[]> productObjects) {
        return productObjects.stream()
                .map(product -> {
                    ProductListResponse productDto = ProductListResponse.builder()
                            .id((Integer) product[0])
                            .name((String) product[1])
//                            .price((int) product[2])
//                            .discountPercent(((Float) product[3]))
                            .mainImage((String) product[4])
                            .averageRating(((Double) product[5]).floatValue())
                            .reviewCount(((Long) product[6]).intValue())
                            .saleCount(((Long) product[7]).intValue())
                            .build();

                    productDto.setMainImage(getProductMainImageURL(productDto.getId(), productDto.getMainImage()));

                    ProductPriceResponse productPriceResponse = productPriceService
                            .calculateProductPriceResponse(productDto.getId());
                    productDto.setPrice(productPriceResponse.getPrice());
                    productDto.setDiscountPercent(productPriceResponse.getDiscountPercent());
                    productDto.setDiscountPrice(productPriceResponse.getDiscountPrice());

                    return productDto;
                })
                .collect(Collectors.toList());
    }

    private ListResponse<ProductListResponse> getListProductResponse(Page<Object[]> productPage) {
        List<ProductListResponse> productListResponses = toProductListResponses(productPage.getContent());
        return ListResponse.<ProductListResponse>builder()
                .content(productListResponses)
                .totalPages(productPage.getTotalPages())
                .build();
    }

    @Override
    @Cacheable(value = "bestSellers")
    public ListResponse<ProductListResponse> getBestSellerProducts() {
        Pageable pageable = PageRequest.of(0, 4);
        Page<Object[]> productPage = productRepository.getBestSelling(pageable);
        return getListProductResponse(productPage);
    }

    @Override
    @Cacheable(value = "trendingProducts")
    public ListResponse<ProductListResponse> getTrendingProducts(Map<String, String> params) {
        Pageable pageable = PageRequest.of(0, 4);

//        trending last 7 days
        Date start = new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000);
        Date end = new Date();
        Page<Object[]> productPage = productRepository.getTrending(pageable, start, end);
        return getListProductResponse(productPage);
    }

    @Override
    @Cacheable(value = "topRatedProducts") 
    public ListResponse<ProductListResponse> getTopRatedProducts(Map<String, String> params) {
        Pageable pageable = PageRequest.of(0, 4);
        Page<Object[]> productPage = productRepository.getTopRated(pageable);
        return getListProductResponse(productPage);
    }

    @Override
    @Cacheable(value = "discountedProducts")
    public ListResponse<ProductListResponse> getTopDiscountedProducts(Map<String, String> params) {
        Pageable pageable = PageRequest.of(0, 4);
        Page<Object[]> productPage = productRepository.getTopDiscounted(pageable);
        return getListProductResponse(productPage);
    }

}
