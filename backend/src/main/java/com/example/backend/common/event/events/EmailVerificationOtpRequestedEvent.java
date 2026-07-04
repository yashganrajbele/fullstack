package com.example.backend.common.event.events;

public record EmailVerificationOtpRequestedEvent(
        String email,
        String username,
        String otp
) {
}
