package com.shopme.client.service;

import com.shopme.client.dto.request.PlaceOrderCODRequest;
import com.shopme.client.dto.request.PlaceOrderPayOSRequest;
import com.shopme.client.dto.response.PayOSACKResponse;
import com.shopme.client.dto.response.PlaceOrderPayOSResponse;
import com.shopme.client.dto.response.PaymentMethodResponse;
import vn.payos.type.Webhook;

import java.util.List;

public interface CheckoutService {
    List<PaymentMethodResponse> getPaymentMethods();

    PlaceOrderPayOSResponse placeOrderPayOS(PlaceOrderPayOSRequest request);

    void placeOrderCOD(PlaceOrderCODRequest request);

    PayOSACKResponse payosTransferHandler(Webhook webhookBody);
}
