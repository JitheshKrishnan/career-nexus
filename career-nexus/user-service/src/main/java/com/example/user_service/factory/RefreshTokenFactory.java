package com.example.user_service.factory;

import com.example.user_service.model.RefreshToken;
import com.example.user_service.model.TokenType;
import com.example.user_service.util.TokenGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class RefreshTokenFactory implements TokenFactory {

    private final TokenGenerator tokenGenerator;

    @Value("${token.refresh-token.expiration-days:30}")
    private int expirationDays;

    public RefreshTokenFactory(TokenGenerator tokenGenerator) {
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public RefreshToken createToken(Long userId) {
        return RefreshToken.builder()
                .userId(userId)
                .token(tokenGenerator.generateSecureToken(64))
                .expiresAt(LocalDateTime.now().plusDays(expirationDays))
                .revoked(false)
                .build();
    }

    @Override
    public TokenType getTokenType() {
        return TokenType.REFRESH_TOKEN;
    }

    @Override
    public Duration getExpirationDuration() {
        return Duration.ofDays(expirationDays);
    }
}