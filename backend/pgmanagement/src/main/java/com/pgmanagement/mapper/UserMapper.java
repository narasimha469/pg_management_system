package com.pgmanagement.mapper;

import com.pgmanagement.entity.User;
import com.pgmanagement.requestDtos.UserRequestDto;
import com.pgmanagement.responseDtos.UserResponseDto;

public final class UserMapper {

    // Prevent instantiation
    private UserMapper() {}

    
    public static User toEntity(UserRequestDto dto) {
        if (dto == null) return null;

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setPassword(dto.getPassword());  // Encode in service
        user.setStreet(dto.getStreet());
        user.setCity(dto.getCity());
        user.setPincode(dto.getPincode());

           if (dto.getRole() != null) {
            user.setRole(dto.getRole());
        }

        return user;
    }

    /**
     * Convert User entity to UserResponseDto.
     * Excludes sensitive data like password.
     */
    public static UserResponseDto toResponseDto(User user) {
        if (user == null) return null;

        UserResponseDto response = new UserResponseDto();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setStreet(user.getStreet());
        response.setCity(user.getCity());
        response.setPincode(user.getPincode());
        response.setStatus(user.getStatus());
        response.setRole(user.getRole());  // Important for frontend logic
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());

        return response;
    }
}