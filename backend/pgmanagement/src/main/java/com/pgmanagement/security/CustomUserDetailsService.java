package com.pgmanagement.security;

import com.pgmanagement.entity.User;
import com.pgmanagement.repository.UserRepository;
import com.pgmanagement.exception.BusinessException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String emailOrPhone) {
        User user = userRepository.findByEmailOrPhone(emailOrPhone, emailOrPhone)
                .orElseThrow(() -> new BusinessException("User not found with email/phone: " + emailOrPhone));

        if (user.getStatus() != com.pgmanagement.enums.UserStatus.ACTIVE) {
            throw new BusinessException("User is not active or pending approval");
        }

        return new CustomUserDetails(user);
    }
}