package com.shopme.client.service.impl;

import com.shopme.client.dto.request.AddressRequest;
import com.shopme.client.dto.response.AddressDetailResponse;
import com.shopme.client.dto.response.AddressResponse;
import com.shopme.client.mapper.AddressMapper;
import com.shopme.client.repository.AddressRepository;
import com.shopme.client.repository.CountryRepository;
import com.shopme.client.service.AddressService;
import com.shopme.client.service.AuthenticationService;
import com.shopme.client.service.CustomerContextService;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final CountryRepository countryRepository;
    private final AddressMapper addressMapper;
    private final CustomerContextService customerContextService;

    @Override
    public void deleteAddress(Integer id) {
        Integer currentCustomerId = customerContextService.getCurrentCustomerId();
        Address address = addressRepository.findByIdAndCustomerId(id, currentCustomerId).orElseThrow(
                () -> new RuntimeException("Address not found with id: " + id)
        );
        addressRepository.delete(address);
    }

    @Override
    public AddressDetailResponse getDefaultAddress() {
        Integer currentCustomerId = customerContextService.getCurrentCustomerId();
        Address address = addressRepository.findByDefaultForShippingTrueAndCustomerId(currentCustomerId);
        return addressMapper.toAddressDetailResponse(address);
    }

    @Override
    @Transactional
    public void setDefaultAddress(Integer id) {
        Integer currentCustomerId = customerContextService.getCurrentCustomerId();
        Address address = addressRepository.findByIdAndCustomerId(id, currentCustomerId).orElseThrow(
                () -> new RuntimeException("Address not found with id: " + id)
        );
        addressRepository.setNoneDefaultAllAddressByCustomerId(currentCustomerId);
        address.setDefaultForShipping(true);
        addressRepository.save(address);
    }

    @Override
    @Transactional
    public AddressDetailResponse updateAddress(Integer id, AddressRequest addressRequest) {
        Integer currentCustomerId = customerContextService.getCurrentCustomerId();
        Address address = addressRepository.findByIdAndCustomerId(id, currentCustomerId).orElseThrow(
                () -> new RuntimeException("Address not found with id: " + id)
        );

        Country country = countryRepository.findById(addressRequest.getCountryId()).orElseThrow(
                () -> new RuntimeException("Country not found with id: " + addressRequest.getCountryId())
        );

        address.setCountry(country);

        address.setFirstName(addressRequest.getFirstName());
        address.setLastName(addressRequest.getLastName());
        address.setPhoneNumber(addressRequest.getPhoneNumber());
        address.setAddressLine1(addressRequest.getAddressLine1());
        address.setAddressLine2(addressRequest.getAddressLine2());
        address.setCity(addressRequest.getCity());
        address.setState(addressRequest.getState());
        address.setPostalCode(addressRequest.getPostalCode());
        if (addressRequest.isDefaultForShipping()) {
            addressRepository.setNoneDefaultAllAddressByCustomerId(currentCustomerId);
            address.setDefaultForShipping(true);
        }

        addressRepository.save(address);
        return addressMapper.toAddressDetailResponse(address);
    }

    @Override
    @Transactional
    public AddressDetailResponse createAddress(AddressRequest addressRequest) {
        Address address = addressMapper.toAddress(addressRequest);

        Customer currentCustomer = customerContextService.getCurrentCustomer();
        address.setCustomer(currentCustomer);

        Country country = countryRepository.findById(addressRequest.getCountryId()).orElseThrow(
                () -> new RuntimeException("Country not found with id: " + addressRequest.getCountryId())
        );
        address.setCountry(country);

        if (addressRequest.isDefaultForShipping()) {
            addressRepository.setNoneDefaultAllAddressByCustomerId(currentCustomer.getId());
        }

        addressRepository.save(address);
        return addressMapper.toAddressDetailResponse(address);
    }

    @Override
    public AddressDetailResponse getAddressDetail(Integer id) {
        Integer currentCustomerId = customerContextService.getCurrentCustomerId();
        Address address = addressRepository.findByIdAndCustomerId(id, currentCustomerId).orElseThrow(
                () -> new RuntimeException("Address not found with id: " + id)
        );
        return addressMapper.toAddressDetailResponse(address);
    }

    @Override
    public List<AddressResponse> getAll() {
        Integer currentCustomerId = customerContextService.getCurrentCustomerId();
        return addressRepository
                .findAllByCustomerId(currentCustomerId)
                .stream()
                .map(addressMapper::toAddressResponse)
                .toList();
    }

    @Override
    public void createDefaultAddress(Customer customer) {
        Address address = Address.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .phoneNumber(customer.getPhoneNumber())
                .addressLine1(customer.getAddressLine1())
                .addressLine2(customer.getAddressLine2())
                .city(customer.getCity())
                .state(customer.getState())
                .postalCode(customer.getPostalCode())
                .country(customer.getCountry())
                .customer(customer)
                .defaultForShipping(true)
                .build();
        addressRepository.save(address);
    }
}
