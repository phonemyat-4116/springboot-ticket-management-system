package com.phone_myat.ticketapp.domain.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDto {

    private String token;
    private String id;
    private String role;
    private String name;
    private String email;
    private Long expiresIn;
}
