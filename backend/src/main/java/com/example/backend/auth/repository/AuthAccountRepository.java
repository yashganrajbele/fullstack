package com.example.backend.auth.repository;

import com.example.backend.auth.entity.AuthAccount;
import com.example.backend.common.enums.AuthProvider;
import com.example.backend.common.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthAccountRepository extends JpaRepository<AuthAccount, UUID> {
    Optional<AuthAccount> findByUsernameOrEmail(String username, String email);

    List<AuthAccount> findAllByRolesContaining(Role role);

    Optional<AuthAccount> findByEmail(String email);

    Optional<AuthAccount> findByProviderAndProviderId(AuthProvider provider, String providerId);

    boolean existsByUsername(String username);
}
