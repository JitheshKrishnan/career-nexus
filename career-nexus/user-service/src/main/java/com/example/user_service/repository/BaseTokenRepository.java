package com.example.user_service.repository;

import com.example.user_service.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BaseTokenRepository<T extends Token> extends JpaRepository<T, Long> {
    Optional<T> findByToken(String token);
    List<T> findByUserId(Long userId);
    void deleteByExpiresAtBefore(LocalDateTime date);
}
