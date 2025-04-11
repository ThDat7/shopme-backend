package com.shopme.client.repository;

import com.shopme.common.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
    @Query("SELECT b.id, b.name, b.logo, COUNT(p.id) FROM Brand b " +
            "LEFT JOIN Product p ON b.id = p.brand.id " +
            "GROUP BY b.id, b.name, b.logo")
    Page<Object[]> findAllWithProductCount(Pageable pageable);
}
