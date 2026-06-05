package org.example.springsecuritycustomauthn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "org.example.springsecuritycustomauthn.properties")
public class SpringSecurityCustomAuthnApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityCustomAuthnApplication.class, args);
    }

}