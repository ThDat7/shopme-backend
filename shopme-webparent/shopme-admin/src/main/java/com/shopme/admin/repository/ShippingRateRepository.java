package com.shopme.admin.repository;

import com.shopme.common.entity.ShippingRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ShippingRateRepository extends JpaRepository<ShippingRate, Integer> {
}
