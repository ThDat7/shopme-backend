package com.shopme.client.service.impl;

import com.shopme.client.dto.request.CustomerUpdateProfileRequest;
import com.shopme.client.dto.request.CustomerRegistrationRequest;
import com.shopme.client.dto.request.CustomerResendVerifyEmailRequest;
import com.shopme.client.dto.request.CustomerVerifyEmailRequest;
import com.shopme.client.dto.response.AuthenticationResponse;
import com.shopme.client.dto.response.CustomerInfoResponse;
import com.shopme.client.dto.response.CustomerVerifyEmailResponse;
import com.shopme.client.exception.type.CustomerAlreadyVerifiedException;
import com.shopme.client.exception.type.CustomerNotFoundException;
import com.shopme.client.exception.type.EmailExistsException;
import com.shopme.client.exception.type.VerificationCodeInvalidException;
import com.shopme.client.mapper.AuthenticationMapper;
import com.shopme.client.mapper.CustomerMapper;
import com.shopme.client.repository.CustomerRepository;
import com.shopme.client.service.*;
import com.shopme.common.entity.AuthenticationType;
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
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;
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
            throw new EmailExistsException();

        Customer customer = customerMapper.toCustomer(request);
//                mock postal code, will remove that field soon
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setEnabled(true);
        customer.setCreatedTime(new Date());
        customer.setAuthenticationType(AuthenticationType.DATABASE);
        customer.setStatus(CustomerStatus.UNVERIFIED);

        String randomUUID = UUID.randomUUID().toString();
        customer.setVerificationCode(randomUUID);
        customerRepository.save(customer);

        emailService.sendVerificationEmail(customer.getEmail(), randomUUID);
        String token = jwtTokenService.generateToken(customer);
        return authenticationMapper.toAuthenticationResponse(token, customer);
    }

    @Override
    public CustomerVerifyEmailResponse verify(CustomerVerifyEmailRequest request) {
        Customer currentCustomer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(CustomerNotFoundException::new);

        boolean isMatch = currentCustomer.getVerificationCode().equals(request.getVerificationCode());
        if (!isMatch)
            throw new VerificationCodeInvalidException();

        currentCustomer.setStatus(CustomerStatus.VERIFIED);
        currentCustomer.setVerificationCode(null);
        customerRepository.save(currentCustomer);

        return new CustomerVerifyEmailResponse(true);
    }

    @Override
    public void resendVerification(CustomerResendVerifyEmailRequest request) {
        Customer currentCustomer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(CustomerNotFoundException::new);

        if (currentCustomer.getStatus() == CustomerStatus.VERIFIED)
            throw new CustomerAlreadyVerifiedException();

        currentCustomer.setVerificationCode(UUID.randomUUID().toString());
        emailService.sendVerificationEmail(currentCustomer.getEmail(), currentCustomer.getVerificationCode());
        customerRepository.save(currentCustomer);
    }

    @Override
    public CustomerInfoResponse getProfile() {
        Customer customer = customerContextService.getCurrentCustomer();
        return customerMapper.toCustomerInfoResponse(customer);
    }

    private void updateEmailAndPassword(Customer customer, String email, String password) {
        if (customer.getAuthenticationType() != AuthenticationType.DATABASE)
            return;

        if (password != null && !password.isEmpty())
            customer.setPassword(passwordEncoder.encode(password));

        if (email != null && !email.isEmpty()) {
            customer.setEmail(email);
            customer.setStatus(CustomerStatus.UNVERIFIED);
            customer.setVerificationCode(UUID.randomUUID().toString());

        }
    }

    @Override
    public CustomerInfoResponse updateProfile(CustomerUpdateProfileRequest request) {
        Customer customer = customerContextService.getCurrentCustomer();

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setPhoneNumber(request.getPhoneNumber());

        if (customer.getStatus() == CustomerStatus.NEED_INFO)
            customer.setStatus(CustomerStatus.VERIFIED);

        updateEmailAndPassword(customer, request.getEmail(), request.getPassword());

        customerRepository.save(customer);
        return customerMapper.toCustomerInfoResponse(customer);

    }

    //    MOCK DATA NAME
    private void setCustomerName(Customer customer, String name) {
        String[] nameArray = name.split(" ");
        if (nameArray.length > 0) customer.setFirstName(nameArray[0]);
        if (nameArray.length > 1) customer.setLastName(nameArray[1]);
    }
}
