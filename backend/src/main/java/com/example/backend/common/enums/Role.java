package com.example.backend.common.enums;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_USER(1),
    ROLE_ADMIN(2),
    ROLE_SUPER_ADMIN(3);

    private final int level;

    Role(int level) {
        this.level = level;
    }
}