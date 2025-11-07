package com.example.user_service.service;

import com.example.user_service.model.*;
import com.example.user_service.repository.*;
import com.example.user_service.dto.*;
import com.example.user_service.factory.*;
import com.example.user_service.util.*;
import com.example.user_service.exception.*;
import com.example.user_service.util.validator.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@Transactional
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public PasswordResetServiceImpl(UserRepository userRepository,
                                    TokenService tokenService,
                                    PasswordEncoder passwordEncoder,
                                    EmailService emailService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public void requestPasswordReset(PasswordResetRequest request) {
        // Find user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));

        // Generate reset token
        Token resetToken = tokenService.createToken(user.getId(), TokenType.PASSWORD_RESET);

        // Send reset email
        emailService.sendPasswordResetEmail(user.getEmail(), resetToken.getToken());
    }

    @Override
    public void resetPassword(PasswordResetConfirmRequest request) {
        // Validate passwords match
        if (!request.passwordsMatch()) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // Validate password strength
        if (!PasswordValidator.validatePassword(request.getNewPassword())) {
            throw new IllegalArgumentException("Password does not meet security requirements");
        }

        // Validate token
        if (!tokenService.validateToken(request.getToken(), TokenType.PASSWORD_RESET)) {
            throw new InvalidTokenException("Invalid or expired reset token");
        }

        // Get token
        Optional<Token> tokenOpt = tokenService.getTokenByValue(
                request.getToken(), TokenType.PASSWORD_RESET);

        if (tokenOpt.isEmpty()) {
            throw new InvalidTokenException("Reset token not found");
        }

        PasswordResetToken resetToken = (PasswordResetToken) tokenOpt.get();

        // Get user
        User user = userRepository.findById(resetToken.getUserId())
                .orElseThrow(() -> new UserNotFoundException(resetToken.getUserId()));

        // Update password
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Mark token as used
        tokenService.invalidateToken(request.getToken(), TokenType.PASSWORD_RESET);
    }

    @Override
    public boolean validateResetToken(String token) {
        return tokenService.validateToken(token, TokenType.PASSWORD_RESET);
    }
}