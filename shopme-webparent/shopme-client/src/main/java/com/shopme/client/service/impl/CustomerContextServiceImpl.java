package com.shopme.client.service.impl;

import com.shopme.client.exception.type.CustomerNotFoundException;
import com.shopme.client.exception.type.UnAuthenticatedException;
import com.shopme.client.repository.CustomerRepository;
import com.shopme.client.service.CustomerContextService;
import com.shopme.common.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerContextServiceImpl implements CustomerContextService {
    private final CustomerRepository customerRepository;

    @Override
    public Integer getCurrentCustomerId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            Long customerId = jwt.getClaim("customerId");
            return customerId.intValue();
        } catch (RuntimeException e) {
            throw new UnAuthenticatedException();
        }
    }

    @Override
    public Customer getCurrentCustomer() {
        Integer customerId = getCurrentCustomerId();
        return customerRepository.findById(customerId).orElseThrow(
            CustomerNotFoundException::new
        );
    }
}
