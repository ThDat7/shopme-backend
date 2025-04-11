package com.shopme.client.service.impl;

import com.shopme.client.dto.request.AddressRequest;
import com.shopme.client.dto.response.AddressDetailResponse;
import com.shopme.client.dto.response.AddressResponse;
import com.shopme.client.exception.type.CountryNotFoundException;
import com.shopme.client.mapper.AddressMapper;
import com.shopme.client.repository.AddressRepository;
import com.shopme.client.repository.WardRepository;
import com.shopme.client.service.AddressService;
import com.shopme.client.service.CustomerContextService;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Ward;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.shopme.client.exception.type.AddressNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final WardRepository wardRepository;
    private final AddressMapper addressMapper;
    private final CustomerContextService customerContextService;

    @Override
    public void deleteAddress(Integer id) {
        Integer currentCustomerId = customerContextService.getCurrentCustomerId();
        Address address = addressRepository.findByIdAndCustomerId(id, currentCustomerId).orElseThrow(
                AddressNotFoundException::new
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
                AddressNotFoundException::new
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
                AddressNotFoundException::new
        );

        Ward ward = wardRepository.findById(addressRequest.getWardId()).orElseThrow(
                CountryNotFoundException::new
        );

        address.setWard(ward);

        address.setFirstName(addressRequest.getFirstName());
        address.setLastName(addressRequest.getLastName());
        address.setPhoneNumber(addressRequest.getPhoneNumber());
        address.setAddressLine(addressRequest.getAddressLine());
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

        Ward ward = wardRepository.findById(addressRequest.getWardId()).orElseThrow(
                CountryNotFoundException::new
        );
        address.setWard(ward);

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
                AddressNotFoundException::new
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
}
