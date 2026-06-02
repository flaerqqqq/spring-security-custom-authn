package org.example.springsecuritycustomauthn.config;

import org.example.springsecuritycustomauthn.dto.user.UserRegisterRequestDto;
import org.springframework.http.MediaType;

public class TestFactory {

    public final String MEDIA_JSON = MediaType.APPLICATION_JSON_VALUE;

    public final String USER_USERNAME = "username";
    public final String USER_PASSWORD = "username";

    public UserRegisterRequestDto getUserRegisterRequestDto() {
        return UserRegisterRequestDto.builder()
                .username(USER_USERNAME)
                .password(USER_PASSWORD)
                .build();
    }
}