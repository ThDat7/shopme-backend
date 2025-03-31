package com.shopme.client.service.impl;

import com.shopme.client.dto.response.ListResponse;
import com.shopme.client.dto.response.OrderDetailResponse;
import com.shopme.client.dto.response.OrderListResponse;
import com.shopme.client.mapper.OrderMapper;
import com.shopme.client.repository.OrderRepository;
import com.shopme.client.service.*;
import com.shopme.common.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private static final String DEFAULT_SORT_FIELD = "orderTime";
    private static final String DEFAULT_SORT_DIRECTION = "desc";
    private static final int DEFAULT_ORDERS_PER_PAGE = 4;

    private final PaymentService paymentService;
    private final FileUploadService fileUploadService;
    private final OrderRepository orderRepository;
    private final CustomerContextService customerContextService;
    private final OrderMapper orderMapper;

    private void updateOrderStatusStatic(Order order, OrderStatus newStatus) {
        order.setStatus(newStatus);
        order.getOrderTracks().add(OrderTrack.builder()
                .order(order)
                .status(newStatus)
                .build());
    }

    private String getProductMainImageURL(Product product) {
        return fileUploadService.getProductMainImageUrl(product.getId(), product.getMainImage());
    }

    private Pageable getPageableFromParams(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "0"));
        int size = Integer.parseInt(params.getOrDefault("size", String.valueOf(DEFAULT_ORDERS_PER_PAGE)));
        String sortField = params.getOrDefault("sortField", DEFAULT_SORT_FIELD);
        String sortDirection = params.getOrDefault("sortDirection", DEFAULT_SORT_DIRECTION);

        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        return PageRequest.of(page, size, sort);
    }

    @Override
    public void cancelOrder(Integer orderId) {
        Integer currentCustomerId = customerContextService.getCurrentCustomerId();
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
        Integer currentCustomerId = customerContextService.getCurrentCustomerId();
        Order order = orderRepository.findByIdAndCustomerId(orderId, currentCustomerId).orElseThrow(
                () -> new IllegalArgumentException("Order not found with Id: " + orderId)
        );
        return order.getStatus();
    }

    @Override
    public ListResponse<OrderListResponse> getOrders(Map<String, String> params) {
        Integer currentCustomerId = customerContextService.getCurrentCustomerId();
        Pageable pageable = getPageableFromParams(params);
        OrderStatus status =  params.containsKey("status") ? OrderStatus.valueOf(params.get("status")) : null;

        Page<Order> orderPage = orderRepository.findAllByCustomerIdAndStatus(currentCustomerId, status, pageable);
        List<OrderListResponse> orderListResponses = orderPage.getContent().stream()
                .map(orderMapper::toOrderListResponse)
                .collect(Collectors.toList());

        return ListResponse.<OrderListResponse>builder()
                .content(orderListResponses)
                .totalPages(orderPage.getTotalPages())
                .build();
    }

    @Override
    public OrderDetailResponse getOrder(Integer orderId) {
        Integer currentCustomerId = customerContextService.getCurrentCustomerId();
        Order order = orderRepository.findByIdAndCustomerId(orderId, currentCustomerId).orElseThrow(
                () -> new IllegalArgumentException("Order not found with Id: " + orderId)
        );
        OrderDetailResponse orderDetailResponse = orderMapper.toOrderDetailResponse(order);
        orderDetailResponse.getOrderItems().stream()
                .forEach(orderItem -> {
                    Product product = order.getOrderDetails().stream()
                            .filter(orderDetail -> orderDetail.getId().equals(orderItem.getId()))
                            .findFirst().get().getProduct();
                    orderItem.setProductMainImage(getProductMainImageURL(product));
                });
        return orderDetailResponse;
    }
}
