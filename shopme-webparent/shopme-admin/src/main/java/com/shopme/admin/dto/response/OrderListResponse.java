package com.shopme.admin.dto.response;

import com.shopme.common.entity.OrderStatus;
import com.shopme.common.entity.PaymentMethod;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class OrderListResponse {
    private Integer id;
    private String customerName;
    private String customerId;
    private Float total;
    private Date orderTime;
    private String address;
    private PaymentMethod paymentMethod;
    private OrderStatus status;
}
