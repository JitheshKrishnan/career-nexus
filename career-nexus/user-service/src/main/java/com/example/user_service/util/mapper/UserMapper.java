package com.example.user_service.util.mapper;

import com.example.user_service.dto.*;
import com.example.user_service.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .profilePictureUrl(user.getProfilePictureUrl())
                .preferredJobTitle(user.getPreferredJobTitle())
                .preferredLocation(user.getPreferredLocation())
                .yearsOfExperience(user.getYearsOfExperience())
                .accountStatus(user.getAccountStatus().name())
                .emailVerified(user.getEmailVerified())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public User toEntity(UserDTO dto) {
        return User.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .fullName(dto.getFullName())
                .phoneNumber(dto.getPhoneNumber())
                .profilePictureUrl(dto.getProfilePictureUrl())
                .preferredJobTitle(dto.getPreferredJobTitle())
                .preferredLocation(dto.getPreferredLocation())
                .yearsOfExperience(dto.getYearsOfExperience())
                .emailVerified(dto.getEmailVerified())
                .build();
    }

    public UserRegistrationResponse toRegistrationResponse(User user) {
        return UserRegistrationResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();
    }

    public void updateEntityFromDTO(User user, UserUpdateRequest dto) {
        if (dto.getFullName() != null) {
            user.setFullName(dto.getFullName());
        }
        if (dto.getPhoneNumber() != null) {
            user.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getProfilePictureUrl() != null) {
            user.setProfilePictureUrl(dto.getProfilePictureUrl());
        }
        if (dto.getPreferredJobTitle() != null) {
            user.setPreferredJobTitle(dto.getPreferredJobTitle());
        }
        if (dto.getPreferredLocation() != null) {
            user.setPreferredLocation(dto.getPreferredLocation());
        }
        if (dto.getYearsOfExperience() != null) {
            user.setYearsOfExperience(dto.getYearsOfExperience());
        }
    }
}