package org.example.springsecuritycustomauthn.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.springsecuritycustomauthn.dto.user.UserUsernamePasswordLoginDto;
import org.example.springsecuritycustomauthn.dto.user.UserUsernamePasswordLoginRequestDto;
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

    @PostMapping(
            path = "/login/password",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> loginWithPassword(
            @Valid @RequestBody UserUsernamePasswordLoginRequestDto loginRequestDto,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        UserUsernamePasswordLoginDto loginDto = userMapper.toUsernamePasswordLoginDto(loginRequestDto);
        
        authenticationService.login(loginDto, request, response);

        return ResponseEntity.ok().build();
    }
}