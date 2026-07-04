package com.example.backend.email.consumer;

import com.example.backend.common.event.events.PasswordResetOtpRequestedEvent;
import com.example.backend.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordResetOtpRequestedConsumer {
    private final EmailService emailService;

    @Async
    @EventListener
    public void consume(PasswordResetOtpRequestedEvent event) {
        emailService.sendPasswordResetOtp(event);
    }
}
