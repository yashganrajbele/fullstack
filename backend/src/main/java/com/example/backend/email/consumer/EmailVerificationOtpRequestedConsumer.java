package com.example.backend.email.consumer;

import com.example.backend.common.event.events.EmailVerificationOtpRequestedEvent;
import com.example.backend.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailVerificationOtpRequestedConsumer {
    private final EmailService emailService;

    @Async
    @EventListener
    public void consume(EmailVerificationOtpRequestedEvent event) {
        emailService.sendEmailVerificationOtp(event);
    }
}

