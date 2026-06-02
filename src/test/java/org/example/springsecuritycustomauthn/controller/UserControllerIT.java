package org.example.springsecuritycustomauthn.controller;

import org.example.springsecuritycustomauthn.config.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerIT extends AbstractIntegrationTest {

    @Test
    void register_RegistersUserAndReturnsOk_WhenUserDoesNotExistByUsername() throws Exception {
        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MEDIA_JSON)
                        .accept(MEDIA_JSON)
                        .content(objectMapper.writeValueAsString(getUserRegisterRequestDto())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(USER_USERNAME));
    }
}