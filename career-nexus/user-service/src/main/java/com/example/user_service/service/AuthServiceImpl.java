package com.example.user_service.service;

import com.example.user_service.model.*;
import com.example.user_service.repository.*;
import com.example.user_service.dto.*;
import com.example.user_service.factory.*;
import com.example.user_service.util.*;
import com.example.user_service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final JwtBlacklistRepository jwtBlacklistRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           TokenService tokenService,
                           JwtBlacklistRepository jwtBlacklistRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.jwtBlacklistRepository = jwtBlacklistRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        // Find user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        // Check if email is verified
        if (!user.getEmailVerified()) {
            throw new EmailNotVerifiedException(user.getEmail());
        }

        // Check account status
        if (!user.isActive()) {
            throw new IllegalStateException("Account is not active");
        }

        // Generate tokens
        String accessToken = generateAccessToken(user);
        Token refreshToken = tokenService.createToken(user.getId(), TokenType.REFRESH_TOKEN);

        // Update last login
        user.updateLastLogin();
        userRepository.save(user);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .expiresIn(jwtUtil.getAccessTokenExpiration())
                .user(UserDTO.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .fullName(user.getFullName())
                        .build())
                .build();
    }

    @Override
    public void logout(Long userId, String accessToken) {
        // Blacklist access token
        blacklistToken(accessToken, userId);

        // Revoke all refresh tokens
        // Note: This requires RefreshTokenRepository to be accessible
        // For now, we'll just blacklist the access token
    }

    @Override
    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) {
        // Validate refresh token
        if (!tokenService.validateToken(request.getRefreshToken(), TokenType.REFRESH_TOKEN)) {
            throw new InvalidTokenException("Invalid or expired refresh token");
        }

        // Get token
        Optional<Token> tokenOpt = tokenService.getTokenByValue(
                request.getRefreshToken(), TokenType.REFRESH_TOKEN);

        if (tokenOpt.isEmpty()) {
            throw new InvalidTokenException("Refresh token not found");
        }

        RefreshToken refreshToken = (RefreshToken) tokenOpt.get();

        // Get user
        User user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new UserNotFoundException(refreshToken.getUserId()));

        // Generate new access token
        String newAccessToken = generateAccessToken(user);

        // Generate new refresh token
        Token newRefreshToken = tokenService.createToken(user.getId(), TokenType.REFRESH_TOKEN);

        // Invalidate old refresh token
        tokenService.invalidateToken(request.getRefreshToken(), TokenType.REFRESH_TOKEN);

        return TokenRefreshResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .expiresIn(jwtUtil.getAccessTokenExpiration())
                .build();
    }

    @Override
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token) && !isTokenBlacklisted(token);
    }

    @Override
    public Long getUserIdFromToken(String token) {
        return jwtUtil.getUserIdFromToken(token);
    }

    private String generateAccessToken(User user) {
        return jwtUtil.generateAccessToken(user);
    }

    private void blacklistToken(String token, Long userId) {
        String tokenHash = TokenGenerator.hashToken(token);
        LocalDateTime expiresAt = jwtUtil.getExpirationDateFromToken(token);

        JwtBlacklist blacklist = JwtBlacklist.builder()
                .tokenHash(tokenHash)
                .userId(userId)
                .expiresAt(expiresAt)
                .reason("User logout")
                .build();

        jwtBlacklistRepository.save(blacklist);
    }

    private boolean isTokenBlacklisted(String token) {
        String tokenHash = TokenGenerator.hashToken(token);
        return jwtBlacklistRepository.existsByTokenHash(tokenHash);
    }
}