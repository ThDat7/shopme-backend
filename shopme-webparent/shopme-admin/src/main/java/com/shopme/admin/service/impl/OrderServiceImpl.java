package com.shopme.admin.service.impl;

import com.shopme.admin.dto.response.OrderListResponse;
import com.shopme.admin.dto.response.ListResponse;
import com.shopme.admin.dto.response.OrderDetailResponse;
import com.shopme.admin.mapper.OrderMapper;
import com.shopme.admin.repository.OrderRepository;
import com.shopme.admin.service.OrderService;
import com.shopme.common.entity.Order;
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
    private static final String DEFAULT_SORT_FIELD = "name";
    private static final String DEFAULT_SORT_DIRECTION = "asc";
    private static final int DEFAULT_BRANDS_PER_PAGE = 4;

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    private Pageable getPageableFromParams(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "0"));
        int size = Integer.parseInt(params.getOrDefault("size", String.valueOf(DEFAULT_BRANDS_PER_PAGE)));
        String sortField = params.getOrDefault("sortField", DEFAULT_SORT_FIELD);
        String sortDirection = params.getOrDefault("sortDirection", DEFAULT_SORT_DIRECTION);

        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        return PageRequest.of(page, size, sort);
    }

    @Override
    public ListResponse<OrderListResponse> listByPage(Map<String, String> params) {
        Pageable pageable = getPageableFromParams(params);
        String keyword = params.getOrDefault("keyword", "");

        Page<Order> orderPage = orderRepository.findAll(keyword, pageable);
        List<OrderListResponse> orderListResponses = orderPage.getContent().stream()
                .map(orderMapper::toOrderListResponse)
                .collect(Collectors.toList());

        return ListResponse.<OrderListResponse>builder()
                .content(orderListResponses)
                .totalPages(orderPage.getTotalPages())
                .build();
    }

    @Override
    public OrderDetailResponse getOrderById(Integer id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        return orderMapper.toOrderDetailResponse(order);
    }
}
