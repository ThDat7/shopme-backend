package com.shopme.client.repository;

import com.shopme.common.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {
    List<ProductDetail> findAllByProductId(Integer productId);
}
