package com.shopme.client.specification;

import com.shopme.client.dto.request.ProductFilterType;
import com.shopme.common.entity.*;
import com.shopme.common.entity.Order;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static com.shopme.client.specification.JoinHelper.*;

public class ProductSpecification {
    
    public static Specification<Product> avgRatingGreaterThan(Integer rating) {
        return (root, query, cb) -> {
            if (rating == null) {
                return cb.conjunction();
            }

            Join<Product, OrderDetail> orderDetailJoin = join(root, "orderDetails", JoinType.LEFT);
            Join<OrderDetail, Review> reviewJoin = join(orderDetailJoin, "review", JoinType.LEFT);

            return cb.greaterThanOrEqualTo(
                    cb.coalesce(cb.avg(reviewJoin.get("rating")), 0.0),
                    cb.literal(rating.doubleValue()));
        };
    }
    
    public static Specification<Product> hasKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return cb.conjunction();
            }
            
            String likePattern = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                cb.like(cb.lower(root.get("name")), likePattern),
                cb.like(cb.lower(root.get("description")), likePattern)
            );
        };
    }
    
    public static Specification<Product> inCategories(List<Integer> categoryIds) {
        return (root, query, cb) -> {
            if (categoryIds == null || categoryIds.isEmpty()) {
                return cb.conjunction();
            }
            
            return root.get("category").get("id").in(categoryIds);
        };
    }
    
    public static Specification<Product> inBrands(List<Integer> brandIds) {
        return (root, query, cb) -> {
            if (brandIds == null || brandIds.isEmpty()) {
                return cb.conjunction();
            }
            
            return root.get("brand").get("id").in(brandIds);
        };
    }
    
    public static Specification<Product> priceGreaterThanOrEqual(Integer minPrice) {
        return (root, query, cb) -> {
            if (minPrice == null) {
                return cb.conjunction();
            }
            
            return cb.greaterThanOrEqualTo(root.get("price"), minPrice);
        };
    }
    
    public static Specification<Product> priceLessThanOrEqual(Integer maxPrice) {
        return (root, query, cb) -> {
            if (maxPrice == null) {
                return cb.conjunction();
            }
            
            return cb.lessThanOrEqualTo(root.get("price"), maxPrice);
        };
    }
    
    public static Specification<Product> inStock() {
        return (root, query, cb) -> cb.greaterThan(root.get("quantity"), 0);
    }
    
    public static Specification<Product> hasActivePromotion() {
        return (root, query, cb) -> {
            Join<Product, PromotionProduct> promotionProductJoin = join(root, "promotionProducts", JoinType.INNER);
            Join<PromotionProduct, Promotion> promotionJoin = join(promotionProductJoin, "promotion", JoinType.INNER);
            
            Date now = new Date();
            
            return cb.and(
                cb.lessThanOrEqualTo(promotionJoin.get("startDate"), now),
                cb.greaterThanOrEqualTo(promotionJoin.get("endDate"), now),
                cb.isTrue(promotionJoin.get("active"))
            );
        };
    }
    
    public static Specification<Product> isEnabled() {
        return (root, query, cb) -> cb.isTrue(root.get("enabled"));
    }
    
    public static Specification<Product> byFilterType(ProductFilterType filterType) {
        return (root, query, cb) -> {
            if (filterType == null || filterType == ProductFilterType.ALL) {
                return cb.conjunction();
            }
            
            switch (filterType) {
                case BEST_SELLER:
                    // This will be handled by sorting, not filtering
                    return cb.conjunction();
                    
                case TRENDING:
                    // This will be handled by sorting, not filtering
                    return cb.conjunction();
                    
                case HIGH_RATED:
                    // This will be handled by sorting, not filtering
                    return cb.conjunction();
                    
                case DISCOUNTED:
                    return cb.greaterThan(root.get("discountPercent"), 0);
                    
                default:
                    return cb.conjunction();
            }
        };
    }
    
    public static Specification<Product> withSuccessfulOrders() {
        return (root, query, cb) -> {
            Join<Product, OrderDetail> orderDetailJoin = join(root, "orderDetails", JoinType.INNER);
            Join<OrderDetail, Order> orderJoin = join(orderDetailJoin, "order", JoinType.INNER);
            
            // Consider DELIVERED, PAID as successful order statuses
            return orderJoin.get("status").in(OrderStatus.DELIVERED, OrderStatus.PAID);
        };
    }
    
    public static Specification<Product> isNewArrival() {
        return (root, query, cb) -> {
            // Products added in the last 30 days
            Date thirtyDaysAgo = new Date(System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000);
            return cb.greaterThanOrEqualTo(root.get("createdTime"), thirtyDaysAgo);
        };
    }

    public static Specification<Product> orderDateBetween(Date startDate, Date endDate) {
        return (root, query, cb) -> {
            Join<Product, OrderDetail> orderDetailJoin = join(root, "orderDetails", JoinType.INNER);
            Join<OrderDetail, Order> orderJoin = join(orderDetailJoin, "order", JoinType.INNER);
            
            return cb.between(orderJoin.get("orderTime"), startDate, endDate);
        };
    }

    public static Specification<Product> inCategoryTree(List<Integer> categoryIds) {
        return (root, query, cb) -> {
            if (categoryIds == null || categoryIds.isEmpty()) {
                return cb.conjunction();
            }

            // Ensure distinct results to avoid duplicates
            query.distinct(true);
            
            // For a 3-4 level hierarchy, using a subquery with explicit level JOIN is more efficient
            // than using FIND_IN_SET in most databases
            Subquery<Integer> categorySubquery = query.subquery(Integer.class);
            Root<Category> categoryRoot = categorySubquery.from(Category.class);
            
            // Get all categories at level 1 (direct match)
            Predicate level1 = categoryRoot.get(Category_.id).in(categoryIds);
            
            // Get all categories at level 2 (children)
            Join<Category, Category> childrenJoin = categoryRoot.join(Category_.parent, JoinType.LEFT);
            Predicate level2 = childrenJoin.get(Category_.id).in(categoryIds);
            
            // Get all categories at level 3 (grandchildren)
            Join<Category, Category> grandchildrenJoin = childrenJoin.join(Category_.parent, JoinType.LEFT);
            Predicate level3 = grandchildrenJoin.get(Category_.id).in(categoryIds);
            
            // Get all categories at level 4 (great-grandchildren)
            Join<Category, Category> greatGrandchildrenJoin = grandchildrenJoin.join(Category_.parent, JoinType.LEFT);
            Predicate level4 = greatGrandchildrenJoin.get(Category_.id).in(categoryIds);
            
            // Combine the predicates with OR
            categorySubquery.select(categoryRoot.get(Category_.id))
                .where(cb.or(level1, level2, level3, level4));
            
            // Join with products
            Join<Product, Category> categoryJoin = root.join(Product_.category);
            
            // Return products whose category is in the subquery result
            return categoryJoin.get(Category_.id).in(categorySubquery);
        };
    }

    public static Specification<Product> byPromotionId(Integer promotionId) {
        return (root, query, cb) -> {
            if (promotionId == null) {
                return cb.conjunction();
            }

            Join<Product, PromotionProduct> promotionProductJoin = join(root, Product_.PROMOTION_PRODUCTS, JoinType.INNER);
            return cb.equal(promotionProductJoin.get(PromotionProduct_.PROMOTION).get(Promotion_.ID), promotionId);
        };
    }
}
