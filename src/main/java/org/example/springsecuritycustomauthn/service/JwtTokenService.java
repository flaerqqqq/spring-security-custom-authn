package org.example.springsecuritycustomauthn.service;

import org.springframework.security.core.Authentication;

public interface JwtTokenService {

    String generateAccessToken(Authentication authentication);
}