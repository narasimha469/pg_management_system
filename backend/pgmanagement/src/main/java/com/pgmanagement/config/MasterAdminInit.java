package com.pgmanagement.config;

import com.pgmanagement.entity.User;
import com.pgmanagement.enums.Role;
import com.pgmanagement.enums.UserStatus;
import com.pgmanagement.repository.UserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MasterAdminInit implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.role}")
    private String adminRole;

    public MasterAdminInit(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = User.builder()
                    .firstName("Master")
                    .lastName("Admin")
                    .email(adminEmail)
                    .phone("9999999999") // default dummy phone
                    .password(passwordEncoder.encode(adminPassword))
                    .role(Role.valueOf(adminRole))
                    .status(UserStatus.ACTIVE)
                    .street("Admin Street")
                    .city("Admin City")
                    .pincode("000000")
                    .enabled(true)
                    .accountNonLocked(true)
                    .deleted(false)
                    .build();

            userRepository.save(admin);
            System.out.println("✅ Master Admin created successfully!");
        }
    }
}