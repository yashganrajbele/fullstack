package com.example.backend.auth.service.helper;

import com.example.backend.common.enums.Role;
import com.example.backend.common.exception.BaseException;
import com.example.backend.common.exception.ErrorCode;
import com.example.backend.common.security.custom.CustomUserDetails;
import com.example.backend.auth.entity.AuthAccount;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.UUID;

@Component
public class AccountManagementValidator {
    public void validateManagePermission(CustomUserDetails actor, AuthAccount target) {
        if (UUID.fromString(actor.getAccountId()).equals(target.getId())) {
            throw new BaseException(ErrorCode.FORBIDDEN);
        }
        Role actorRole = actor.getHighestRole();
        Role targetRole = getHighestRole(target);
        if (actorRole.getLevel() <= targetRole.getLevel()) {
            throw new BaseException(ErrorCode.FORBIDDEN);
        }
    }

    private Role getHighestRole(AuthAccount account) {
        return account.getRoles()
                .stream()
                .max(Comparator.comparingInt(Role::getLevel))
                .orElse(Role.ROLE_USER);
    }
}