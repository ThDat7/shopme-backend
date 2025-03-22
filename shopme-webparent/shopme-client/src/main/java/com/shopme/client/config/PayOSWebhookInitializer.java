package com.shopme.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import vn.payos.PayOS;

@Component
public class PayOSWebhookInitializer {
    private final PayOS payOS;
    private final String publicHost;

    public PayOSWebhookInitializer(PayOS payOS,
                                   @Value("${server.public.host}") String publicHost) {
        this.payOS = payOS;
        this.publicHost = publicHost;
    }

    @EventListener(ApplicationReadyEvent.class)
    private void confirmWebhook() throws Exception {
        String webhookUrl = publicHost + "/api/v1/checkout/payment/PAY_OS/handler";
        payOS.confirmWebhook(webhookUrl);
    }
}
