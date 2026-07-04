package com.example.backend.email.service.impl;

import com.example.backend.common.enums.RequestStatus;
import com.example.backend.common.event.events.*;
import com.example.backend.email.service.EmailService;
import com.example.backend.email.service.EmailTemplateService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final EmailTemplateService emailTemplateService;
    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendWelcomeEmail(AccountCreatedEvent event) {
        try {
            String html = emailTemplateService.buildWelcomeEmail(event.username());
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    true,
                    StandardCharsets.UTF_8.name()
            );
            helper.setFrom(from);
            helper.setTo(event.email());
            helper.setSubject("Welcome to the Platform");
            helper.setText(html, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send welcome email", e);
        }
    }

    @Override
    public void sendPasswordResetOtp(PasswordResetOtpRequestedEvent event) {
        try {
            String html = emailTemplateService.buildPasswordResetOtpEmail(event.username(), event.otp());
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    true,
                    StandardCharsets.UTF_8.name()
            );
            helper.setFrom(from);
            helper.setTo(event.email());
            helper.setSubject("Password Reset OTP");
            helper.setText(html, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send password reset OTP", e);
        }
    }

    @Override
    public void sendEmailVerificationOtp(EmailVerificationOtpRequestedEvent event) {
        try {
            String html = emailTemplateService.buildEmailVerificationOtpEmail(event.username(), event.otp());
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    true,
                    StandardCharsets.UTF_8.name()
            );
            helper.setFrom(from);
            helper.setTo(event.email());
            helper.setSubject("Email Verification OTP");
            helper.setText(html, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email verification OTP", e);
        }
    }

    @Override
    public void sendRequestStatusUpdate(RequestStatusUpdatedEvent event) {
        try {
            String html = emailTemplateService.buildAdminAccessRequestStatusEmail(event);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    true,
                    StandardCharsets.UTF_8.name()
            );
            helper.setFrom(from);
            helper.setTo(event.email());
            helper.setSubject(
                    event.status() == RequestStatus.APPROVED
                            ? "Admin Access Request Approved"
                            : "Admin Access Request Rejected"
            );
            helper.setText(html, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send admin access request status email", e);
        }
    }

    @Override
    public void sendAdminAccessRevokedEmail(AdminAccessRevokedEvent event) {
        try {
            String html = emailTemplateService.buildAdminAccessRevokedEmail(event);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    true,
                    StandardCharsets.UTF_8.name()
            );
            helper.setFrom(from);
            helper.setTo(event.email());
            helper.setSubject("Administrator Access Revoked");
            helper.setText(html, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send administrator access revoked email", e);
        }
    }
}