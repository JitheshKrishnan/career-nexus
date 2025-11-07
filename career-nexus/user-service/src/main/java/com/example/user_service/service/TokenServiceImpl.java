package com.example.user_service.service;

import com.example.user_service.model.*;
import com.example.user_service.repository.*;
import com.example.user_service.dto.*;
import com.example.user_service.factory.*;
import com.example.user_service.util.*;
import com.example.user_service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class TokenServiceImpl implements TokenService {

    private final TokenFactoryProvider factoryProvider;
    private final TokenRepositoryProvider repositoryProvider;

    @Autowired
    public TokenServiceImpl(TokenFactoryProvider factoryProvider,
                            TokenRepositoryProvider repositoryProvider) {
        this.factoryProvider = factoryProvider;
        this.repositoryProvider = repositoryProvider;
    }

    @Override
    public Token createToken(Long userId, TokenType tokenType) {
        // Get appropriate factory
        TokenFactory factory = factoryProvider.getFactory(tokenType);

        // Create token
        Token token = factory.createToken(userId);

        // Save token
        return saveToken(token, tokenType);
    }

    @Override
    public boolean validateToken(String tokenValue, TokenType tokenType) {
        BaseTokenRepository<?> repository = repositoryProvider.getRepository(tokenType);
        Optional<? extends Token> token = repository.findByToken(tokenValue);

        if (token.isEmpty()) {
            return false;
        }

        Token foundToken = token.get();

        // Check if expired
        if (foundToken.isExpired()) {
            return false;
        }

        // Additional checks based on token type
        if (tokenType == TokenType.PASSWORD_RESET && foundToken instanceof PasswordResetToken) {
            return !((PasswordResetToken) foundToken).isUsed();
        }

        if (tokenType == TokenType.REFRESH_TOKEN && foundToken instanceof RefreshToken) {
            return !((RefreshToken) foundToken).isRevoked();
        }

        return true;
    }

    @Override
    public Optional<Token> getTokenByValue(String tokenValue, TokenType tokenType) {
        BaseTokenRepository<?> repository = repositoryProvider.getRepository(tokenType);
        return repository.findByToken(tokenValue).map(token -> (Token) token);
    }

    @Override
    public void invalidateToken(String tokenValue, TokenType tokenType) {
        Optional<Token> tokenOpt = getTokenByValue(tokenValue, tokenType);

        if (tokenOpt.isPresent()) {
            Token token = tokenOpt.get();

            if (token instanceof RefreshToken) {
                ((RefreshToken) token).revoke();
                BaseTokenRepository<RefreshToken> repo = repositoryProvider.getRepository(tokenType);
                repo.save((RefreshToken) token);
            } else if (token instanceof PasswordResetToken) {
                ((PasswordResetToken) token).markAsUsed();
                BaseTokenRepository<PasswordResetToken> repo = repositoryProvider.getRepository(tokenType);
                repo.save((PasswordResetToken) token);
            }
        }
    }

    @Override
    public void cleanupExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();

        for (TokenType type : TokenType.values()) {
            BaseTokenRepository<?> repository = repositoryProvider.getRepository(type);
            repository.deleteByExpiresAtBefore(now);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Token> T saveToken(Token token, TokenType tokenType) {
        BaseTokenRepository<T> repository = repositoryProvider.getRepository(tokenType);
        return repository.save((T) token);
    }
}