package com.phone_myat.ticketapp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "keycloak")
@Data
public class KeycloakProperties {

    private String tokenUri;
    private String clientId;

}
