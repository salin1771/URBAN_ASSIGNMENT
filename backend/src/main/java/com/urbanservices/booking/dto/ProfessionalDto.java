package com.urbanservices.booking.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProfessionalDto extends BaseDto {
    private Long id;
    
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number should be valid")
    private String phone;
    
    @Size(max = 1000, message = "Bio cannot exceed 1000 characters")
    private String bio;
    
    @Min(value = 0, message = "Years of experience cannot be negative")
    private Integer yearsOfExperience;
    
    @DecimalMin(value = "0.0", message = "Hourly rate must be a positive number")
    private Double hourlyRate;
    
    private String profileImageUrl;
    private boolean verified = false;
    private boolean available = true;
    
    @NotNull(message = "At least one service must be selected")
    @Size(min = 1, message = "At least one service must be selected")
    private Set<Long> serviceIds;
    
    // Calculated fields
    private Double averageRating;
    private Integer totalReviews;
    private Long totalBookings;
    private Set<ServiceDto> services;
    
    // Address information
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    
    // Helper method to update from entity
    public void updateFromEntity(com.urbanservices.booking.model.Professional professional) {
        this.id = professional.getId();
        this.name = professional.getName();
        this.email = professional.getEmail();
        this.phone = professional.getPhone();
        this.bio = professional.getBio();
        this.yearsOfExperience = professional.getYearsOfExperience();
        this.hourlyRate = professional.getHourlyRate();
        this.profileImageUrl = professional.getProfileImageUrl();
        this.verified = professional.isVerified();
        this.available = professional.isAvailable();
        this.averageRating = professional.getAverageRating();
    }
}
