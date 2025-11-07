package com.example.user_service.model;

import java.time.LocalDateTime;

// ===== Token Interface =====
public interface Token {
    String getToken();
    LocalDateTime getExpiresAt();
    Long getUserId();
    boolean isExpired();
}