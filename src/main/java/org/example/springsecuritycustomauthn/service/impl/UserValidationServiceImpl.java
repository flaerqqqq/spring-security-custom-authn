package org.example.springsecuritycustomauthn.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springsecuritycustomauthn.exception.UserAlreadyExistsException;
import org.example.springsecuritycustomauthn.repository.UserRepository;
import org.example.springsecuritycustomauthn.service.UserValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Transactional(readOnly = true)
@Service
@Slf4j
@RequiredArgsConstructor
public class UserValidationServiceImpl implements UserValidationService {

    private static final String USERNAME_NOT_BLANK = "Username must not be blank";

    private final UserRepository userRepository;

    @Override
    public void throwIfUsernameExists(String username) {
        Assert.hasText(username, USERNAME_NOT_BLANK);

        log.debug("Checking existence for User with username: {}", username);

        if (userRepository.existsByUsername(username)) {
            log.warn("User exists with username: {}", username);

            throw new UserAlreadyExistsException("username", username);
        }
    }
}