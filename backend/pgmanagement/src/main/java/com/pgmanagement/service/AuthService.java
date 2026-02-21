package com.pgmanagement.service;

import com.pgmanagement.requestDtos.LoginRequestDto;
import com.pgmanagement.responseDtos.LoginResponseDto;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto dto);
}