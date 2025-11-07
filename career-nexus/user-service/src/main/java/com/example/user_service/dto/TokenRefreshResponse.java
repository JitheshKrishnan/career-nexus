package com.example.user_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenRefreshResponse {
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
}