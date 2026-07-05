package com.phone_myat.ticketapp.domain.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {

    @NotBlank
    @Email(message =  "Invalid Format")
    private String email;

    @NotBlank
    private String password;

}
