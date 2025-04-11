package com.shopme.client.repository;

import com.shopme.client.repository.projection.ProductDetailProjection;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM Product p " +
            "WHERE (:keyword IS NULL OR p.name LIKE %:keyword%)" +
            "AND (:categoryId IS NULL OR p.category.id = :categoryId)")
    Page<Product> findAll(@Param("keyword") String keyword, @Param("categoryId") Integer categoryId, Pageable pageable);

    @Query("SELECT p.id, p.name, p.price, p.discountPercent, p.mainImage, " +
            "COALESCE(AVG(r.rating), 0) AS averageRating, " +
            "COUNT(DISTINCT r.id) AS reviewCount, " +
            "COUNT(DISTINCT od.id) AS saleCount FROM Product p " +
            "LEFT JOIN OrderDetail od ON od.product.id = p.id " +
            "LEFT JOIN Review r ON r.orderDetail.id = od.id " +
            "WHERE p.name LIKE :keyword OR p.description LIKE :keyword GROUP BY p.id")
    Page<Object[]> search(String keyword, Pageable pageable);

    @Query("SELECT p.id, p.name, p.price, p.discountPercent, p.mainImage, " +
            "COALESCE(AVG(r.rating), 0) AS averageRating, " +
            "COUNT(DISTINCT r.id) AS reviewCount, " +
            "COUNT(DISTINCT od.id) AS saleCount " +
            "FROM Product p " +
            "LEFT JOIN OrderDetail od ON od.product.id = p.id " +
            "LEFT JOIN Review r ON r.orderDetail.id = od.id " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(od.id) DESC")
    Page<Object[]> getBestSelling(Pageable pageable);

    @Query("SELECT p.id, p.name, p.price, p.discountPercent, p.mainImage, " +
            "COALESCE(AVG(r.rating), 0) AS averageRating, " +
            "COUNT(DISTINCT r.id) AS reviewCount, " +
            "COUNT(DISTINCT od.id) AS saleCount " +
            "FROM Product p " +
            "LEFT JOIN OrderDetail od ON od.product.id = p.id " +
            "LEFT JOIN Review r ON r.orderDetail.id = od.id " +
            "JOIN Order o ON od.order.id = o.id " +
            "WHERE o.orderTime BETWEEN :start AND :end " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(od.id) DESC")
    Page<Object[]> getTrending(Pageable pageable, @Param("start") Date start, @Param("end") Date end);

    @Query("SELECT p.id, p.name, p.price, p.discountPercent, p.mainImage, " +
            "COALESCE(AVG(r.rating), 0) AS averageRating, " +
            "COUNT(DISTINCT r.id) AS reviewCount, " +
            "COUNT(DISTINCT od.id) AS saleCount " +
            "FROM Product p " +
            "LEFT JOIN OrderDetail od ON od.product.id = p.id " +
            "LEFT JOIN Review r ON r.orderDetail.id = od.id " +
            "GROUP BY p.id " +
            "ORDER BY COALESCE(AVG(r.rating), 0) DESC")
    Page<Object[]> getTopRated(Pageable pageable);

    @Query("SELECT p.id, p.name, p.price, p.discountPercent, p.mainImage, " +
            "COALESCE(AVG(r.rating), 0) AS averageRating, " +
            "COUNT(DISTINCT r.id) AS reviewCount, " +
            "COUNT(DISTINCT od.id) AS saleCount " +
            "FROM Product p " +
            "LEFT JOIN OrderDetail od ON od.product.id = p.id " +
            "LEFT JOIN Review r ON r.orderDetail.id = od.id " +
            "WHERE p.discountPercent > 0 " +
            "GROUP BY p.id " +
            "ORDER BY p.discountPercent DESC")
    Page<Object[]> getTopDiscounted(Pageable pageable);

    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN PromotionProduct pp ON p.id = pp.product.id " +
            "JOIN pp.promotion prom " +
            "WHERE prom = :promotion")
    Page<Product> findByPromotion(@Param("promotion") Promotion promotion, Pageable pageable);

    @Query("SELECT p.id AS id, p.name AS name, p.alias AS alias, p.description AS description, p.inStock AS inStock, " +
            "p.price AS price, p.discountPercent AS discountPercent, " +
            "p.length AS length, p.width AS width, p.height AS height, p.weight AS weight, " +
            "p.mainImage AS mainImage, " +
            "p.category.id AS categoryId, p.category.name AS category, p.brand.name AS brand, " +
            "COALESCE(AVG(r.rating), 0) AS averageRating, " +
            "COUNT(DISTINCT r.id) AS reviewCount, " +
            "COUNT(DISTINCT od.id) AS saleCount " +
            "FROM Product p " +
            "LEFT JOIN OrderDetail od ON od.product.id = p.id " +
            "LEFT JOIN Review r ON r.orderDetail.id = od.id " +
            "WHERE p.id = :id " +
            "GROUP BY p.id")
    Optional<ProductDetailProjection> getProductDetail(@Param("id") Integer id);
}