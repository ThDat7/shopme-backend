package com.shopme.client.repository;

import com.shopme.common.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    Optional<OrderDetail> findByIdAndOrderCustomerId(Integer id, Integer customerId);
}
