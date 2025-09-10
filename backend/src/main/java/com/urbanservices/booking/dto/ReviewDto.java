package com.urbanservices.booking.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReviewDto extends BaseDto {
    private Long id;
    
    @NotNull(message = "Booking ID is required")
    private Long bookingId;
    
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot be more than 5")
    private Integer rating;
    
    @Size(max = 1000, message = "Comment cannot exceed 1000 characters")
    private String comment;
    
    private boolean anonymous = false;
    
    // Additional fields for display
    private LocalDateTime createdAt;
    private String customerName;
    private String customerImageUrl;
    private String professionalName;
    private String serviceName;
    
    // Helper method to get the appropriate display name
    public String getDisplayName() {
        return anonymous ? "Anonymous" : customerName;
    }
}
