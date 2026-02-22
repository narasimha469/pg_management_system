package com.pgmanagement.controller;

import com.pgmanagement.config.PaginationProperties;
import com.pgmanagement.responseDtos.AdminUserDashboardResponse;
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
    
    private final PaginationProperties paginationProperties;

    public AdminController(UserService userService,
            PaginationProperties paginationProperties) {
this.userService = userService;
this.paginationProperties = paginationProperties;
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

    
    
    @GetMapping("/users")
    public ResponseEntity<AdminUserDashboardResponse> getAllUsersWithDashboard(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        int finalPage = (page != null) ? page : paginationProperties.getDefaultPage();
        int finalSize = (size != null) ? size : paginationProperties.getDefaultSize();

        // Prevent large data attacks
        if (finalSize > paginationProperties.getMaxSize()) {
            finalSize = paginationProperties.getMaxSize();
        }

        AdminUserDashboardResponse response =
                userService.getAllUsersWithDashboard(finalPage, finalSize);

        return ResponseEntity.ok(response);
    }
}