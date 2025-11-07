package com.example.user_service.service;

import com.example.user_service.model.*;
import com.example.user_service.dto.*;
import java.util.*;

public interface UserService {
    UserRegistrationResponse registerUser(UserRegistrationRequest request);
    UserDTO getUserById(Long userId);
    UserDTO getUserByEmail(String email);
    UserDTO updateUser(Long userId, UserUpdateRequest request);
    void deleteUser(Long userId);
    EmailVerificationResponse verifyEmail(String token);
    void resendVerificationEmail(String email);
    List<UserDTO> getAllUsers();
    List<UserDTO> getUsersByStatus(AccountStatus status);
}