package com.shopme.client.service;

import com.shopme.common.entity.Customer;

public interface CustomerService {
    Customer customerFromGoogleLogin(String email, String name);
}
