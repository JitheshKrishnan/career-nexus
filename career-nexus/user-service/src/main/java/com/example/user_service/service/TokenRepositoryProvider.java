package com.example.user_service.service;

import com.example.user_service.model.*;
import com.example.user_service.repository.*;
import com.example.user_service.dto.*;
import com.example.user_service.factory.*;
import com.example.user_service.util.*;
import com.example.user_service.exception.*;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class TokenRepositoryProvider {

    private final Map<TokenType, BaseTokenRepository<?>> repositories;

    public TokenRepositoryProvider(
            EmailVerificationTokenRepository emailRepo,
            PasswordResetTokenRepository passwordRepo,
            RefreshTokenRepository refreshRepo) {
        this.repositories = new HashMap<>();
        repositories.put(TokenType.EMAIL_VERIFICATION, emailRepo);
        repositories.put(TokenType.PASSWORD_RESET, passwordRepo);
        repositories.put(TokenType.REFRESH_TOKEN, refreshRepo);
    }

    @SuppressWarnings("unchecked")
    public <T extends Token> BaseTokenRepository<T> getRepository(TokenType tokenType) {
        BaseTokenRepository<?> repository = repositories.get(tokenType);
        if (repository == null) {
            throw new IllegalArgumentException("No repository found for token type: " + tokenType);
        }
        return (BaseTokenRepository<T>) repository;
    }
}