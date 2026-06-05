package org.example.springsecuritycustomauthn.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import java.util.List;

@ConfigurationProperties(prefix = "app.security.jwt")
@Setter
@Getter
public class JwtProperties {

    private String keyId;

    private String issuer;

    private List<String> audience;

    private long accessTokenExpiration;

    private Resource privateKey;

    private Resource publicKey;
}