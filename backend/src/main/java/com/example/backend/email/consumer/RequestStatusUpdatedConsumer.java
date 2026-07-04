package com.example.backend.email.consumer;

import com.example.backend.common.event.events.RequestStatusUpdatedEvent;
import com.example.backend.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestStatusUpdatedConsumer {
    private final EmailService emailService;

    @Async
    @EventListener
    public void consume(RequestStatusUpdatedEvent event) {
        emailService.sendRequestStatusUpdate(event);
    }
}
