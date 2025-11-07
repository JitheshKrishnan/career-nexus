package com.example.user_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    private String fullName;
    private String phoneNumber;
    private String profilePictureUrl;
    private String preferredJobTitle;
    private String preferredLocation;
    private Integer yearsOfExperience;
}