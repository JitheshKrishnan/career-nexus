package com.example.user_service.service;

public interface EmailService {
    void sendVerificationEmail(String email, String token);
    void sendPasswordResetEmail(String email, String token);
    void sendWelcomeEmail(String email, String fullName);
}