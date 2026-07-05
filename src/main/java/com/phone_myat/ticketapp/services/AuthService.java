package com.phone_myat.ticketapp.services;

import com.phone_myat.ticketapp.domain.dtos.auth.LoginRequestDto;
import com.phone_myat.ticketapp.domain.dtos.auth.LoginResponseDto;

public interface AuthService {

    LoginResponseDto login(LoginRequestDto loginRequestDto);
}
