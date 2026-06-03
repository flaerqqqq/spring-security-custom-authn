package org.example.springsecuritycustomauthn.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springsecuritycustomauthn.dto.user.UserUsernamePasswordLoginDto;
import org.example.springsecuritycustomauthn.service.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

@Validated
@Transactional(readOnly = true)
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final String USER_USERNAME_PASSWORD_LOGIN_DTO_NOT_NULL = "UserUsernamePasswordLoginDto must not be null";

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    @Override
    public void login(@Valid UserUsernamePasswordLoginDto loginDto,
                      HttpServletRequest request,
                      HttpServletResponse response) {
        Assert.notNull(loginDto, USER_USERNAME_PASSWORD_LOGIN_DTO_NOT_NULL);

        String username = loginDto.getUsername();
        String password = loginDto.getPassword();

        log.debug("Logging in User with username: {}", username);

        Authentication authRequestToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticatedToken = authenticationManager.authenticate(authRequestToken);

        SecurityContext newContext = SecurityContextHolder.createEmptyContext();
        newContext.setAuthentication(authenticatedToken);
        SecurityContextHolder.setContext(newContext);
        securityContextRepository.saveContext(newContext, request, response);

        log.debug("User logged in with username: {}", username);
    }
}