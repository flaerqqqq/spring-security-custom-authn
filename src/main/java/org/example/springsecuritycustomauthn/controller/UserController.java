package org.example.springsecuritycustomauthn.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.springsecuritycustomauthn.dto.user.UserDetailsDto;
import org.example.springsecuritycustomauthn.dto.user.UserDetailsResponseDto;
import org.example.springsecuritycustomauthn.dto.user.UserRegisterDto;
import org.example.springsecuritycustomauthn.dto.user.UserRegisterRequestDto;
import org.example.springsecuritycustomauthn.mapper.UserMapper;
import org.example.springsecuritycustomauthn.service.UserAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserAccountService userAccountService;
    private final UserMapper userMapper;

    @PostMapping(
            path = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserDetailsResponseDto> register(
            @Valid @RequestBody UserRegisterRequestDto registerRequestDto
    ) {
        UserRegisterDto registerDto = userMapper.toRegisterDto(registerRequestDto);

        UserDetailsDto registeredUserDetails = userAccountService.register(registerDto);

        UserDetailsResponseDto response = userMapper.toDetailsResponseDto(registeredUserDetails);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}