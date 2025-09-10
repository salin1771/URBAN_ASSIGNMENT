package com.urbanservices.booking.model.enums;

public enum BookingStatus {
    PENDING,            // Booking created but not yet confirmed
    CONFIRMED,          // Booking confirmed by the professional
    IN_PROGRESS,        // Service is currently being provided
    COMPLETED,          // Service has been completed
    CANCELLED,          // Booking was cancelled
    REJECTED,           // Professional rejected the booking
    EXPIRED,            // Booking expired before confirmation
    RESCHEDULED         // Booking was rescheduled
}
