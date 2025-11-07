package com.example.user_service.repository;

import com.example.user_service.model.EmailVerificationToken;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationTokenRepository extends BaseTokenRepository<EmailVerificationToken> {
    boolean existsByUserIdAndVerifiedAtIsNull(Long userId);
}