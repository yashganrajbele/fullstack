package com.example.backend.email.service;

import com.example.backend.common.event.events.*;

public interface EmailService {
    void sendWelcomeEmail(AccountCreatedEvent event);

    void sendPasswordResetOtp(PasswordResetOtpRequestedEvent event);

    void sendEmailVerificationOtp(EmailVerificationOtpRequestedEvent event);

    void sendRequestStatusUpdate(RequestStatusUpdatedEvent event);

    void sendAdminAccessRevokedEmail(AdminAccessRevokedEvent event);
}
