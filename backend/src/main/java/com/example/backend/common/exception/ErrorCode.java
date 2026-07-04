package com.example.backend.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    BAD_REQUEST("VALIDATION_ERROR", "Invalid request", 400),
    UNAUTHORIZED("UNAUTHORIZED", "Invalid credentials", 401),
    FORBIDDEN("FORBIDDEN", "Access denied", 403),
    NOT_VERIFIED("NOT_VERIFIED", "Please verify your email first", 403),
    ALREADY_VERIFIED("ALREADY_VERIFIED","Email is already verified",400),
    INVALID_TOKEN("INVALID_TOKEN", "Invalid verification token", 400),
    EXPIRED_TOKEN("EXPIRED_TOKEN", "Verification token has been expired", 400),
    ACCOUNT_DISABLED("ACCOUNT_DISABLED", "Your account has been temporarily disabled", 403),
    NOT_FOUND("RESOURCE_NOT_FOUND", "Resource not found", 404),
    CONFLICT("DUPLICATE_RESOURCE", "Resource already exists", 409),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "Something went wrong", 500),

    SAME_PASSWORD("SAME_PASSWORD","New password must be different from the current password",400),
    INVALID_CURRENT_PASSWORD("INVALID_CURRENT_PASSWORD", "Current password is incorrect", 400),
    ACCOUNT_USES_GOOGLE_LOGIN("ACCOUNT_USES_GOOGLE_LOGIN", "This account uses Google Sign-In. Please sign in with Google instead of resetting a password", 400),

    INVALID_OTP("INVALID_OTP", "Invalid OTP", 400),
    EXPIRED_OTP("EXPIRED_OTP", "OTP has expired", 400),
    RESEND_OTP_TOO_SOON("RESEND_OTP_TOO_SOON", "Please wait for 30 seconds before requesting another OTP", 429);

    private final String code;
    private final String message;
    private final int status;
}
