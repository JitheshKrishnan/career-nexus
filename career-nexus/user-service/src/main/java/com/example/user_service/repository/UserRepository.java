package com.example.user_service.repository;

import com.example.user_service.model.AccountStatus;
import com.example.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByIdAndAccountStatus(Long id, AccountStatus status);
    boolean existsByEmail(String email);
    List<User> findAllByAccountStatus(AccountStatus status);
    List<User> findByEmailVerified(Boolean verified);
    long countByAccountStatus(AccountStatus status);
}