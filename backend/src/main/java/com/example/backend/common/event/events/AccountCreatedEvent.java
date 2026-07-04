package com.example.backend.common.event.events;

public record AccountCreatedEvent(
        String email,
        String username
) {
}
