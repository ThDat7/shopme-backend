package com.shopme.client.config;

import com.shopme.client.service.SettingService;
import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import com.shopme.common.entity.SettingKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import vn.payos.PayOS;

import java.util.List;

@Configuration
public class PaymentConfig {
    private final SettingService settingService;

    public PaymentConfig(@Lazy SettingService settingService) {
        this.settingService = settingService;
    }

    private String getSettingValue(List<Setting> settings, SettingKey key) {
        return settings.stream().filter(s -> s.getKey().equals(key.name()))
                .map(Setting::getValue)
                .findFirst().orElseThrow(
                () -> new IllegalStateException("Setting not found: " + key));
    }

    @Bean
    public PayOS payOS() {
        List<Setting> paymentSettings = settingService.getSettingsByCategory(SettingCategory.PAYMENT);

        if (paymentSettings.isEmpty())
            return new PayOS("", "", "");

        String clientId = getSettingValue(paymentSettings, SettingKey.PAYOS_CLIENT_ID);
        String apiKey = getSettingValue(paymentSettings, SettingKey.PAYOS_API_KEY);
        String checksumKey = getSettingValue(paymentSettings, SettingKey.PAYOS_CHECKSUM_KEY);
        return new PayOS(clientId, apiKey, checksumKey);
    }

}
