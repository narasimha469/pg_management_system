package com.pgmanagement.serviceImpl;

import com.pgmanagement.entity.User;
import com.pgmanagement.enums.Role;
import com.pgmanagement.enums.UserStatus;
import com.pgmanagement.exception.BusinessException;
import com.pgmanagement.repository.UserRepository;
import com.pgmanagement.requestDtos.LoginRequestDto;
import com.pgmanagement.responseDtos.LoginResponseDto;
import com.pgmanagement.security.JwtUtils;
import com.pgmanagement.service.AuthService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public LoginResponseDto login(LoginRequestDto dto) {

        // ✅ Find user by email OR phone
        User user = userRepository.findByEmailOrPhone(dto.getEmailOrPhone(), dto.getEmailOrPhone())
                .orElseThrow(() -> new BusinessException("Invalid credentials"));

        // ✅ Admin can login anytime
        if (user.getRole() != Role.ROLE_ADMIN) {
            // Only ACTIVE users (OWNER/GUEST) can login
            if (user.getStatus() != UserStatus.ACTIVE) {
                throw new BusinessException("User is not active or pending approval");
            }
        }

        // ✅ Check password
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException("Invalid credentials");
        }

        // ✅ Generate JWT token
        String token = jwtUtils.generateToken(user.getEmail(), user.getRole().name());

        // ✅ Return only token (frontend can decode role if needed)
        LoginResponseDto response = new LoginResponseDto();
        response.setToken(token);

        return response;
    }
}