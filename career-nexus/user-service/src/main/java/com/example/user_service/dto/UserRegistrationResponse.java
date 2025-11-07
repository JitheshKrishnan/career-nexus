package com.example.user_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistrationResponse {
    private Long userId;
    private String email;
    private String fullName;
    private String message;
    private Boolean verificationEmailSent;
}