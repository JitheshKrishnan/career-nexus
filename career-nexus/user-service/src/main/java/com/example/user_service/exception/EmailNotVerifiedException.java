package com.example.user_service.exception;

public class EmailNotVerifiedException extends RuntimeException {
    public EmailNotVerifiedException(String email) {
        super("Email not verified: " + email);
    }
}