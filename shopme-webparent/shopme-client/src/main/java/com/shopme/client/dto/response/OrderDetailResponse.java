package com.shopme.client.dto.response;

import com.shopme.common.entity.OrderStatus;
import com.shopme.common.entity.PaymentMethod;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderDetailResponse {
    private Integer id;
    private OrderStatus status;
    private PaymentMethod paymentMethod;
    private Date createdAt;
    private float totalPrice;
    private ShippingAddressResponse shippingAddress;
    private List<OrderItemResponse> orderItems;
    private List<OrderTrackResponse> orderTracks;
}
