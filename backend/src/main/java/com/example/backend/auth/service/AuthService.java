package com.example.backend.auth.service;

import com.example.backend.auth.dto.request.ChangePasswordRequest;
import com.example.backend.auth.dto.request.GoogleLoginRequest;
import com.example.backend.auth.dto.request.LoginRequest;
import com.example.backend.auth.dto.request.RegisterRequest;
import com.example.backend.auth.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse googleLogin(GoogleLoginRequest request);
    String changePassword(ChangePasswordRequest request);
}
