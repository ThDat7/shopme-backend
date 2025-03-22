package com.shopme.client.service.impl;

import com.shopme.client.dto.response.PayOSACKResponse;
import com.shopme.client.service.PaymentService;
import com.shopme.common.entity.Order;
import com.shopme.common.entity.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.*;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PayOS payOS;

    @Override
    public CheckoutResponseData generatePayOSResponse(Order order, String returnUrl, String cancelUrl) {
        Long orderCode = order.getId().longValue();
        Integer amount = Math.round(order.getTotal());
        String description = "";
        String buyerName = String.format("%s %s", order.getCustomer().getFirstName(), order.getCustomer().getLastName());
        String buyerEmail = order.getCustomer().getEmail();
        String buyerPhone = order.getCustomer().getPhoneNumber();
        String buyerAddress = "";

        List<ItemData> items = order.getOrderDetails().stream()
                .map(orderDetail -> ItemData.builder()
                        .name(orderDetail.getProduct().getName())
                        .quantity(orderDetail.getQuantity())
                        .price(Math.round(orderDetail.getSubtotal()))
                        .build())
                .toList();


        PaymentData paymentData = PaymentData.builder()
                .orderCode(orderCode)
                .amount(amount)
                .description(description)
                .items(items)
                .cancelUrl(cancelUrl)
                .returnUrl(returnUrl)
                .buyerName(buyerName)
                .buyerEmail(buyerEmail)
                .buyerPhone(buyerPhone)
                .buyerAddress(buyerAddress)
                .build();

        try {
            return payOS.createPaymentLink(paymentData);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PayOS response: ", e);
        }
    }

    @Override
    public PayOSACKResponse handlePayOSWebhook(Webhook webhookBody, BiConsumer<Long, Integer> onOrderPaid) {
        try {
            WebhookData data = payOS.verifyPaymentWebhookData(webhookBody);
            Long orderCode = data.getOrderCode();
            Integer amount = data.getAmount();
            onOrderPaid.accept(orderCode, amount);
        } catch (Exception e) {
            throw new RuntimeException("Failed to handle PayOS webhook: ", e);
        }
        return PayOSACKResponse.builder().success(true).build();
    }

    @Override
    public void cancelPayOSPayment(long orderId) {
        try {
            String cancelReason = "User cancelled the order";
            payOS.cancelPaymentLink(orderId, cancelReason);
        } catch (Exception e) {
            throw new RuntimeException("Failed to cancel PayOS payment: ", e);
        }
    }
}
