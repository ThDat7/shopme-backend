package com.shopme.client.service.impl;

import com.shopme.client.dto.request.CalculateShippingRequest;
import com.shopme.client.dto.response.CalculateShippingResponse;
import com.shopme.client.exception.type.AddressNotFoundException;
import com.shopme.client.exception.type.AddressNotSupportShippingException;
import com.shopme.client.mapper.ShippingMapper;
import com.shopme.client.repository.AddressRepository;
import com.shopme.client.repository.CartItemRepository;
import com.shopme.client.repository.ShippingRateRepository;
import com.shopme.client.service.AuthenticationService;
import com.shopme.client.service.CustomerContextService;
import com.shopme.client.service.ShippingService;
import com.shopme.common.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShippingServiceImpl implements ShippingService {
    // mock currently
    private static final int DIM_DIVISOR = 139;

    private final CustomerContextService customerContextService;

    private final ShippingRateRepository shippingRateRepository;
    private final AddressRepository addressRepository;
    private final CartItemRepository cartItemRepository;

    private final ShippingMapper shippingMapper;

    public ShippingRate getShippingRateByAddressId(Integer addressId) {
        Integer currentCustomerId = customerContextService.getCurrentCustomerId();
        Address address = addressRepository.findByIdAndCustomerId(addressId, currentCustomerId)
                .orElseThrow(AddressNotFoundException::new);

        District district = address.getWard().getDistrict();

        return shippingRateRepository.findByDistrict(district)
                .orElseThrow(() -> new IllegalArgumentException("Your address is not supported"));
    }

    @Override
    public CalculateShippingResponse calculateShipping(CalculateShippingRequest request) {
        ShippingRate shippingRate = getShippingRateByAddressId(request.getAddressId());

        Integer currentCustomerId = customerContextService.getCurrentCustomerId();
        List<CartItem> cartItems = cartItemRepository
                .findAllByCustomerIdAndProductIdIn(currentCustomerId, request.getCartItemIds());

        int shippingCostTotal = 0;

        for (CartItem item : cartItems) {
            int shippingCost = calculateShippingCost(item, shippingRate);

            shippingCostTotal += shippingCost;
        }

        return shippingMapper.toCalculateShippingResponse(shippingCostTotal);
    }

    public int calculateShippingCost(CartItem cartItem, ShippingRate shippingRate) {
        Product product = cartItem.getProduct();
        float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISOR;
        float finalWeight = Math.max(product.getWeight(), dimWeight);
        return Math.round(finalWeight * cartItem.getQuantity() * shippingRate.getRate());
    }
}
