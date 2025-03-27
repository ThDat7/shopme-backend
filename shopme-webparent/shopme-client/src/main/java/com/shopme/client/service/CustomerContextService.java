package com.shopme.client.service;

import com.shopme.common.entity.Customer;

public interface CustomerContextService {
    Customer getCurrentCustomer();
    Integer getCurrentCustomerId();
}
