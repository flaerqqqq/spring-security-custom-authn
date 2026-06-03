package org.example.springsecuritycustomauthn.controller;

import org.example.springsecuritycustomauthn.config.AbstractIntegrationTest;
import org.example.springsecuritycustomauthn.dto.user.UserUsernamePasswordLoginRequestDto;
import org.example.springsecuritycustomauthn.security.userdetails.SecurityUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(
        scripts = {
                "/sql/auth/cleanup.sql",
                "/sql/auth/users.sql",
                "/sql/auth/users-roles.sql"
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
public class AuthenticationControllerIT extends AbstractIntegrationTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void loginWithPassword_ReturnsOkAndSecurityContextHasAuthentication_WhenCorrectCredentialsProvided() throws Exception {
        UserUsernamePasswordLoginRequestDto loginRequestDto = new UserUsernamePasswordLoginRequestDto(
                "username",
                "password"
        );

        MvcResult result = mockMvc.perform(post("/api/v1/auth/login/password")
                        .contentType(MEDIA_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);

        assertThat(session).isNotNull();

        SecurityContext securityContext = (SecurityContext) session.getAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY
        );

        assertThat(securityContext).isNotNull();

        Authentication authentication = securityContext.getAuthentication();

        assertThat(authentication)
                .isNotNull()
                .extracting(Authentication::isAuthenticated)
                .isEqualTo(true);

        Object principal = authentication.getPrincipal();

        assertThat(principal)
                .isInstanceOfSatisfying(SecurityUser.class, securityUser -> {
                    assertThat(securityUser.getUsername()).isEqualTo("username");
                    assertThat(passwordEncoder.matches("password", securityUser.getPassword()))
                            .isTrue();
                });
    }

    @Test
    void logout_ClearsSecurityContextAndInvalidatesHttpSession() throws Exception {
        UserUsernamePasswordLoginRequestDto loginRequestDto = new UserUsernamePasswordLoginRequestDto(
                "username",
                "password"
        );

        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login/password")
                        .contentType(MEDIA_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);

        assertThat(session).isNotNull();

        mockMvc.perform(post("/api/v1/auth/logout")
                        .session(session))
                .andExpect(status().isNoContent())
                .andReturn();

        assertThat(session.isInvalid()).isTrue();
    }
}