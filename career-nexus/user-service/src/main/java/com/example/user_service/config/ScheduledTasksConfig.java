package com.example.user_service.config;

import com.example.user_service.repository.JwtBlacklistRepository;
import com.example.user_service.service.TokenService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
public class ScheduledTasksConfig {

    private final TokenService tokenService;
    private final JwtBlacklistRepository jwtBlacklistRepository;

    public ScheduledTasksConfig(TokenService tokenService,
                                JwtBlacklistRepository jwtBlacklistRepository) {
        this.tokenService = tokenService;
        this.jwtBlacklistRepository = jwtBlacklistRepository;
    }

    @Scheduled(cron = "0 0 2 * * *") // 2 AM daily
    public void cleanupExpiredTokens() {
        tokenService.cleanupExpiredTokens();
        jwtBlacklistRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }
}