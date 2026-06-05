package org.example.springsecuritycustomauthn.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.springsecuritycustomauthn.dto.auth.JwtTokenDto;
import org.example.springsecuritycustomauthn.dto.auth.JwtTokenResponseDto;
import org.example.springsecuritycustomauthn.dto.user.UserUsernamePasswordLoginDto;
import org.example.springsecuritycustomauthn.dto.user.UserUsernamePasswordLoginRequestDto;
import org.example.springsecuritycustomauthn.mapper.JwtTokenMapper;
import org.example.springsecuritycustomauthn.mapper.UserMapper;
import org.example.springsecuritycustomauthn.service.AuthenticationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;
    private final JwtTokenMapper jwtTokenMapper;

    @PostMapping(
            path = "/login/password",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<JwtTokenResponseDto> loginWithPassword(
            @Valid @RequestBody UserUsernamePasswordLoginRequestDto requestDto
    ) {
        UserUsernamePasswordLoginDto loginDto = userMapper.toUsernamePasswordLoginDto(requestDto);

        JwtTokenDto jwtTokenDto = authenticationService.login(loginDto);

        JwtTokenResponseDto response = jwtTokenMapper.toResponseDto(jwtTokenDto);

        return ResponseEntity.ok(response);
    }
}