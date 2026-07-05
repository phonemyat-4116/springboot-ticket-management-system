package com.phone_myat.ticketapp.controller;

import com.phone_myat.ticketapp.domain.dtos.auth.LoginRequestDto;
import com.phone_myat.ticketapp.domain.dtos.auth.LoginResponseDto;
import com.phone_myat.ticketapp.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody
                                                  @Valid LoginRequestDto loginRequestDto) {

        LoginResponseDto loginResponseDto = authService.login(loginRequestDto);

        return ResponseEntity.ok(loginResponseDto);
        
    }
}
