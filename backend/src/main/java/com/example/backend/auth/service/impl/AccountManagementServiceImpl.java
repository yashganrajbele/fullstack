package com.example.backend.auth.service.impl;

import com.example.backend.common.enums.Role;
import com.example.backend.common.event.EventPublisher;
import com.example.backend.common.event.events.AdminAccessRevokedEvent;
import com.example.backend.common.exception.BaseException;
import com.example.backend.common.exception.ErrorCode;
import com.example.backend.common.security.SecurityUtils;
import com.example.backend.auth.dto.request.StatusUpdateRequest;
import com.example.backend.auth.dto.response.AccountResponse;
import com.example.backend.auth.dto.response.CurrentUserResponse;
import com.example.backend.auth.entity.AuthAccount;
import com.example.backend.auth.mapper.AuthMapper;
import com.example.backend.auth.repository.AdminPromotionRequestRepository;
import com.example.backend.auth.repository.AuthAccountRepository;
import com.example.backend.auth.service.AccountManagementService;
import com.example.backend.auth.service.helper.AccountManagementValidator;
import com.example.backend.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountManagementServiceImpl implements AccountManagementService {
    private final AuthAccountRepository authAccountRepository;
    private final AdminPromotionRequestRepository adminPromotionRequestRepository;
    private final AuthMapper authMapper;
    private final AccountManagementValidator validator;
    private final EmailService emailService;
    private final EventPublisher eventPublisher;

    @Override
    @Transactional(readOnly = true)
    public CurrentUserResponse getCurrentUser() {
        return authMapper.toResponse(SecurityUtils.getCurrentUser());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponse> getAllAdmins() {
        return authAccountRepository.findAllByRolesContaining(Role.ROLE_ADMIN)
                .stream()
                .map(authMapper::toResponse)
                .toList();
    }

    @Override
    public AccountResponse demoteAdmin(UUID accountId) {
        adminPromotionRequestRepository.deleteAllByAccountId(accountId);
        AuthAccount target = authAccountRepository.findById(accountId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));
        if (!target.getRoles().contains(Role.ROLE_ADMIN)) {
            throw new BaseException(ErrorCode.BAD_REQUEST);
        }
        validator.validateManagePermission(SecurityUtils.getCurrentUser(), target);

        target.getRoles().remove(Role.ROLE_ADMIN);
        target.getRoles().add(Role.ROLE_USER);
        AuthAccount savedAccount = authAccountRepository.save(target);
        eventPublisher.publish(new AdminAccessRevokedEvent(
                savedAccount.getEmail(),
                savedAccount.getUsername(),
                SecurityUtils.getUsername(),
                savedAccount.getUpdatedAt()
        ));
        return authMapper.toResponse(savedAccount);
    }

    @Override
    public AccountResponse updateStatus(UUID accountId, StatusUpdateRequest request) {
        AuthAccount targetAccount = authAccountRepository.findById(accountId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        validator.validateManagePermission(SecurityUtils.getCurrentUser(), targetAccount);

        if (request.enabled() != null) {
            targetAccount.setEnabled(request.enabled());
        }
        if (request.accountNonLocked() != null) {
            targetAccount.setAccountNonLocked(request.accountNonLocked());
        }
        return authMapper.toResponse(targetAccount);
    }
}
