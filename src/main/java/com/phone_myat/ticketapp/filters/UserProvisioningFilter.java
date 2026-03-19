package com.phone_myat.ticketapp.filters;

import com.phone_myat.ticketapp.domain.entities.User;
import org.springframework.security.oauth2.jwt.Jwt;
import com.phone_myat.ticketapp.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserProvisioningFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {


        // Get the authentication object from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof Jwt jwt) {

            // Extract the user ID from the JWT subject
            UUID keycloakId = UUID.fromString(jwt.getSubject());

            if(!userRepository.existsById(keycloakId)) {
                User user = new User();
                user.setId(keycloakId);
                user.setName(jwt.getClaimAsString("preferred_username"));
                user.setEmail(jwt.getClaimAsString("email"));

                userRepository.save(user);
            }
        }

        filterChain.doFilter(request, response);

    }
}

/**
 * ⚠️ Important issue in your code
 * UUID keycloakId = UUID.fromString(jwt.getSubject());
 *
 * 👉 This may crash ❌
 *
 * Why?
 * Keycloak sub is usually a String, not always a UUID
 */
