package com.shopme.client.repository;

import com.shopme.common.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM Product p " +
            "WHERE (:keyword IS NULL OR p.name LIKE %:keyword%)" +
            "AND (:categoryId IS NULL OR p.category.id = :categoryId)")
    Page<Product> findAll(@Param("keyword") String keyword, @Param("categoryId") Integer categoryId, Pageable pageable);
}
