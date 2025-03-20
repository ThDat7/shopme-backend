package com.shopme.client.repository;

import com.shopme.common.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    List<Address> findAllByCustomerId(Integer customerId);
    Optional<Address> findByIdAndCustomerId(Integer id, Integer customerId);

    Address findByDefaultForShippingTrueAndCustomerId(Integer customerId);

    @Query("UPDATE Address a SET a.defaultForShipping = false WHERE a.customer.id = ?1")
    @Modifying
    void setNoneDefaultAllAddressByCustomerId(Integer customerId);
}
