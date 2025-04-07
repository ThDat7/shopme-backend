package com.shopme.client.repository;

import com.shopme.common.entity.PromotionProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionProductRepository extends JpaRepository<PromotionProduct, Integer> {
    @Query("SELECT pp FROM PromotionProduct pp " +
            "JOIN pp.promotion p " +
            "WHERE p.active = true AND p.startDate <= CURRENT_TIMESTAMP " +
            "AND p.endDate >= CURRENT_TIMESTAMP AND pp.product.id = :productId " +
            "AND (pp.stockLimit IS NULL OR pp.soldCount < pp.stockLimit)")
    List<PromotionProduct> findAllByProductIdAndPromotionActive(@Param("productId") Integer productId);
}
