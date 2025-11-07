package com.example.user_service.controller;

import com.example.user_service.dto.*;
import com.example.user_service.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "User authentication and token management")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "User login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "User logout")
    public ResponseEntity<Void> logout(
            @RequestParam Long userId,
            @RequestHeader("Authorization") String token) {
        // Remove "Bearer " prefix
        String accessToken = token.substring(7);
        authService.logout(userId, accessToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh access token")
    public ResponseEntity<TokenRefreshResponse> refreshToken(
            @Valid @RequestBody TokenRefreshRequest request) {
        TokenRefreshResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate")
    @Operation(summary = "Validate JWT token")
    public ResponseEntity<Boolean> validateToken(
            @RequestHeader("Authorization") String token) {
        String accessToken = token.substring(7);
        boolean isValid = authService.validateToken(accessToken);
        return ResponseEntity.ok(isValid);
    }
}