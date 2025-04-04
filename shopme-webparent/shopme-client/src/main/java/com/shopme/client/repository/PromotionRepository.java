package com.shopme.client.repository;

import com.shopme.common.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
    @Query("SELECT p FROM Promotion p WHERE p.active = true AND p.startDate <= CURRENT_TIMESTAMP " +
            "AND p.endDate >= CURRENT_TIMESTAMP")
    List<Promotion> findActivePromotions();

    @Query("SELECT p FROM Promotion p WHERE p.active = true AND p.startDate <= CURRENT_TIMESTAMP " +
            "AND p.endDate >= CURRENT_TIMESTAMP AND p.id = :id")
    Optional<Promotion> findActivePromotionById(@Param("id") Integer id);
}
