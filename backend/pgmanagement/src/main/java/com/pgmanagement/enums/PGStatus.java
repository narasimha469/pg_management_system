package com.pgmanagement.enums;

public enum PGStatus {

    PENDING_APPROVAL,  // Admin must verify PG
    APPROVED,          // Visible to guests
    REJECTED,          // Admin rejected
    SUSPENDED          // Temporarily blocked
}