package com.example.backend.auth.service.impl;

import com.example.backend.common.event.EventPublisher;
import com.example.backend.common.event.events.EmailVerificationOtpRequestedEvent;
import com.example.backend.common.exception.BaseException;
import com.example.backend.common.exception.ErrorCode;
import com.example.backend.auth.dto.request.VerifyEmailRequest;
import com.example.backend.auth.entity.AuthAccount;
import com.example.backend.auth.entity.EmailVerificationOtp;
import com.example.backend.auth.repository.AuthAccountRepository;
import com.example.backend.auth.repository.EmailVerificationOtpRepository;
import com.example.backend.auth.service.EmailVerificationService;
import com.example.backend.auth.service.helper.OtpGenerator;
import com.example.backend.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {
    private final AuthAccountRepository authAccountRepository;
    private final EmailVerificationOtpRepository emailVerificationRepository;
    private final OtpGenerator otpGenerator;
    private final EmailService emailService;
    private final EventPublisher eventPublisher;

    @Override
    public String sendEmailVerificationOtp(String email) {
        AuthAccount account = authAccountRepository
                .findByEmail(email)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));
        emailVerificationRepository.findByEmail(email)
                .ifPresent(existing -> {
                    if (existing.getCreatedAt().plusSeconds(30).isAfter(LocalDateTime.now())) {
                        throw new BaseException(ErrorCode.RESEND_OTP_TOO_SOON);
                    }
                    emailVerificationRepository.delete(existing);
                });
        EmailVerificationOtp otp = EmailVerificationOtp.builder()
                .email(email)
                .otp(otpGenerator.generateOtp())
                .build();
        emailVerificationRepository.save(otp);
        return otp.getOtp();
//        eventPublisher.publish(new EmailVerificationOtpRequestedEvent(
//                email,
//                account.getUsername(),
//                otp.getOtp()
//        ));
    }

    @Override
    public void verifyEmail(VerifyEmailRequest request) {
        AuthAccount account = authAccountRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));
        EmailVerificationOtp otp = emailVerificationRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));
        if (!otp.getOtp().equals(request.otp())) {
            throw new BaseException(ErrorCode.INVALID_OTP);
        }
        if (otp.getCreatedAt().plusMinutes(5).isBefore(LocalDateTime.now())) {
            emailVerificationRepository.delete(otp);
            throw new BaseException(ErrorCode.EXPIRED_OTP);
        }
        account.setEmailVerified(true);
        authAccountRepository.save(account);
        emailVerificationRepository.delete(otp);
    }
}