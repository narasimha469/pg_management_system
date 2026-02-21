package com.pgmanagement.responseDtos;

import java.time.LocalDateTime;

import com.pgmanagement.enums.Role;
import com.pgmanagement.enums.UserStatus;

import lombok.Data;

@Data
public class UserResponseDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String street;
    private String city;
    private String pincode;

    private Role role;          // Guest or Owner
    private UserStatus status;  // ACTIVE, PENDING_APPROVAL, etc.

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ⚠️ Password is intentionally removed from response
}