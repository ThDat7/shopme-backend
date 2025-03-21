package com.shopme.client.service.impl;

import com.shopme.client.dto.request.AbstractPlaceOrderRequest;
import com.shopme.client.dto.request.PlaceOrderCODRequest;
import com.shopme.client.dto.request.PlaceOrderPayOSRequest;
import com.shopme.client.dto.response.PayOSACKResponse;
import com.shopme.client.dto.response.PlaceOrderPayOSResponse;
import com.shopme.client.dto.response.PaymentMethodResponse;
import com.shopme.client.mapper.CheckoutMapper;
import com.shopme.client.repository.AddressRepository;
import com.shopme.client.repository.CartItemRepository;
import com.shopme.client.repository.OrderRepository;
import com.shopme.client.service.AuthenticationService;
import com.shopme.client.service.CheckoutService;
import com.shopme.client.service.PaymentService;
import com.shopme.client.service.ShippingService;
import com.shopme.common.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.Webhook;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private final ShippingService shippingService;
    private final PaymentService paymentService;

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final CartItemRepository cartItemRepository;
    private final AuthenticationService authenticationService;

    private final CheckoutMapper checkoutMapper;

    @Override
    public List<PaymentMethodResponse> getPaymentMethods() {
        return Arrays.stream(PaymentMethod.values())
                .map(checkoutMapper::toPaymentMethodResponse)
                .toList();
    }

    private List<CartItem> getCartItems(List<Integer> cartItemIds) {
        Integer currentCustomerId = authenticationService.getCurrentCustomerId();
        return cartItemRepository.findAllByCustomerIdAndProductIdIn(currentCustomerId, cartItemIds);
    }

    private Address getAddress(Integer addressId) {
        Integer currentCustomerId = authenticationService.getCurrentCustomerId();
        return addressRepository.findByIdAndCustomerId(addressId, currentCustomerId)
                .orElseThrow(() -> new IllegalArgumentException("Address not found"));
    }

    private Set<OrderDetail> generateOrderDetails(List<CartItem> cartItems, Address address) {
        Set<OrderDetail> orderDetails = new HashSet<>();
        ShippingRate shippingRate = shippingService.getShippingRateByAddressId(address.getId());

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();
            float productCost = product.getCost();
            float shippingCost = shippingService.calculateShippingCost(cartItem, shippingRate);
            float unitPrice = product.getPrice() * (1 - product.getDiscountPercent() / 100);
            float subtotal = unitPrice * quantity;
            orderDetails.add(OrderDetail.builder()
                    .product(product)
                    .quantity(quantity)
                    .productCost(productCost)
                    .shippingCost(shippingCost)
                    .unitPrice(unitPrice)
                    .subtotal(subtotal)
                    .build());
        }
        return orderDetails;
    }

    private void setOrderAddress(Order order, Address address) {
        order.setFirstName(address.getFirstName());
        order.setLastName(address.getLastName());
        order.setPhoneNumber(address.getPhoneNumber());
        order.setAddressLine1(address.getAddressLine1());
        order.setAddressLine2(address.getAddressLine2());
        order.setCity(address.getCity());
        order.setState(address.getState());
        order.setPostalCode(address.getPostalCode());
        order.setCountry(address.getCountry().getName());
    }

    private void setOrderShipping(Order order, Address address) {
        ShippingRate shippingRate = shippingService.getShippingRateByAddressId(address.getId());

        order.setShippingCost(shippingRate.getRate());
        order.setDeliverDays(shippingRate.getDays());
        order.setDeliverDays(shippingRate.getDays());
    }

    private void setOrderCost(Order order) {
        float productCost = 0;
        float shippingCost = 0;
        float subtotal = 0;
        float tax = 0;
        ;

        for (OrderDetail orderDetail : order.getOrderDetails()) {
            productCost += orderDetail.getProductCost();
            shippingCost += orderDetail.getShippingCost();
            subtotal += orderDetail.getSubtotal();
        }

        float total = subtotal + shippingCost + tax;

        order.setProductCost(productCost);
        order.setShippingCost(shippingCost);
        order.setSubtotal(subtotal);
        order.setTax(tax);
        order.setTotal(total);
    }

    private Order generateOrder(AbstractPlaceOrderRequest request) {
        List<CartItem> cartItems = getCartItems(request.getCartItemIds());
        Address address = getAddress(request.getAddressId());
        Set<OrderDetail> orderDetails = generateOrderDetails(cartItems, address);
        Customer currentCustomer = authenticationService.getCurrentCustomer();

        Order order = Order.builder()
                .orderTime(new Date())
                .customer(currentCustomer)
                .orderDetails(orderDetails)
                .build();
        setOrderAddress(order, address);
        setOrderShipping(order, address);
        setOrderCost(order);

        return order;
    }

    private Order placeOrder(AbstractPlaceOrderRequest request, PaymentMethod paymentMethod) {
        Order order = generateOrder(request);
        order.setPaymentMethod(paymentMethod);
        OrderStatus initialStatus;

        if (paymentMethod == PaymentMethod.COD)
            initialStatus = OrderStatus.NEW;
        else
            initialStatus = OrderStatus.PENDING_PAYMENT;

        order.setStatus(initialStatus);
        OrderTrack initialTrack = OrderTrack.builder()
                .order(order)
                .status(initialStatus)
                .updatedTime(new Date())
                .notes(initialStatus.getDescription())
                .build();
        order.setOrderTracks(Collections.singletonList(initialTrack));

        orderRepository.save(order);
        return order;
    }

    @Transactional
    @Override
    public PlaceOrderPayOSResponse placeOrderPayOS(PlaceOrderPayOSRequest request) {
        Order order = placeOrder(request, PaymentMethod.PAY_OS);
        CheckoutResponseData data = paymentService.generatePayOSResponse(order, request.getReturnUrl(), request.getCancelUrl());
        return checkoutMapper.toPlaceOrderPayOSResponse(data);
    }

    @Transactional
    @Override
    public void placeOrderCOD(PlaceOrderCODRequest request) {
        placeOrder(request, PaymentMethod.COD);
    }

    private void onPaymentSuccess(Long orderId, Integer amount) {
        Integer currentCustomerId = authenticationService.getCurrentCustomerId();
        Order order = orderRepository.findByIdAndCustomerId(orderId.intValue(), currentCustomerId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        boolean isMatchAmount = Math.round(order.getTotal()) == amount;
        if (!isMatchAmount)
            throw new IllegalArgumentException("Amount not match");

        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);
    }

    @Override
    public PayOSACKResponse payosTransferHandler(Webhook webhookBody) {
        return paymentService.handlePayOSWebhook(webhookBody, this::onPaymentSuccess);
    }


}
