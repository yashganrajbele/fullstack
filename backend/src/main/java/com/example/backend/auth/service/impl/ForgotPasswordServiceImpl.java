package com.example.backend.auth.service.impl;

import com.example.backend.common.event.EventPublisher;
import com.example.backend.common.event.events.PasswordResetOtpRequestedEvent;
import com.example.backend.common.exception.BaseException;
import com.example.backend.common.exception.ErrorCode;
import com.example.backend.auth.dto.request.ResetPasswordRequest;
import com.example.backend.auth.entity.AuthAccount;
import com.example.backend.auth.entity.PasswordResetOtp;
import com.example.backend.auth.repository.AuthAccountRepository;
import com.example.backend.auth.repository.PasswordResetOtpRepository;
import com.example.backend.auth.service.ForgotPasswordService;
import com.example.backend.auth.service.helper.OtpGenerator;
import com.example.backend.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ForgotPasswordServiceImpl implements ForgotPasswordService {
    private final AuthAccountRepository authAccountRepository;
    private final PasswordResetOtpRepository passwordResetOtpRepository;
    private final OtpGenerator otpGenerator;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final EventPublisher eventPublisher;

    @Override
    public void sendPasswordResetOtp(String email) {
        AuthAccount account = authAccountRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));
        if (account.getPassword() == null) {
            throw new BaseException(ErrorCode.ACCOUNT_USES_GOOGLE_LOGIN);
        }
        passwordResetOtpRepository.findByEmail(email)
                .ifPresent(existing -> {
                    if (existing.getCreatedAt().plusSeconds(30).isAfter(LocalDateTime.now())) {
                        throw new BaseException(ErrorCode.RESEND_OTP_TOO_SOON);
                    }
                    passwordResetOtpRepository.delete(existing);
                });
        PasswordResetOtp passwordResetOtp = PasswordResetOtp.builder()
                .email(email)
                .otp(otpGenerator.generateOtp())
                .build();
        passwordResetOtpRepository.save(passwordResetOtp);
        eventPublisher.publish(new PasswordResetOtpRequestedEvent(
                email,
                account.getUsername(),
                passwordResetOtp.getOtp()
        ));
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        AuthAccount account = authAccountRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));
        PasswordResetOtp otp = passwordResetOtpRepository
                        .findByEmail(request.email())
                        .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));
        if (!otp.getOtp().equals(request.otp())) {
            throw new BaseException(ErrorCode.INVALID_OTP);
        }
        if (otp.getCreatedAt().plusMinutes(5).isBefore(LocalDateTime.now())) {
            passwordResetOtpRepository.delete(otp);
            throw new BaseException(ErrorCode.EXPIRED_OTP);
        }
        account.setPassword(passwordEncoder.encode(request.newPassword()));
        account.setEmailVerified(true);
        authAccountRepository.save(account);
        passwordResetOtpRepository.delete(otp);
    }
}