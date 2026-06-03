package org.example.springsecuritycustomauthn.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.example.springsecuritycustomauthn.dto.user.UserUsernamePasswordLoginDto;

public interface AuthenticationService {

    void login(@Valid UserUsernamePasswordLoginDto loginDto, HttpServletRequest request, HttpServletResponse response);
}