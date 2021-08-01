package com.amiron.booking.calendar.security.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Aliaksandr Miron
 */
@ConfigurationProperties("google.credentials")
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@Data
public class GoogleCredentialsProperties {

    private String privateKey;

    private String privateKeyId;

    private String tokenUri;

    private String authUri;

    private String clientEmail;

    private String clientId;

    private String projectId;

    private List<String> scopes;
}
