package com.example.user_service.controller;

import com.example.user_service.dto.*;
import com.example.user_service.model.AccountStatus;
import com.example.user_service.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "User registration and profile management")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<UserRegistrationResponse> registerUser(
            @Valid @RequestBody UserRegistrationRequest request) {
        UserRegistrationResponse response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/verify-email")
    @Operation(summary = "Verify user email")
    public ResponseEntity<EmailVerificationResponse> verifyEmail(
            @RequestParam String token) {
        EmailVerificationResponse response = userService.verifyEmail(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resend-verification")
    @Operation(summary = "Resend verification email")
    public ResponseEntity<Void> resendVerificationEmail(
            @RequestParam String email) {
        userService.resendVerificationEmail(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable Long userId) {
        UserDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update user profile")
    public ResponseEntity<UserDTO> updateUserProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UserUpdateRequest request) {
        UserDTO user = userService.updateUser(userId, request);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user account")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get all users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get users by status")
    public ResponseEntity<List<UserDTO>> getUsersByStatus(
            @PathVariable AccountStatus status) {
        List<UserDTO> users = userService.getUsersByStatus(status);
        return ResponseEntity.ok(users);
    }
}