package com.example.user_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "jwt_blacklist")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtBlacklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String tokenHash;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private LocalDateTime blacklistedAt;

    private String reason;

    @PrePersist
    protected void onCreate() {
        blacklistedAt = LocalDateTime.now();
    }
}