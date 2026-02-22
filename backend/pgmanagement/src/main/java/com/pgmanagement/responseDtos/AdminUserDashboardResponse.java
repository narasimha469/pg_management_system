package com.pgmanagement.responseDtos;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDashboardResponse {

    // 🔹 Paginated user list
    private List<UserResponseDto> users;

    // 🔹 Pagination info
    private long totalUsers;
    private int totalPages;
    private int currentPage;
    private int pageSize;

    // 🔹 Role Counts
    private Map<String, Long> roleCounts;

    // 🔹 Status Counts
    private long activeUsers;
    private long pendingUsers;
    private long suspendedUsers;
    private long deletedUsers;

   
}