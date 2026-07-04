package com.example.backend.auth.service;

import com.example.backend.auth.dto.request.ResetPasswordRequest;

public interface ForgotPasswordService {
    String sendPasswordResetOtp(String email);

    void resetPassword(ResetPasswordRequest request);
}
