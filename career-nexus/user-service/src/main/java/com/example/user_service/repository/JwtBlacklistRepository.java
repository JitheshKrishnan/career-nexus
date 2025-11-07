package com.example.user_service.repository;

import com.example.user_service.model.JwtBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface JwtBlacklistRepository extends JpaRepository<JwtBlacklist, Long> {
    boolean existsByTokenHash(String tokenHash);
    void deleteByExpiresAtBefore(LocalDateTime date);
}