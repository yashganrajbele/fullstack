package com.example.backend.auth.service;

import com.example.backend.auth.dto.request.ResetPasswordRequest;

public interface ForgotPasswordService {
    void sendPasswordResetOtp(String email);

    void resetPassword(ResetPasswordRequest request);
}
