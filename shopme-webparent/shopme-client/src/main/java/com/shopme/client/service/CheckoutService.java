package com.shopme.client.service;

import com.shopme.client.dto.request.PlaceOrderCODRequest;
import com.shopme.client.dto.request.PlaceOrderPayOSRequest;
import com.shopme.client.dto.response.PayOSACKResponse;
import com.shopme.client.dto.response.PlaceOrderPayOSResponse;
import vn.payos.type.Webhook;

public interface CheckoutService {

    PlaceOrderPayOSResponse placeOrderPayOS(PlaceOrderPayOSRequest request);

    void placeOrderCOD(PlaceOrderCODRequest request);

    PayOSACKResponse payosTransferHandler(Webhook webhookBody);
}
