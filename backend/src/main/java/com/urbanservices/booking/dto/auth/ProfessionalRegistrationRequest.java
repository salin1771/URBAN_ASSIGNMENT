package com.urbanservices.booking.dto.auth;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Set;

@Data
public class ProfessionalRegistrationRequest {
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
        message = "Password must contain at least one digit, one lowercase, one uppercase letter, one special character and no whitespace"
    )
    private String password;
    
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number should be valid")
    private String phone;
    
    @Size(max = 1000, message = "Bio should not exceed 1000 characters")
    private String bio;
    
    @Min(value = 0, message = "Years of experience cannot be negative")
    private Integer yearsOfExperience;
    
    @NotNull(message = "At least one service must be selected")
    @Size(min = 1, message = "At least one service must be selected")
    private Set<Long> serviceIds;
    
    @DecimalMin(value = "0.0", message = "Hourly rate must be a positive number")
    private Double hourlyRate;
}
