package org.example.springsecuritycustomauthn.service;

import jakarta.validation.Valid;
import org.example.springsecuritycustomauthn.dto.user.UserUsernamePasswordLoginDto;

public interface AuthenticationService {

    void login(@Valid UserUsernamePasswordLoginDto loginDto);
}