package com.shopme.client.service.impl;

import com.shopme.client.repository.CustomerRepository;
import com.shopme.client.service.CustomerService;
import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public Customer customerFromGoogleLogin(String email, String name) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseGet(() -> {
                    Customer newCustomer = Customer.builder()
                            .email(email)
                            .enabled(true)
                            .createdTime(new Date())
//                                empty data temporary, will handle later
                            .addressLine1("")
                            .addressLine2("")
                            .city("")
                            .state("")
                            .postalCode("")
                            .phoneNumber("")
                            .build();
                    setCustomerName(newCustomer, name);
                    return newCustomer;
                });

        customer.setPassword("");
        customer.setAuthenticationType(AuthenticationType.GOOGLE);
        customerRepository.save(customer);
        return customer;
    }


//    MOCK DATA NAME
    private void setCustomerName(Customer customer, String name) {
        String[] nameArray = name.split(" ");
        if (nameArray.length > 0) customer.setFirstName(nameArray[0]);
        if (nameArray.length > 1) customer.setLastName(nameArray[1]);
    }
}
