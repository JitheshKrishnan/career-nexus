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
@RequestMapping("/api/password-reset")
@Tag(name = "Password Reset", description = "Password reset operations")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @Autowired
    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/request")
    @Operation(summary = "Request password reset")
    public ResponseEntity<Void> requestPasswordReset(
            @Valid @RequestBody PasswordResetRequest request) {
        passwordResetService.requestPasswordReset(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset")
    @Operation(summary = "Reset password")
    public ResponseEntity<Void> resetPassword(
            @Valid @RequestBody PasswordResetConfirmRequest request) {
        passwordResetService.resetPassword(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/validate-token")
    @Operation(summary = "Validate reset token")
    public ResponseEntity<Boolean> validateResetToken(
            @RequestParam String token) {
        boolean isValid = passwordResetService.validateResetToken(token);
        return ResponseEntity.ok(isValid);
    }
}