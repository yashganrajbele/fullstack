package com.example.backend.common.security.custom;

import com.example.backend.auth.entity.AuthAccount;
import com.example.backend.common.enums.AuthProvider;
import com.example.backend.common.enums.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Comparator;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final AuthAccount authAccount;

    public String getAccountId() {
        return authAccount.getId().toString();
    }

    public String getEmail() {
        return authAccount.getEmail();
    }

    public Role getHighestRole() {
        return authAccount.getRoles()
                .stream()
                .max(Comparator.comparingInt(Role::getLevel))
                .orElse(Role.ROLE_USER);
    }

    public AuthProvider getAuthProvider() {
        return authAccount.getProvider();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authAccount.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.name())).toList();
    }

    @Override
    public String getUsername() {
        return authAccount.getUsername();
    }

    @Override
    public String getPassword() {
        return authAccount.getPassword();
    }

    @Override
    public boolean isEnabled() {
        return authAccount.isEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return authAccount.isAccountNonLocked();
    }

    public boolean isEmailVerified() {
        return authAccount.isEmailVerified();
    }
}
