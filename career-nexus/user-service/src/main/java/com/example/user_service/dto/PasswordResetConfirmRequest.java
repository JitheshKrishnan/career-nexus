package com.example.user_service.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetConfirmRequest {
    @NotBlank(message = "Token is required")
    private String token;

    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String newPassword;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    public boolean passwordsMatch() {
        return newPassword != null && newPassword.equals(confirmPassword);
    }
}