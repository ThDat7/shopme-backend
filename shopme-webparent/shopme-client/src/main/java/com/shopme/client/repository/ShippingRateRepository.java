package com.shopme.client.repository;

import com.shopme.common.entity.District;
import com.shopme.common.entity.ShippingRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShippingRateRepository extends JpaRepository<ShippingRate, Integer> {
    Optional<ShippingRate> findByDistrict(District district);
}
