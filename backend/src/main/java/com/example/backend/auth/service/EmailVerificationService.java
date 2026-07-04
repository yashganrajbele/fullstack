package com.example.backend.auth.service;

import com.example.backend.auth.dto.request.VerifyEmailRequest;

public interface EmailVerificationService {
    void sendEmailVerificationOtp(String email);

    void verifyEmail(VerifyEmailRequest request);
}