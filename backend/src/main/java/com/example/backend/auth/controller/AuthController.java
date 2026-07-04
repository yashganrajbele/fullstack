package com.example.backend.auth.controller;

import com.example.backend.common.dto.ApiResponse;
import com.example.backend.auth.dto.request.*;
import com.example.backend.auth.dto.response.AuthResponse;
import com.example.backend.auth.service.AuthService;
import com.example.backend.auth.service.EmailVerificationService;
import com.example.backend.auth.service.ForgotPasswordService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final ForgotPasswordService forgotPasswordService;
    private final EmailVerificationService emailVerificationService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request, HttpServletRequest httpRequest) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
                response,
                "Registration successful",
                httpRequest.getRequestURI()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(
                response,
                "Login successful",
                httpRequest.getRequestURI()
        ));
    }

    @PostMapping("/google")
    public ResponseEntity<ApiResponse<AuthResponse>> googleLogin(
            @Valid @RequestBody GoogleLoginRequest request,
            HttpServletRequest httpRequest
    ) {
        AuthResponse response = authService.googleLogin(request);
        return ResponseEntity.ok(ApiResponse.success(
                response,
                "Google login successful",
                httpRequest.getRequestURI()
        ));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            HttpServletRequest httpRequest
    ) {
        String response = authService.changePassword(request);
        return ResponseEntity.ok(ApiResponse.success(
                response,
                "Password changed successfully",
                httpRequest.getRequestURI()
        ));
    }

    @PostMapping("/send-password-reset-otp")
    public ResponseEntity<ApiResponse<String>> sendPasswordResetOtp(
            @Valid @RequestBody SendOtpRequest request,
            HttpServletRequest httpRequest
    ) {
        String otp = forgotPasswordService.sendPasswordResetOtp(request.email());
        return ResponseEntity.ok(ApiResponse.success(
                null,
                "A six-digit password reset OTP has been sent to %s. OTP: %s".formatted(request.email(), otp),
                httpRequest.getRequestURI()
        ));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request,
            HttpServletRequest httpRequest
    ) {
        forgotPasswordService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success(
                null,
                "Password reset successful",
                httpRequest.getRequestURI()
        ));
    }

    @PostMapping("/send-email-verification-otp")
    public ResponseEntity<ApiResponse<String>> sendEmailVerificationOtp(
            @Valid @RequestBody SendOtpRequest request,
            HttpServletRequest httpRequest
    ) {
        String otp = emailVerificationService.sendEmailVerificationOtp(request.email());
        return ResponseEntity.ok(ApiResponse.success(
                null,
                "A six-digit email verification OTP has been sent to %s. OTP: %s".formatted(request.email(), otp),
                httpRequest.getRequestURI()
        ));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<String>> verifyEmail(
            @Valid @RequestBody VerifyEmailRequest request,
            HttpServletRequest httpRequest
    ) {
        emailVerificationService.verifyEmail(request);
        return ResponseEntity.ok(ApiResponse.success(
                null,
                "Email verification successful",
                httpRequest.getRequestURI()
        ));
    }
}
