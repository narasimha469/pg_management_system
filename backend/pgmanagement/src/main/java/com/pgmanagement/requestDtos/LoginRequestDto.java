package com.pgmanagement.requestDtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {

    @NotBlank(message = "Email or Phone is required")
    private String emailOrPhone;

    @NotBlank(message = "Password is required")
    private String password;
}