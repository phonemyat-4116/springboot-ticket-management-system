package com.phone_myat.ticketapp.filters;

import com.phone_myat.ticketapp.domain.entities.User;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class UserProvisioningFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        // Get the authentication object from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof Jwt jwt) {

            try{

                provisionUserIfAbsent(jwt);

            }catch (Exception ex){

                // Never block the request - provisioning failure should not cause 500
                log.error("UserProvisioningFilter: unexpected error during provisioning, continuing chain", ex);
            }

        }
        filterChain.doFilter(request, response);
    }

    private void provisionUserIfAbsent(Jwt jwt) {
        String subject = jwt.getSubject();

        if(subject == null || subject.isBlank()){
            log.warn("UserProvisioningFilter: JWT has no subject claim, skipping provisioning");
            return;
        }

        UUID keycloakUserId;
        try{
            keycloakUserId = UUID.fromString(subject);
        }catch(IllegalArgumentException ex){
            log.warn("UserProvisioningFilter: JWT subject '{}' is not a valid UUID, skipping provisioning", subject);
            return;
        }

        String email = jwt.getClaim("email");
        String username = jwt.getClaim("preferred_username");

        if(username == null || email == null){
            log.warn("UserProvisioningFilter: JWT missing email or preferred_username for subject '{}', skipping provisioning", keycloakUserId);
        }

        userRepository.upsertUser(keycloakUserId, username, email);
        log.debug("UserProvisioningFilter: provisioned user '{}'", keycloakUserId);

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // Skip provisioning for paths that don't need an authenticated user
        return path.startsWith("/actuator")
                || path.startsWith("/public")
                || path.startsWith("/error");
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

/*
authentication.getPrincipal() returns the currently authenticated principal (identity)
It could be - Jwt (JWT Resource Server)
            - UserDetail (formLogin) or CustomUserDetail
            - DefaultOAuth2User or OidcUser (OAuth2 Login)
            - anonymousUser (if no authentication exists)
 */

/*
Why not just cast directly?
Suppose you wrote:

Jwt jwt = (Jwt) authentication.getPrincipal();

If getPrincipal() actually returns something else, such as:
- UserDetails
- OidcUser
- "anonymousUser" (a String)

then Java throws:
 java.lang.ClassCastException:
 class java.lang.String cannot be cast to class org.springframework.security.oauth2.jwt.Jwt
-------

Using instanceof avoids that:

if (authentication.getPrincipal() instanceof Jwt jwt) {
    // Safe to use jwt here
}

If the principal is not a Jwt, the condition is simply false, and the block is skipped
 */
