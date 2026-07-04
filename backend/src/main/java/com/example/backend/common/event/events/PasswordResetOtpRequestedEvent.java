package com.example.backend.common.event.events;

public record PasswordResetOtpRequestedEvent(
        String email,
        String username,
        String otp
) {
}