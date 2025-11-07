package com.example.user_service.service;

import com.example.user_service.model.*;
import com.example.user_service.repository.*;
import com.example.user_service.dto.*;
import com.example.user_service.factory.*;
import com.example.user_service.util.*;
import com.example.user_service.exception.*;
import com.example.user_service.util.mapper.UserMapper;
import com.example.user_service.util.validator.EmailValidator;
import com.example.user_service.util.validator.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final UserPreferencesRepository userPreferencesRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           TokenService tokenService,
                           UserPreferencesRepository userPreferencesRepository,
                           PasswordEncoder passwordEncoder,
                           EmailService emailService,
                           UserMapper userMapper) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.userPreferencesRepository = userPreferencesRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.userMapper = userMapper;
    }

    @Override
    public UserRegistrationResponse registerUser(UserRegistrationRequest request) {
        // Validate request
        validateRegistrationRequest(request);

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(request.getEmail());
        }

        // Create user
        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .preferredJobTitle(request.getPreferredJobTitle())
                .yearsOfExperience(request.getYearsOfExperience())
                .accountStatus(AccountStatus.PENDING_VERIFICATION)
                .emailVerified(false)
                .build();

        user = userRepository.save(user);

        // Create default preferences
        createDefaultPreferences(user.getId());

        // Generate verification token
        Token verificationToken = tokenService.createToken(user.getId(), TokenType.EMAIL_VERIFICATION);

        // Send verification email
        emailService.sendVerificationEmail(user.getEmail(), verificationToken.getToken());

        return UserRegistrationResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .message("Registration successful. Please check your email to verify your account.")
                .verificationEmailSent(true)
                .build();
    }

    @Override
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return userMapper.toDTO(user);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return userMapper.toDTO(user);
    }

    @Override
    public UserDTO updateUser(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        userMapper.updateEntityFromDTO(user, request);
        user = userRepository.save(user);

        return userMapper.toDTO(user);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.setAccountStatus(AccountStatus.DELETED);
        userRepository.save(user);
    }

    @Override
    public EmailVerificationResponse verifyEmail(String tokenValue) {
        // Validate token
        if (!tokenService.validateToken(tokenValue, TokenType.EMAIL_VERIFICATION)) {
            throw new InvalidTokenException("Invalid or expired verification token");
        }

        // Get token
        Optional<Token> tokenOpt = tokenService.getTokenByValue(tokenValue, TokenType.EMAIL_VERIFICATION);
        if (tokenOpt.isEmpty()) {
            throw new InvalidTokenException("Token not found");
        }

        EmailVerificationToken token = (EmailVerificationToken) tokenOpt.get();

        // Get user
        User user = userRepository.findById(token.getUserId())
                .orElseThrow(() -> new UserNotFoundException(token.getUserId()));

        // Mark email as verified
        user.markEmailAsVerified();
        userRepository.save(user);

        // Mark token as used
        token.markAsVerified();

        return EmailVerificationResponse.builder()
                .success(true)
                .message("Email verified successfully")
                .userId(user.getId())
                .build();
    }

    @Override
    public void resendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        if (user.getEmailVerified()) {
            throw new IllegalStateException("Email already verified");
        }

        // Generate new token
        Token verificationToken = tokenService.createToken(user.getId(), TokenType.EMAIL_VERIFICATION);

        // Send verification email
        emailService.sendVerificationEmail(user.getEmail(), verificationToken.getToken());
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getUsersByStatus(AccountStatus status) {
        return userRepository.findAllByAccountStatus(status).stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void createDefaultPreferences(Long userId) {
        UserPreferences preferences = UserPreferences.getDefaultPreferences(userId);
        userPreferencesRepository.save(preferences);
    }

    private void validateRegistrationRequest(UserRegistrationRequest request) {
        if (!EmailValidator.isValidEmail(request.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (!PasswordValidator.validatePassword(request.getPassword())) {
            throw new IllegalArgumentException("Password does not meet security requirements");
        }
    }
}