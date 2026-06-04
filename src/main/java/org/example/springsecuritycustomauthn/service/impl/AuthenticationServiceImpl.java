package org.example.springsecuritycustomauthn.service.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springsecuritycustomauthn.dto.user.UserUsernamePasswordLoginDto;
import org.example.springsecuritycustomauthn.service.AuthenticationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Transactional(readOnly = true)
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    @Override
    public void login(@Valid UserUsernamePasswordLoginDto loginDto) {
        // authentication with jwt response
    }
}