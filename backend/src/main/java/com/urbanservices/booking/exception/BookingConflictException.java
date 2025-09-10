package com.urbanservices.booking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class BookingConflictException extends RuntimeException {
    public BookingConflictException(String message) {
        super(message);
    }

    public BookingConflictException(String resourceName, String conflictReason) {
        super(String.format("Conflict with %s: %s", resourceName, conflictReason));
    }
}
