package com.example.user_service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenRefreshRequest {
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
}