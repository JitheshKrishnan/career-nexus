package com.example.user_service.repository;

import com.example.user_service.model.RefreshToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefreshTokenRepository extends BaseTokenRepository<RefreshToken> {
    @Modifying
    @Query("UPDATE RefreshToken r SET r.revoked = true, r.revokedAt = CURRENT_TIMESTAMP WHERE r.userId = :userId")
    void revokeAllByUserId(Long userId);

    List<RefreshToken> findByUserIdAndRevokedFalse(Long userId);
}