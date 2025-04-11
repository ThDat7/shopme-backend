package com.shopme.client.repository;

import com.shopme.common.entity.FeatureCategoryBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureCategoryBrandRepository extends JpaRepository<FeatureCategoryBrand, Integer> {
}
