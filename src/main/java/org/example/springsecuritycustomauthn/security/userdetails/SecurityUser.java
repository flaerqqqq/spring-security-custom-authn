package org.example.springsecuritycustomauthn.security.userdetails;

import org.example.springsecuritycustomauthn.model.entity.User;
import org.example.springsecuritycustomauthn.model.enums.UserStatus;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.Collection;

public class SecurityUser implements UserDetails {

    private static final String USER_NOT_NULL = "User must not be null";

    private final User user;

    public SecurityUser(User user) {
        Assert.notNull(user, USER_NOT_NULL);

        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> "ROLE_" + role.getName().toString())
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getStatus() != UserStatus.LOCKED;
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus() != UserStatus.DISABLED;
    }
}