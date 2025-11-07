package com.example.user_service.factory;

import com.example.user_service.model.Token;
import com.example.user_service.model.TokenType;

import java.time.Duration;

public interface TokenFactory {
    Token createToken(Long userId);
    TokenType getTokenType();
    Duration getExpirationDuration();
}