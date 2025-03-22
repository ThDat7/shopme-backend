package com.shopme.client.repository;

import com.shopme.common.entity.Order;
import com.shopme.common.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Optional<Order> findByIdAndCustomerId(Integer id, Integer customerId);

    @Query("SELECT o FROM Order o " +
            "WHERE (:status IS NULL OR o.status = :status)")
    Page<Order> findAll(@Param("status") OrderStatus status, Pageable pageable);
}
