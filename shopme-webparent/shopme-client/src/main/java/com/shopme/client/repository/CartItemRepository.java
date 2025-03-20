package com.shopme.client.repository;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findAllByCustomer(Customer customer);

    CartItem findByCustomerAndProduct_Id(Customer customer, Integer productId);
}
