package org.example.springsecuritycustomauthn.security.userdetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springsecuritycustomauthn.model.entity.User;
import org.example.springsecuritycustomauthn.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Transactional(readOnly = true)
@Service
@Slf4j
@RequiredArgsConstructor
public class DatabaseUserDetailsService implements UserDetailsService {

    private static final String USERNAME_NOT_BLANK = "Username must not be blank";

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Assert.hasText(username, USERNAME_NOT_BLANK);

        log.debug("Loading User with username: {}", username);

        User foundUser = userRepository.findByUsername(username).orElseThrow(() -> {
            log.debug("User not found with username: {}", username);

            return new UsernameNotFoundException("User not found with username: " + username);
        });

        log.debug("User found with username: {}", username);

        return new SecurityUser(foundUser);
    }
}