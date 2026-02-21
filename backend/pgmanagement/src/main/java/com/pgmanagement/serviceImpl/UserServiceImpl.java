package com.pgmanagement.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgmanagement.entity.User;
import com.pgmanagement.enums.Role;
import com.pgmanagement.enums.UserStatus;
import com.pgmanagement.exception.BusinessException;
import com.pgmanagement.exception.DuplicateResourceException;
import com.pgmanagement.mapper.UserMapper;
import com.pgmanagement.repository.UserRepository;
import com.pgmanagement.requestDtos.UserRequestDto;
import com.pgmanagement.responseDtos.UserResponseDto;
import com.pgmanagement.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDto createUser(UserRequestDto dto) {

        String email = dto.getEmail().trim().toLowerCase();
        String phone = dto.getPhone().trim();

        logger.info("Registering user with email: {}", email);

        // ✅ Duplicate checks
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Email already exists");
        }
        if (userRepository.existsByPhone(phone)) {
            throw new DuplicateResourceException("Phone number already exists");
        }

        // Map DTO to Entity
        User user = UserMapper.toEntity(dto);

        // ✅ Determine role and status
        if (dto.getRole() == null || dto.getRole() == Role.ROLE_GUEST) {
            // Guest → auto ACTIVE
            user.setRole(Role.ROLE_GUEST);
            user.setStatus(UserStatus.ACTIVE);
        } else if (dto.getRole() == Role.ROLE_OWNER) {
            // Owner → pending admin approval
            user.setRole(Role.ROLE_OWNER);
            user.setStatus(UserStatus.PENDING_APPROVAL);
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(dto.getPassword().trim()));

        User savedUser = userRepository.save(user);

        logger.info("User registered successfully with ID: {}, Role: {}, Status: {}",
                savedUser.getId(), savedUser.getRole(), savedUser.getStatus());

        return UserMapper.toResponseDto(savedUser);
    }
    
    
    
    @Override
    public UserResponseDto approveOwner(Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new BusinessException("Owner not found with ID: " + ownerId));

        if (!owner.getRole().name().equals("ROLE_OWNER")) {
            throw new BusinessException("User is not an owner");
        }

        if (owner.getStatus() != UserStatus.PENDING_APPROVAL) {
            throw new BusinessException("Owner is already approved or inactive");
        }

        owner.setStatus(UserStatus.ACTIVE);
        User savedOwner = userRepository.save(owner);

        return UserMapper.toResponseDto(savedOwner);
    }
}