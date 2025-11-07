package com.example.user_service.service;

import com.example.user_service.dto.PasswordResetConfirmRequest;
import com.example.user_service.dto.PasswordResetRequest;

public interface PasswordResetService {
    void requestPasswordReset(PasswordResetRequest request);
    void resetPassword(PasswordResetConfirmRequest request);
    boolean validateResetToken(String token);
}