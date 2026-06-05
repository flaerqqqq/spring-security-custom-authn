package org.example.springsecuritycustomauthn.config;

import org.example.springsecuritycustomauthn.properties.JwtProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.converter.RsaKeyConverters;

import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration(proxyBeanMethods = false)
public class RsaKeyConfig {

    @Bean
    public RSAPrivateKey rsaPrivateKey(JwtProperties jwtProperties) throws IOException {
        Resource privateKeyResource = jwtProperties.getPrivateKey();

        return RsaKeyConverters.pkcs8().convert(privateKeyResource.getInputStream());
    }

    @Bean
    public RSAPublicKey rsaPublicKey(JwtProperties jwtProperties) throws IOException {
        Resource publicKeyResource = jwtProperties.getPublicKey();

        return RsaKeyConverters.x509().convert(publicKeyResource.getInputStream());
    }
}