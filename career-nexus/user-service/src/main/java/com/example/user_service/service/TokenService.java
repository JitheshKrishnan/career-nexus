package com.example.user_service.service;

import com.example.user_service.model.*;
import java.util.*;

public interface TokenService {
    Token createToken(Long userId, TokenType tokenType);
    boolean validateToken(String token, TokenType tokenType);
    Optional<Token> getTokenByValue(String token, TokenType tokenType);
    void invalidateToken(String token, TokenType tokenType);
    void cleanupExpiredTokens();
}