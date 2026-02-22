package com.pgmanagement.serviceImpl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.pgmanagement.responseDtos.AdminUserDashboardResponse;
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
    
    
    @Override
    public AdminUserDashboardResponse getAllUsersWithDashboard(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        // ✅ Fetch users with business-priority sorting
        Page<User> userPage = userRepository.findAllOrderByStatusPriority(pageable);

        List<UserResponseDto> userDtos = new ArrayList<>();

        for (User user : userPage.getContent()) {
            userDtos.add(UserMapper.toResponseDto(user));
        }

        // ✅ Role Counts
        List<Object[]> roleCountList = userRepository.countUsersByRole();
        Map<String, Long> roleCounts = new HashMap<>();

        for (Object[] obj : roleCountList) {
            roleCounts.put(obj[0].toString(), (Long) obj[1]);
        }

        // ✅ Status Counts
        List<Object[]> statusCountList = userRepository.countUsersByStatus();

        long activeCount = 0;
        long pendingCount = 0;
        long suspendedCount = 0;
        long deletedCount = 0;

        for (Object[] obj : statusCountList) {

            String status = obj[0].toString();
            Long count = (Long) obj[1];

            if (status.equals("ACTIVE")) {
                activeCount = count;
            } else if (status.equals("PENDING_APPROVAL")) {
                pendingCount = count;
            } else if (status.equals("SUSPENDED")) {
                suspendedCount = count;
            } else if (status.equals("DELETED")) {
                deletedCount = count;
            }
        }

        // ✅ Build response
        AdminUserDashboardResponse response = new AdminUserDashboardResponse();

        response.setUsers(userDtos);
        response.setTotalUsers(userPage.getTotalElements());
        response.setTotalPages(userPage.getTotalPages());
        response.setCurrentPage(userPage.getNumber());
        response.setPageSize(userPage.getSize());
        response.setRoleCounts(roleCounts);

        response.setActiveUsers(activeCount);
        response.setPendingUsers(pendingCount);
        response.setSuspendedUsers(suspendedCount);
        response.setDeletedUsers(deletedCount);

        return response;
    }
}