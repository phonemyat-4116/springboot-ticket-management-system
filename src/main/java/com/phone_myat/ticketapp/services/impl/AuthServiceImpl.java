package com.phone_myat.ticketapp.services.impl;

import com.phone_myat.ticketapp.config.KeycloakProperties;
import com.phone_myat.ticketapp.domain.dtos.KeycloakTokenResponseDto;
import com.phone_myat.ticketapp.domain.dtos.auth.LoginRequestDto;
import com.phone_myat.ticketapp.domain.dtos.auth.LoginResponseDto;
import com.phone_myat.ticketapp.exceptions.InvalidCredentialsException;
import com.phone_myat.ticketapp.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Base64;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RestClient restClient;
    private final KeycloakProperties keycloakProperties;
    private final ObjectMapper objectMapper;


    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        // Build body to connect keycloak and get token
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", keycloakProperties.getClientId());
        formData.add("username", loginRequestDto.getEmail()); // ✅ sends email as the username value
        formData.add("password", loginRequestDto.getPassword());

        // Keycloak call
        KeycloakTokenResponseDto tokenResponse;
        try{
            tokenResponse = restClient
                    .post()
                    .uri(keycloakProperties.getTokenUri())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formData)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError,
                            (req, res) -> {
                        throw new InvalidCredentialsException("Invalid email or password");
                    })
                    .body(KeycloakTokenResponseDto.class); // 	Convert the JSON response into a KeycloakTokenResponseDto domain object
        }
        catch (InvalidCredentialsException e){
            throw e;
        }
        catch (Exception e){
            throw new RuntimeException("Failed to reach Keycloak: " + e.getMessage());
        }


        // Decode JWT Payload
        try{
            assert tokenResponse != null;
            String payload = tokenResponse.getAccessToken().split("\\.")[1];
            String decoded = new String(Base64.getDecoder().decode(payload));

            // Base64.getDecoder().decode(payload)
            JsonNode claims = objectMapper.readTree(decoded);

            String name = claims.path("name").asText(loginRequestDto.getEmail());
            String email = claims.path("email").asText(loginRequestDto.getEmail());
            String role = extractRole(claims);

            return new LoginResponseDto(tokenResponse.getAccessToken(), role, name, email);

        }
        catch (Exception e){
            throw new RuntimeException("Failed to decode token: " + e.getMessage());
        }


    }


    private String extractRole(JsonNode claims) {

        JsonNode roles = claims.path("realm_access").path("roles");
        if(roles.isArray()){
            for(JsonNode r : roles){
                String role = r.asText().toUpperCase();
                if(role.equals("ORGANIZER") || role.equals("STAFF")){
                    return role;
                }
            }
        }

        return "ORGANIZER";// default role

    }
}
