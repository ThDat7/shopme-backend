package com.shopme.admin.dto.response;

import com.shopme.common.entity.OrderStatus;
import com.shopme.common.entity.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailResponse {
    private Integer id;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String postalCode;

    private Float productCost;
    private Float subtotal;
    private Float shippingCost;
    private Float tax;
    private Float total;
    private PaymentMethod paymentMethod;
    private OrderStatus status;
    private Date orderTime;
    private List<OrderSpecificResponse> details;
}
