package com.example.user_service.factory;

import com.example.user_service.model.PasswordResetToken;
import com.example.user_service.model.TokenType;
import com.example.user_service.util.TokenGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class PasswordResetTokenFactory implements TokenFactory {

    private final TokenGenerator tokenGenerator;

    @Value("${token.password-reset.expiration-hours:1}")
    private int expirationHours;

    public PasswordResetTokenFactory(TokenGenerator tokenGenerator) {
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public PasswordResetToken createToken(Long userId) {
        return PasswordResetToken.builder()
                .userId(userId)
                .token(tokenGenerator.generateSecureToken())
                .expiresAt(LocalDateTime.now().plusHours(expirationHours))
                .build();
    }

    @Override
    public TokenType getTokenType() {
        return TokenType.PASSWORD_RESET;
    }

    @Override
    public Duration getExpirationDuration() {
        return Duration.ofHours(expirationHours);
    }
}