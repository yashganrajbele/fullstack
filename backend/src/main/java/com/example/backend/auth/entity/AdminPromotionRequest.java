package com.example.backend.auth.entity;

import com.example.backend.common.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "admin_promotion_requests",
        indexes = {
                @Index(name = "idx_admin_request_account", columnList = "account_id"),
                @Index(name = "idx_admin_request_status", columnList = "status")
        }
//        uniqueConstraints = {
//                @UniqueConstraint(
//                        name = "uq_admin_request_account_status",
//                        columnNames = {"account_id", "status"}
//                )
//        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminPromotionRequest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;
    @Column(nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private RequestStatus status = RequestStatus.PENDING;

    @Column(name = "reviewed_by")
    private String reviewedBy;
}
