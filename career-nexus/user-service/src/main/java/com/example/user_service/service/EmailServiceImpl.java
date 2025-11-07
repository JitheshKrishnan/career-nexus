package com.example.user_service.service;

import com.example.user_service.client.NotificationServiceClient;
import com.example.user_service.model.*;
import com.example.user_service.repository.*;
import com.example.user_service.dto.*;
import com.example.user_service.factory.*;
import com.example.user_service.util.*;
import com.example.user_service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class EmailServiceImpl implements EmailService {

    private final NotificationServiceClient notificationServiceClient;

    @Value("${app.base-url}")
    private String baseUrl;

    @Autowired
    public EmailServiceImpl(NotificationServiceClient notificationServiceClient) {
        this.notificationServiceClient = notificationServiceClient;
    }

    @Override
    public void sendVerificationEmail(String email, String token) {
        String verificationUrl = baseUrl + "/api/users/verify-email?token=" + token;

        Map<String, Object> templateData = new HashMap<>();
        templateData.put("verificationUrl", verificationUrl);
        templateData.put("token", token);

        SendNotificationRequest request = SendNotificationRequest.builder()
                .userId(null) // Email-based, no userId yet
                .type("EMAIL_VERIFICATION")
                .channels(List.of("EMAIL"))
                .priority("HIGH")
                .templateName("email_verification")
                .data(templateData)
                .build();

        notificationServiceClient.sendNotification(request);
    }

    @Override
    public void sendPasswordResetEmail(String email, String token) {
        String resetUrl = baseUrl + "/reset-password?token=" + token;

        Map<String, Object> templateData = new HashMap<>();
        templateData.put("resetUrl", resetUrl);
        templateData.put("token", token);

        SendNotificationRequest request = SendNotificationRequest.builder()
                .type("PASSWORD_RESET")
                .channels(List.of("EMAIL"))
                .priority("HIGH")
                .templateName("password_reset")
                .data(templateData)
                .build();

        notificationServiceClient.sendNotification(request);
    }

    @Override
    public void sendWelcomeEmail(String email, String fullName) {
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("fullName", fullName);

        SendNotificationRequest request = SendNotificationRequest.builder()
                .type("WELCOME")
                .channels(List.of("EMAIL"))
                .priority("MEDIUM")
                .templateName("welcome_email")
                .data(templateData)
                .build();

        notificationServiceClient.sendNotification(request);
    }
}