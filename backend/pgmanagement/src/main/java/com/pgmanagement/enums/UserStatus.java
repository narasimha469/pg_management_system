package com.pgmanagement.enums;

public enum UserStatus {

    ACTIVE,              // Can use system
    PENDING_APPROVAL,    // Waiting for admin approval
    SUSPENDED,           // Blocked by admin
    DELETED              // Soft deleted
}