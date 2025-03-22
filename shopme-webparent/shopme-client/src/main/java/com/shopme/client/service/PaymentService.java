package com.shopme.client.service;

import com.shopme.client.dto.response.PayOSACKResponse;
import com.shopme.common.entity.Order;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.Webhook;

import java.util.function.BiConsumer;

public interface PaymentService {
    CheckoutResponseData generatePayOSResponse(Order order, String returnUrl, String cancelUrl);

    PayOSACKResponse handlePayOSWebhook(Webhook webhookBody, BiConsumer<Long, Integer> onOrderPaid);

    void cancelPayOSPayment(long orderId);
}
