package com.shopme.client.service;

public interface EmailService {
    void sendVerificationEmail(String recipientEmail, String verificationCode);
}
