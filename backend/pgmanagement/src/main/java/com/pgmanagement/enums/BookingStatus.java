package com.pgmanagement.enums;

public enum BookingStatus {

    INITIATED,          // Booking created
    PENDING_PAYMENT,    // Waiting for payment
    CONFIRMED,          // Payment successful → Auto confirmed
    CANCELLED,          // Cancelled by user/admin
    COMPLETED,          // Stay completed
    FAILED              // Payment failed
}