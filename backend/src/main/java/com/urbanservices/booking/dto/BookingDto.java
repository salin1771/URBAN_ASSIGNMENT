package com.urbanservices.booking.dto;

import com.urbanservices.booking.model.enums.BookingStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class BookingDto extends BaseDto {
    private Long id;
    
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    @NotNull(message = "Professional ID is required")
    private Long professionalId;
    
    @NotNull(message = "Service ID is required")
    private Long serviceId;
    
    @NotNull(message = "Address ID is required")
    private Long addressId;
    
    @NotNull(message = "Booking date and time is required")
    @Future(message = "Booking date must be in the future")
    private LocalDateTime bookingDate;
    
    private LocalDateTime endDate;
    
    @NotNull(message = "Status is required")
    private BookingStatus status;
    
    @NotNull(message = "Total amount is required")
    @Positive(message = "Total amount must be positive")
    private BigDecimal totalAmount;
    
    private Set<BookingAddonDto> addons;
    
    private String specialInstructions;
    private String cancellationReason;
    private String cancelledBy;
    private Long rescheduledFrom;
    
    // Additional fields for display
    private String customerName;
    private String professionalName;
    private String serviceName;
    private String serviceImageUrl;
    private String addressDetails;
    private Double professionalRating;
    private Integer professionalReviewCount;
    
    // Helper method to calculate end date based on service duration
    public void calculateEndDate(int serviceDurationInMinutes) {
        if (this.bookingDate != null && serviceDurationInMinutes > 0) {
            this.endDate = this.bookingDate.plusMinutes(serviceDurationInMinutes);
        }
    }
}
