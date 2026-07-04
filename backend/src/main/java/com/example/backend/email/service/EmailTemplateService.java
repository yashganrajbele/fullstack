package com.example.backend.email.service;

import com.example.backend.common.event.events.AdminAccessRevokedEvent;
import com.example.backend.common.event.events.RequestStatusUpdatedEvent;

public interface EmailTemplateService {
    String buildWelcomeEmail(String username);
    String buildAdminAccessRequestStatusEmail(RequestStatusUpdatedEvent event);
    String buildAdminAccessRevokedEmail(AdminAccessRevokedEvent event);
    String buildPasswordResetOtpEmail(String username, String otp);
    String buildEmailVerificationOtpEmail(String username,String otp);
}

