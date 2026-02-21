package com.pgmanagement.controller;

import com.pgmanagement.requestDtos.UserRequestDto;
import com.pgmanagement.responseDtos.UserResponseDto;
import com.pgmanagement.service.UserService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 🔹 Handles user registrations for both Guest and Owner roles
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 📝 Register a new user (Guest or Owner)
     * Guest → auto ACTIVE
     * Owner → PENDING_APPROVAL (requires admin approval to login)
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(
            @Valid @RequestBody UserRequestDto userRequestDto) {

        logger.info("Received registration request for email: {}, role: {}",
                userRequestDto.getEmail(),
                userRequestDto.getRole());

        // Create user via service
        UserResponseDto response = userService.createUser(userRequestDto);

        logger.info("User registered successfully with ID: {}, Role: {}, Status: {}",
                response.getId(),
                response.getRole(),
                response.getStatus());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}