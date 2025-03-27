package com.shopme.client.service.impl;

import com.shopme.client.dto.request.CustomerUpdateProfileRequest;
import com.shopme.client.dto.request.CustomerRegistrationRequest;
import com.shopme.client.dto.request.CustomerResendVerifyEmailRequest;
import com.shopme.client.dto.request.CustomerVerifyEmailRequest;
import com.shopme.client.dto.response.AuthenticationResponse;
import com.shopme.client.dto.response.CustomerInfoResponse;
import com.shopme.client.dto.response.CustomerVerifyEmailResponse;
import com.shopme.client.mapper.AuthenticationMapper;
import com.shopme.client.mapper.CustomerMapper;
import com.shopme.client.repository.CountryRepository;
import com.shopme.client.repository.CustomerRepository;
import com.shopme.client.service.*;
import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.CustomerStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerContextService customerContextService;
    private final JwtTokenService jwtTokenService;
    private final EmailService emailService;
    private final AddressService addressService;
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;
    private final CountryRepository countryRepository;
    private final CustomerMapper customerMapper;
    private final AuthenticationMapper authenticationMapper;

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
                            .status(CustomerStatus.NEED_INFO)
                            .build();
                    setCustomerName(newCustomer, name);
                    return newCustomer;
                });

        customer.setPassword("");
        customer.setAuthenticationType(AuthenticationType.GOOGLE);
        customerRepository.save(customer);
        return customer;
    }

    @Override
    public AuthenticationResponse register(CustomerRegistrationRequest request) {
        boolean isEmailExist = customerRepository.existsByEmail(request.getEmail());
        if (isEmailExist)
            throw new RuntimeException("Email already exists");

        Customer customer = customerMapper.toCustomer(request);
//                mock postal code, will remove that field soon
        customer.setPostalCode("");
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setEnabled(true);
        customer.setCreatedTime(new Date());
        customer.setAuthenticationType(AuthenticationType.DATABASE);
        customer.setStatus(CustomerStatus.UNVERIFIED);

        Country country = countryRepository.findById(request.getCountryId())
                .orElseThrow(() -> new RuntimeException("Country not found"));
        customer.setCountry(country);

        String randomUUID = UUID.randomUUID().toString();
        customer.setVerificationCode(randomUUID);
        customerRepository.save(customer);
        addressService.createDefaultAddress(customer);

        emailService.sendVerificationEmail(customer.getEmail(), randomUUID);
        String token = jwtTokenService.generateToken(customer);
        return authenticationMapper.toAuthenticationResponse(token, customer);
    }

    @Override
    public CustomerVerifyEmailResponse verify(CustomerVerifyEmailRequest request) {
        Customer currentCustomer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        boolean isMatch = currentCustomer.getVerificationCode().equals(request.getVerificationCode());
        if (!isMatch)
            throw new RuntimeException("Verification code is not valid");

        currentCustomer.setStatus(CustomerStatus.VERIFIED);
        currentCustomer.setVerificationCode(null);
        customerRepository.save(currentCustomer);

        return new CustomerVerifyEmailResponse(true);
    }

    @Override
    public void resendVerification(CustomerResendVerifyEmailRequest request) {
        Customer currentCustomer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (currentCustomer.getStatus() == CustomerStatus.VERIFIED)
            throw new RuntimeException("Customer already verified");

        currentCustomer.setVerificationCode(UUID.randomUUID().toString());
        emailService.sendVerificationEmail(currentCustomer.getEmail(), currentCustomer.getVerificationCode());
        customerRepository.save(currentCustomer);
    }

    //    MOCK DATA NAME
    private void setCustomerName(Customer customer, String name) {
        String[] nameArray = name.split(" ");
        if (nameArray.length > 0) customer.setFirstName(nameArray[0]);
        if (nameArray.length > 1) customer.setLastName(nameArray[1]);
    }
}
