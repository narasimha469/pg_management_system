package com.pgmanagement.controller;

import com.pgmanagement.requestDtos.LoginRequestDto;
import com.pgmanagement.responseDtos.LoginResponseDto;
import com.pgmanagement.service.AuthService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 🔐 User Login (Admin / Guest / Owner)
     * Accepts email or phone + password
     * Returns JWT token only
     */
    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto requestDto) {

        // Authenticate user and generate token
        LoginResponseDto response = authService.login(requestDto);

        // ✅ Token contains role, frontend can use it to determine dashboard

        return response;
    }
}