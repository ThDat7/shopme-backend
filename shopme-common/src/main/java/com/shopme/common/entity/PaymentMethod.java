package com.shopme.common.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PaymentMethod {
	COD("Cash on Delivery", "Pay when you receive the goods", ""),
	PAY_OS("PayOS", "Secure payment via PayOS", "https://payos.vn/wp-content/uploads/sites/13/2023/07/payos-logo.svg");

	private final String displayName;
	private final String description;
	private final String icon;
}
