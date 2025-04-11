package com.shopme.client.repository;

import com.shopme.common.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    Set<ProductImage> findAllByProductId(Integer productId);
}
