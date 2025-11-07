package com.example.user_service.repository;

import com.example.user_service.model.PasswordResetToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends BaseTokenRepository<PasswordResetToken> {
    @Modifying
    @Query("DELETE FROM PasswordResetToken p WHERE p.userId = :userId")
    void deleteByUserId(Long userId);
}