package com.example.backend.auth.service.impl;

import com.example.backend.common.enums.RequestStatus;
import com.example.backend.common.enums.Role;
import com.example.backend.common.event.EventPublisher;
import com.example.backend.common.event.events.RequestStatusUpdatedEvent;
import com.example.backend.common.exception.BaseException;
import com.example.backend.common.exception.ErrorCode;
import com.example.backend.common.security.SecurityUtils;
import com.example.backend.auth.dto.response.AdminPromotionRequestResponse;
import com.example.backend.auth.entity.AdminPromotionRequest;
import com.example.backend.auth.entity.AuthAccount;
import com.example.backend.auth.mapper.AdminPromotionRequestMapper;
import com.example.backend.auth.repository.AdminPromotionRequestRepository;
import com.example.backend.auth.repository.AuthAccountRepository;
import com.example.backend.auth.service.AdminPromotionRequestService;
import com.example.backend.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminPromotionRequestServiceImpl implements AdminPromotionRequestService {
    private final AdminPromotionRequestRepository requestRepository;
    private final AuthAccountRepository authAccountRepository;
    private final AdminPromotionRequestMapper adminPromotionRequestMapper;
    private final EmailService emailService;
    private final EventPublisher eventPublisher;

    @Override
    public AdminPromotionRequestResponse createRequest() {
        if (SecurityUtils.getHighestRole() != Role.ROLE_USER) {
            throw new BaseException(ErrorCode.BAD_REQUEST);
        }
        AdminPromotionRequest request = requestRepository.save(
                AdminPromotionRequest.builder()
                        .accountId(SecurityUtils.getAccountId())
                        .username(SecurityUtils.getUsername())
                        .status(RequestStatus.PENDING)
                        .build()
        );
        return adminPromotionRequestMapper.toResponse(request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminPromotionRequestResponse> getAllRequests() {
        System.out.println(SecurityUtils.getAccountId());
        return requestRepository.findAllByAccountIdOrderByCreatedAtDesc(SecurityUtils.getAccountId())
                .stream().map(adminPromotionRequestMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminPromotionRequestResponse> getPendingRequests() {
        return requestRepository
                .findAllByStatusOrderByCreatedAtDesc(RequestStatus.PENDING)
                .stream()
                .map(adminPromotionRequestMapper::toResponse)
                .toList();
    }

    @Override
    public AdminPromotionRequestResponse approveRequest(UUID requestId) {
        AdminPromotionRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));
        AuthAccount account = authAccountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));
        account.getRoles().remove(Role.ROLE_USER);
        account.getRoles().add(Role.ROLE_ADMIN);
        request.setStatus(RequestStatus.APPROVED);
        request.setReviewedBy(SecurityUtils.getUsername());
        eventPublisher.publish(new RequestStatusUpdatedEvent(
                requestId,
                account.getUsername(),
                account.getEmail(),
                RequestStatus.APPROVED,
                request.getUpdatedAt(),
                request.getReviewedBy()
        ));
        return adminPromotionRequestMapper.toResponse(request);
    }

    @Override
    public AdminPromotionRequestResponse rejectRequest(UUID requestId) {
        AdminPromotionRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));
        AuthAccount account = authAccountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));
        request.setStatus(RequestStatus.REJECTED);
        request.setReviewedBy(SecurityUtils.getUsername());
        eventPublisher.publish(new RequestStatusUpdatedEvent(
                requestId,
                account.getUsername(),
                account.getEmail(),
                RequestStatus.REJECTED,
                request.getUpdatedAt(),
                request.getReviewedBy()
        ));
        return adminPromotionRequestMapper.toResponse(request);
    }
}