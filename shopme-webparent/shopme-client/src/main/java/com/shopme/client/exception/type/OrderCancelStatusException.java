package com.shopme.client.exception.type;

import com.shopme.common.entity.OrderStatus;

public class OrderCancelStatusException extends BusinessException {
    public OrderCancelStatusException(OrderStatus orderStatus) {
        super("Đơn hàng không thể hủy bỏ vì nó đang ở trạng thái " + orderStatus.getDescription());
    }
}
