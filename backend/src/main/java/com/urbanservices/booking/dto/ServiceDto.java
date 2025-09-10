package com.urbanservices.booking.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceDto extends BaseDto {
    private Long id;
    
    @NotBlank(message = "Service name is required")
    @Size(max = 100, message = "Service name cannot exceed 100 characters")
    private String name;
    
    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    
    @NotNull(message = "Category is required")
    private Long categoryId;
    
    private String categoryName;
    
    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.0", message = "Price cannot be negative")
    private BigDecimal basePrice;
    
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer durationInMinutes = 60;
    
    private String imageUrl;
    
    private boolean active = true;
    
    // Calculated fields
    private Double averageRating;
    private Long totalProfessionals;
    private Long totalBookings;
    
    // Helper method to update from entity
    public void updateFromEntity(com.urbanservices.booking.model.Service service) {
        this.id = service.getId();
        this.name = service.getName();
        this.description = service.getDescription();
        this.basePrice = service.getBasePrice();
        this.durationInMinutes = service.getDurationInMinutes();
        this.imageUrl = service.getImageUrl();
        this.active = service.isActive();
        
        if (service.getCategory() != null) {
            this.categoryId = service.getCategory().getId();
            this.categoryName = service.getCategory().getName();
        }
    }
}
