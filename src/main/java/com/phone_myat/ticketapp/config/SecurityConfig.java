package com.phone_myat.ticketapp.config;

import com.phone_myat.ticketapp.filters.UserProvisioningFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            UserProvisioningFilter userProvisioningFilter) throws Exception {

        http
                .cors(Customizer.withDefaults())

                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/api/v1/auth/login", "/api/v1/auth/refresh", "/api/v1/auth/logout)").permitAll()
                                .anyRequest().authenticated())

                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(Customizer.withDefaults()))

                .addFilterAfter(userProvisioningFilter, BearerTokenAuthenticationFilter.class);

        return http.build();

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration(); // initialize empty cors policy first
        config.setAllowedOrigins(List.of("http://localhost:5173")); // vite server
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // Spring Security needs a place to store CORS rules. | maps URL patterns to CORS configurations.
        source.registerCorsConfiguration("/**", config);
        return source;


        // For configuration like this, you don't intend to modify the list after creating it,
        // so an immutable list from List.of() is a good fit.
    }
}

/*
List.of()
✅ Concise and modern
✅ Immutable (cannot be modified)
✅ Doesn't allow null elements
 */
