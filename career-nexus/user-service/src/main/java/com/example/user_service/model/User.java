package com.example.user_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_email", columnList = "email"),
                @Index(name = "idx_account_status", columnList = "account_status"),
                @Index(name = "idx_email_verified", columnList = "email_verified"),
                @Index(name = "idx_created_at", columnList = "created_at")
        }
)
@EntityListeners(AuditingEntityListener.class) //Make sure to enable it globally (@EnableJpaAuditing)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "Invalid email format")
    @EqualsAndHashCode.Include
    @NotBlank(message = "Email is required")
    @Column(unique = true, nullable = false, length = 255)
    private String email;

    @JsonIgnore
    @NotBlank(message = "Password is required")
    @Column(name = "password_hash", nullable = false, columnDefinition = "TEXT")
    private String passwordHash;

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "profile_picture_url", length = 500)
    private String profilePictureUrl;

    @NotBlank(message = "Preferred job title is required")
    @Size(max = 100, message = "Job title must not exceed 100 characters")
    @Column(name = "preferred_job_title", nullable = false, length = 100)
    private String preferredJobTitle;

    @Size(max = 255, message = "Location must not exceed 255 characters")
    @Column(name = "preferred_location", length = 255)
    private String preferredLocation;

    @Min(value = 0, message = "Years of experience cannot be negative")
    @Column(name = "years_of_experience", columnDefinition = "INT DEFAULT 0")
    private Integer yearsOfExperience = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false, length = 30)
    private AccountStatus accountStatus = AccountStatus.PENDING_VERIFICATION;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 50)
    private UserRole role = UserRole.JOB_SEEKER;

    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    // Audit fields (optional)
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "modified_by")
    private String modifiedBy;

    // Business logic methods
    public boolean isActive() {
        return AccountStatus.ACTIVE.equals(accountStatus);
    }

    public void markEmailAsVerified() {
        this.emailVerified = true;
        this.accountStatus = AccountStatus.ACTIVE;
    }

    public void updateLastLogin() {
        this.lastLoginAt = LocalDateTime.now();
    }

    public void suspend() {
        this.accountStatus = AccountStatus.SUSPENDED;
    }

    public void softDelete() {
        this.accountStatus = AccountStatus.DELETED;
    }

    public boolean canLogin() {
        return isActive() && emailVerified;
    }

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        if (yearsOfExperience == null) {
            yearsOfExperience = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        if (this.accountStatus == AccountStatus.DELETED) {
            throw new IllegalStateException("Deleted user cannot be updated");
        }
    }
}