package org.example.springsecuritycustomauthn.service;

import jakarta.validation.Valid;
import org.example.springsecuritycustomauthn.dto.auth.JwtTokenDto;
import org.example.springsecuritycustomauthn.dto.user.UserUsernamePasswordLoginDto;

public interface AuthenticationService {

    JwtTokenDto login(@Valid UserUsernamePasswordLoginDto loginDto);
}