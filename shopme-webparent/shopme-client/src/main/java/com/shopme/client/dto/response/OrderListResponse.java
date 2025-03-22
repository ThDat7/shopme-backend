package com.shopme.client.dto.response;

import com.shopme.common.entity.OrderStatus;
import com.shopme.common.entity.PaymentMethod;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class OrderListResponse {
    private Integer id;
    private OrderStatus status;
    private float totalPrice;
    private Date createdAt;
    private PaymentMethod paymentMethod;
}
