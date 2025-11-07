package com.example.user_service.factory;

import com.example.user_service.model.EmailVerificationToken;
import com.example.user_service.model.TokenType;
import com.example.user_service.util.TokenGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class EmailVerificationTokenFactory implements TokenFactory {

    private final TokenGenerator tokenGenerator;

    @Value("${token.email-verification.expiration-hours:24}")
    private int expirationHours;

    public EmailVerificationTokenFactory(TokenGenerator tokenGenerator) {
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public EmailVerificationToken createToken(Long userId) {
        return EmailVerificationToken.builder()
                .userId(userId)
                .token(tokenGenerator.generateSecureToken())
                .expiresAt(LocalDateTime.now().plusHours(expirationHours))
                .build();
    }

    @Override
    public TokenType getTokenType() {
        return TokenType.EMAIL_VERIFICATION;
    }

    @Override
    public Duration getExpirationDuration() {
        return Duration.ofHours(expirationHours);
    }
}