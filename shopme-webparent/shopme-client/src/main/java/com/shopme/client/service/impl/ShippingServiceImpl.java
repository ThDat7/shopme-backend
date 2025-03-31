package com.shopme.client.service.impl;

import com.shopme.client.dto.request.CalculateShippingRequest;
import com.shopme.client.dto.response.CalculateShippingResponse;
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
                .orElseThrow(() -> new IllegalArgumentException("Address not found"));

        Country country = address.getCountry();
        String state = address.getState();

        return shippingRateRepository.findByCountryAndState(country, state)
                .orElseThrow(() -> new IllegalArgumentException("Your address is not supported"));
    }

    @Override
    public CalculateShippingResponse calculateShipping(CalculateShippingRequest request) {
        ShippingRate shippingRate = getShippingRateByAddressId(request.getAddressId());

        Integer currentCustomerId = customerContextService.getCurrentCustomerId();
        List<CartItem> cartItems = cartItemRepository
                .findAllByCustomerIdAndProductIdIn(currentCustomerId, request.getCartItemIds());

        float shippingCostTotal = 0.0f;

        for (CartItem item : cartItems) {
            float shippingCost = calculateShippingCost(item, shippingRate);

            shippingCostTotal += shippingCost;
        }

        return shippingMapper.toCalculateShippingResponse(shippingCostTotal);
    }

    public float calculateShippingCost(CartItem cartItem, ShippingRate shippingRate) {
        Product product = cartItem.getProduct();
        float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISOR;
        float finalWeight = Math.max(product.getWeight(), dimWeight);
        return finalWeight * cartItem.getQuantity() * shippingRate.getRate();
    }
}
