package com.phone_myat.ticketapp.services;

import com.phone_myat.ticketapp.domain.auth.AuthResult;
import com.phone_myat.ticketapp.domain.dtos.auth.LoginRequestDto;
import com.phone_myat.ticketapp.domain.dtos.auth.LoginResponseDto;

public interface AuthService {

    AuthResult login(LoginRequestDto loginRequestDto);
    AuthResult refresh(String refreshToken);

}
