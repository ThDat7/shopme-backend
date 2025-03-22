package com.shopme.client.service.impl;

import com.shopme.client.repository.OrderRepository;
import com.shopme.client.service.AuthenticationService;
import com.shopme.client.service.OrderService;
import com.shopme.client.service.PaymentService;
import com.shopme.common.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final PaymentService paymentService;
    private final OrderRepository orderRepository;
    private final AuthenticationService authenticationService;

    private void updateOrderStatusStatic(Order order, OrderStatus newStatus) {
        order.setStatus(newStatus);
        order.getOrderTracks().add(OrderTrack.builder()
                .order(order)
                .status(newStatus)
                .build());
    }

    @Override
    public void cancelOrder(Integer orderId) {
        Integer currentCustomerId = authenticationService.getCurrentCustomerId();
        Order order = orderRepository.findByIdAndCustomerId(orderId, currentCustomerId).orElseThrow(
                () -> new IllegalArgumentException("Order not found with Id: " + orderId)
        );
        if (order.getStatus() == OrderStatus.NEW)
            updateOrderStatusStatic(order, OrderStatus.CANCELLED);
        else if (order.getStatus() == OrderStatus.PENDING_PAYMENT &&
                order.getPaymentMethod() == PaymentMethod.PAY_OS)
            handleCancelPayOSOrder(order);
        else
            throw new IllegalArgumentException("Order can not be cancelled because it is in " + order.getStatus() + " status");


        orderRepository.save(order);
    }

    private void handleCancelPayOSOrder(Order order) {
        paymentService.cancelPayOSPayment(order.getId().longValue());
        updateOrderStatusStatic(order, OrderStatus.CANCELLED_PAYMENT);
    }

    @Override
    public OrderStatus getOrderStatus(Integer orderId) {
        Integer currentCustomerId = authenticationService.getCurrentCustomerId();
        Order order = orderRepository.findByIdAndCustomerId(orderId, currentCustomerId).orElseThrow(
                () -> new IllegalArgumentException("Order not found with Id: " + orderId)
        );
        return order.getStatus();
    }
}
