package org.example.springsecuritycustomauthn.service;

import jakarta.validation.Valid;
import org.example.springsecuritycustomauthn.dto.user.UserDetailsDto;
import org.example.springsecuritycustomauthn.dto.user.UserRegisterDto;

public interface UserAccountService {

    UserDetailsDto register(@Valid UserRegisterDto registerDto);
}