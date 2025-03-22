package com.shopme.client.dto.response;

import com.shopme.common.entity.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderTrackResponse {
    private Integer id;
    private OrderStatus status;
    private String notes;
    private String updatedTime;
}
