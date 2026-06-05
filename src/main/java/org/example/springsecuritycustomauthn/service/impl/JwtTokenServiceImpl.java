package org.example.springsecuritycustomauthn.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springsecuritycustomauthn.properties.JwtProperties;
import org.example.springsecuritycustomauthn.security.userdetails.SecurityUser;
import org.example.springsecuritycustomauthn.service.JwtTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService{

    private static final String AUTHENTICATION_NOT_NULL = "Authentication should not be null";

    private final JwtEncoder jwtEncoder;
    private final JwtProperties jwtProperties;

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public String generateAccessToken(Authentication authentication) {
        Assert.notNull(authentication, AUTHENTICATION_NOT_NULL);

        JwsHeader jwsHeader = JwsHeader.with(SignatureAlgorithm.RS256)
                .keyId("loca-dev-key-1")
                .build();

        JwtClaimsSet jwtClaimsSet = buildClaims(authentication);

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(jwsHeader, jwtClaimsSet);

        return jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }

    private JwtClaimsSet buildClaims(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof SecurityUser su) {

            Instant issuedAt = Instant.now();
            Instant expiresAt = issuedAt.plusMillis(jwtProperties.getAccessTokenExpiration());

            return JwtClaimsSet.builder()
                    .id(UUID.randomUUID().toString())
                    .issuedAt(issuedAt)
                    .expiresAt(expiresAt)
                    .subject(su.getPublicId().toString())
                    .issuer(applicationName)
                    .audience(List.of(applicationName))
                    .build();
        } else {
            throw new RuntimeException("Authentication principal is not of type SecurityUser");
        }
    }
}