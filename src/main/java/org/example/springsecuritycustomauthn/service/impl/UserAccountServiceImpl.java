package org.example.springsecuritycustomauthn.service.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springsecuritycustomauthn.dto.user.UserDetailsDto;
import org.example.springsecuritycustomauthn.dto.user.UserRegisterDto;
import org.example.springsecuritycustomauthn.exception.RoleNotFoundException;
import org.example.springsecuritycustomauthn.mapper.UserMapper;
import org.example.springsecuritycustomauthn.model.entity.Role;
import org.example.springsecuritycustomauthn.model.entity.User;
import org.example.springsecuritycustomauthn.model.enums.RoleName;
import org.example.springsecuritycustomauthn.model.enums.UserStatus;
import org.example.springsecuritycustomauthn.repository.RoleRepository;
import org.example.springsecuritycustomauthn.repository.UserRepository;
import org.example.springsecuritycustomauthn.service.UserAccountService;
import org.example.springsecuritycustomauthn.service.UserValidationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

@Transactional(readOnly = true)
@Validated
@Service
@Slf4j
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    private static final String USER_REGISTER_DTO_NOT_NULL = "UserRegisterDto must not be null";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserValidationService userValidationService;

    @Transactional
    @Override
    public UserDetailsDto register(@Valid UserRegisterDto registerDto) {
        Assert.notNull(registerDto, USER_REGISTER_DTO_NOT_NULL);

        String username = registerDto.getUsername();

        log.debug("Registering User with username: {}", username);

        userValidationService.throwIfUsernameExists(username);

        Role userRole = getRoleByName(RoleName.USER);

        User user = userMapper.toEntity(registerDto);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.addRole(userRole);
        user.setStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);

        log.info( "Registered User with username={}, and publicId={}", username, savedUser.getPublicId());

        return userMapper.toDetailsDto(savedUser);
    }

    private Role getRoleByName(RoleName name) {
        return roleRepository.findByName(name).orElseThrow(() -> {
            log.warn("Role not found with name: {}", name);

            return new RoleNotFoundException("name", name);
        });
    }
}