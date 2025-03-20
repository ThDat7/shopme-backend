package com.shopme.admin.repository;

import com.shopme.common.entity.ShippingRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ShippingRateRepository extends JpaRepository<ShippingRate, Integer> {
    @Query("SELECT sr FROM ShippingRate sr WHERE CONCAT(sr.country.name, ' ', sr.state) LIKE %?1%")
    Page<ShippingRate> findAll(String keyword, Pageable pageable);
}
