package com.shopme.client.repository;

import com.shopme.common.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Query("SELECT r FROM Review r WHERE r.orderDetail.product.id = :productId " +
            "AND (:rating IS NULL OR r.rating = :rating)")
    Page<Review> findAllByProductIdAndRating(@Param("productId") Integer productId,
                                    @Param("rating") Integer rating,
                                    Pageable pageable);
}
