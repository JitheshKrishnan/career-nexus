package com.careernexus.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String name;
    private String phone;
    private String profilePicture;
    private String bio;
    private String location;
    private List<String> skills;
    private String githubUrl;
    private String linkedinUrl;
    private String leetcodeUsername;
    private String codeforcesUsername;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserDTO(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
}