package com.phone_myat.ticketapp.controller;

import com.phone_myat.ticketapp.domain.auth.AuthResult;
import com.phone_myat.ticketapp.domain.dtos.auth.LoginRequestDto;
import com.phone_myat.ticketapp.domain.dtos.auth.LoginResponseDto;
import com.phone_myat.ticketapp.exceptions.InvalidCredentialsException;
import com.phone_myat.ticketapp.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final String REFRESH_COOKIE_NAME = "refresh_token";
    private static final String REFRESH_COOKIE_PATH = "/api/v1/auth";

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody
                                                  @Valid LoginRequestDto loginRequestDto) {

        AuthResult result = authService.login(loginRequestDto); // will give access_token and refresh_token
        ResponseCookie cookie = setRefreshCookie(result.getRefreshToken(), result.getRefreshExpiresIn());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString()) // cookie part
                .body(result.getLoginResponseDto()); // response part
        
    }

    @PostMapping("/refresh")                                                        // required=false means "It's okay if this cookie doesn't exist. It will become null"
    public ResponseEntity<LoginResponseDto> refresh(@CookieValue(value = REFRESH_COOKIE_NAME, required = false) String refreshToken){

        if (refreshToken == null){
            throw new InvalidCredentialsException("No active session");
        }

        AuthResult result = authService.refresh(refreshToken);
        ResponseCookie cookie = setRefreshCookie(result.getRefreshToken(), result.getRefreshExpiresIn());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(result.getLoginResponseDto());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(){

        ResponseCookie cookie = ResponseCookie.from(REFRESH_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(false) // in production, this should be true
                .sameSite("Lax")
                .path(REFRESH_COOKIE_PATH) // using path means "From browser side, Only send this cookie when making requests to URLs under this path."
                .maxAge(0)
                .build();

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    public ResponseCookie setRefreshCookie(String refreshToken, Long maxAgeSecond){

        return ResponseCookie.from(REFRESH_COOKIE_NAME, refreshToken)
                .httpOnly(true)
                .secure(false) // can send over both http and https
                .sameSite("Lax") //block CSRF
                .path(REFRESH_COOKIE_PATH)
                .maxAge(maxAgeSecond != null ? maxAgeSecond : 1800)
                .build();

    }
}

/*
In this, we didn't use HttpServletResponse response, we just wrote with ResponseEntity
 */
