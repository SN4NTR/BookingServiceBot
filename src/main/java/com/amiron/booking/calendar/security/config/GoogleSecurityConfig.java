package com.amiron.booking.calendar.security.config;

import com.amiron.booking.core.util.SecurityUtils;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.PrivateKey;

/**
 * @author Aliaksandr Miron
 */
@Configuration
public class GoogleSecurityConfig {

    @SneakyThrows
    @Bean
    public GoogleCredential googleCredentials(final GoogleCredentialsProperties credentialsProperties) {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        final JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
        final PrivateKey privateKey = SecurityUtils.getPrivateKeyFromPkcs8(credentialsProperties.getPrivateKey());
        return new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jacksonFactory)
                .setServiceAccountId(credentialsProperties.getClientEmail())
                .setServiceAccountPrivateKey(privateKey)
                .setServiceAccountPrivateKeyId(credentialsProperties.getPrivateKeyId())
                .setServiceAccountProjectId(credentialsProperties.getProjectId())
                .setTokenServerEncodedUrl(credentialsProperties.getTokenUri())
                .setServiceAccountScopes(credentialsProperties.getScopes())
                .build();
    }
}
