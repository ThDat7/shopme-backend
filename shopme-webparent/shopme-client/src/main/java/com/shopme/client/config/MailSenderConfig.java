package com.shopme.client.config;

import com.shopme.client.service.SettingService;
import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import com.shopme.common.entity.SettingKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.List;

@Configuration
public class MailSenderConfig {
    private final SettingService settingService;

    public MailSenderConfig(@Lazy SettingService settingService) {
        this.settingService = settingService;
    }

    private String getSettingValue(List<Setting> settings, SettingKey key) {
        return settings.stream().filter(s -> s.getKey().equals(key.name()))
                .map(Setting::getValue)
                .findFirst().orElseThrow(
                        () -> new IllegalStateException("Setting not found: " + key));
    }

    @Bean
    public JavaMailSender mailSender() {
        List<Setting> mailServerSettings = settingService.getSettingsByCategory(SettingCategory.MAIL_SERVER);
        String host = getSettingValue(mailServerSettings, SettingKey.MAIL_SERVER_HOST);
        int port = Integer.parseInt(getSettingValue(mailServerSettings, SettingKey.MAIL_SERVER_PORT));
        String username = getSettingValue(mailServerSettings, SettingKey.MAIL_SERVER_USERNAME);
        String password = getSettingValue(mailServerSettings, SettingKey.MAIL_SERVER_PASSWORD);
        String smtpAuth = getSettingValue(mailServerSettings, SettingKey.MAIL_SERVER_SMTP_AUTH);
        String smtpSecured = getSettingValue(mailServerSettings, SettingKey.MAIL_SERVER_SMTP_SECURED);

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        mailSender.getJavaMailProperties().setProperty("mail.smtp.auth", smtpAuth);
        mailSender.getJavaMailProperties().setProperty("mail.smtp.starttls.enable", smtpSecured);

        return mailSender;
    }
}
