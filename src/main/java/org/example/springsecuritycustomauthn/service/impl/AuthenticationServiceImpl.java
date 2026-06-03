package org.example.springsecuritycustomauthn.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springsecuritycustomauthn.service.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Transactional(readOnly = true)
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final String USERNAME_NOT_BLANK = "Username must not be blank";
    private static final String PASSWORD_NOT_BLANK = "Password must not be blank";

    private final AuthenticationManager authenticationManager;

    @Override
    public void login(String username, String password) {
        Assert.hasText(username, USERNAME_NOT_BLANK);
        Assert.hasText(password, PASSWORD_NOT_BLANK);

        log.debug("Logging in User with username: {}", username);

        Authentication authRequestToken = new UsernamePasswordAuthenticationToken(username, password);

        Authentication authenticatedToken = authenticationManager.authenticate(authRequestToken);

        SecurityContext newContext = SecurityContextHolder.createEmptyContext();

        newContext.setAuthentication(authenticatedToken);

        SecurityContextHolder.setContext(newContext);

        log.debug("User logged in with username: {}", username);
    }
}