package com.example.user_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailVerificationResponse {
    private Boolean success;
    private String message;
    private Long userId;
}