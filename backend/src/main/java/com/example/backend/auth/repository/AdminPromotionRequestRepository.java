package com.example.backend.auth.repository;

import com.example.backend.auth.entity.AdminPromotionRequest;
import com.example.backend.common.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdminPromotionRequestRepository extends JpaRepository<AdminPromotionRequest, UUID> {
    List<AdminPromotionRequest> findAllByStatusOrderByCreatedAtDesc(RequestStatus status);
    List<AdminPromotionRequest> findAllByAccountIdOrderByCreatedAtDesc(UUID accountId);
    List<AdminPromotionRequest> deleteAllByAccountId(UUID accountId);
}
