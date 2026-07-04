package com.example.backend.email.service.impl;

import com.example.backend.common.enums.RequestStatus;
import com.example.backend.common.event.events.AdminAccessRevokedEvent;
import com.example.backend.common.event.events.RequestStatusUpdatedEvent;
import com.example.backend.email.service.EmailTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EmailTemplateServiceImpl implements EmailTemplateService {
    private final TemplateEngine templateEngine;

    @Override
    public String buildWelcomeEmail(String username) {
        Context context = new Context();

        context.setVariable("username", username);

        return templateEngine.process("welcome", context);
    }

    @Override
    public String buildAdminAccessRequestStatusEmail(RequestStatusUpdatedEvent event) {
        Context context = new Context();

        context.setVariable("username", event.username());
        context.setVariable("requestId", event.requestId());
        context.setVariable("reviewedBy", event.reviewedBy());
        context.setVariable(
                "reviewedAt",
                event.reviewedAt().format(
                        DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")
                )
        );

        String template =
                event.status() == RequestStatus.APPROVED
                        ? "approve"
                        : "reject";

        return templateEngine.process(template, context);
    }

    @Override
    public String buildAdminAccessRevokedEmail(AdminAccessRevokedEvent event) {
        Context context = new Context();

        context.setVariable("username", event.username());
        context.setVariable("revokedBy", event.revokedBy());
        context.setVariable(
                "revokedAt",
                event.revokedAt().format(
                        DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")
                )
        );
        return templateEngine.process("demote", context);
    }

    @Override
    public String buildEmailVerificationOtpEmail(String username, String otp) {
        Context context = new Context();

        context.setVariable("username", username);
        context.setVariable("otp", otp);

        return templateEngine.process("otp", context);
    }

    @Override
    public String buildPasswordResetOtpEmail(String username, String otp) {
        Context context = new Context();

        context.setVariable("username", username);
        context.setVariable("otp", otp);

        return templateEngine.process("otp", context);
    }
}
