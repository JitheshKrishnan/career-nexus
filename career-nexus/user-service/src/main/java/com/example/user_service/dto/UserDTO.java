package com.example.user_service.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String profilePictureUrl;
    private String preferredJobTitle;
    private String preferredLocation;
    private Integer yearsOfExperience;
    private String accountStatus;
    private Boolean emailVerified;
    private LocalDateTime createdAt;
}