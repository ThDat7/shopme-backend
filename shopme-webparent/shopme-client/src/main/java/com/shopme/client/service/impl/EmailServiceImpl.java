package com.shopme.client.service.impl;

import com.shopme.client.repository.SettingRepository;
import com.shopme.client.service.EmailService;
import com.shopme.common.entity.SettingKey;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final SettingRepository settingRepository;

    private String getEmailFrom() {
        return settingRepository.findByKey(SettingKey.MAIL_SERVER_FROM.name())
                .orElseThrow(() -> new RuntimeException("Email from setting not found"))
                .getValue();
    }

    private void sendEmail(String recipientEmail, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(getEmailFrom());
        mailMessage.setTo(recipientEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }

    public void sendVerificationEmail(String recipientEmail, String verificationCode) {
        String subject = "Please verify your email address";
//        6 digit random number
        String message = String.format("Your verification code is: %s ", verificationCode);
        sendEmail(recipientEmail, subject, message);
    }
}
