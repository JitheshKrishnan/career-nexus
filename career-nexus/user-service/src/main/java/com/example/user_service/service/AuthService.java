package com.example.user_service.service;

import com.example.user_service.dto.LoginRequest;
import com.example.user_service.dto.LoginResponse;
import com.example.user_service.dto.TokenRefreshRequest;
import com.example.user_service.dto.TokenRefreshResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    void logout(Long userId, String accessToken);
    TokenRefreshResponse refreshToken(TokenRefreshRequest request);
    boolean validateToken(String token);
    Long getUserIdFromToken(String token);
}