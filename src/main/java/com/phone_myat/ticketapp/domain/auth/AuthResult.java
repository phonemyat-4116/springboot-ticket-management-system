package com.phone_myat.ticketapp.domain.auth;

import com.phone_myat.ticketapp.domain.dtos.auth.LoginResponseDto;
import lombok.Value;

@Value
public class AuthResult {

    private LoginResponseDto loginResponseDto;
    private String refreshToken;
    private Long refreshExpiresIn;

}

/*
This class is for:
(internal service-layer type)

Not public facing json
 */
