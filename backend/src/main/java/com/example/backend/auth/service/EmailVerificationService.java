package com.example.backend.auth.service;

import com.example.backend.auth.dto.request.VerifyEmailRequest;

public interface EmailVerificationService {
    String sendEmailVerificationOtp(String email);

    void verifyEmail(VerifyEmailRequest request);
}