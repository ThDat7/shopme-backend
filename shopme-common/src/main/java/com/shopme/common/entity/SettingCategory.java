package com.shopme.common.entity;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum SettingCategory {
	GENERAL, MAIL_SERVER, MAIL_TEMPLATES, CURRENCY, PAYMENT, OTHER;

	public Set<SettingKey> getSettingKeys() {
		return Arrays.stream(
					SettingKey.values())
					.filter(settingKey -> settingKey.getCategory() == this)
					.collect(Collectors.toSet()
				);
	}
}