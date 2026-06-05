package org.example.springsecuritycustomauthn.service;

import org.springframework.security.core.Authentication;

import java.security.Principal;

public interface JwtTokenService {

    String generateAccessToken(Authentication authentication);
}