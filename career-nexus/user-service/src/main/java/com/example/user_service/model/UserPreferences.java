package com.example.user_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_preferences")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreferences {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long userId;

    @Column(columnDefinition = "VARCHAR(20) DEFAULT 'LIGHT'")
    private String theme = "LIGHT";

    @Column(columnDefinition = "VARCHAR(10) DEFAULT 'en'")
    private String language = "en";

    @Column(columnDefinition = "VARCHAR(50) DEFAULT 'UTC'")
    private String timezone = "UTC";

    public static UserPreferences getDefaultPreferences(Long userId) {
        return UserPreferences.builder()
                .userId(userId)
                .theme("LIGHT")
                .language("en")
                .timezone("UTC")
                .build();
    }
}