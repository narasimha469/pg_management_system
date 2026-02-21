package com.pgmanagement.controller;

import com.pgmanagement.responseDtos.UserResponseDto;
import com.pgmanagement.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    /**
     * ✅ Approve a pending owner registration
     * Only accessible by ADMIN role
     *
     * @param ownerId - ID of the owner to approve
     * @return UserResponseDto of the approved owner
     */
    @PutMapping("/approve-owner/{ownerId}")
    public ResponseEntity<UserResponseDto> approveOwner(@PathVariable Long ownerId) {
        logger.info("Admin requested approval for owner with ID: {}", ownerId);

        UserResponseDto approvedOwner = userService.approveOwner(ownerId);

        logger.info("Owner with ID: {} approved successfully", ownerId);

        return new ResponseEntity<>(approvedOwner, HttpStatus.OK);
    }

    // ✅ Optional: You can add more admin endpoints here like
    // get all pending owners, suspend user, delete user, etc.
}