package com.example.backend.auth.repository;

import com.example.backend.auth.entity.PasswordResetOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp, String> {
    Optional<PasswordResetOtp> findByEmail(String email);
}

// findFirstByEmailOrderByExpiresAtDesc can also be used