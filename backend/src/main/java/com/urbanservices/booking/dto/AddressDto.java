package com.urbanservices.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AddressDto extends BaseDto {
    private Long id;
    
    @NotBlank(message = "Address line 1 is required")
    @Size(max = 255, message = "Address line 1 cannot exceed 255 characters")
    private String addressLine1;
    
    @Size(max = 255, message = "Address line 2 cannot exceed 255 characters")
    private String addressLine2;
    
    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City cannot exceed 100 characters")
    private String city;
    
    @NotBlank(message = "State is required")
    @Size(max = 100, message = "State cannot exceed 100 characters")
    private String state;
    
    @NotBlank(message = "Postal code is required")
    @Size(max = 20, message = "Postal code cannot exceed 20 characters")
    private String postalCode;
    
    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country cannot exceed 100 characters")
    private String country;
    
    private boolean isDefault = false;
    
    @Size(max = 50, message = "Label cannot exceed 50 characters")
    private String label;
    
    @Size(max = 500, message = "Instructions cannot exceed 500 characters")
    private String instructions;
    
    private Long customerId;
    
    // Helper method to format full address
    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        sb.append(addressLine1);
        
        if (addressLine2 != null && !addressLine2.trim().isEmpty()) {
            sb.append(", ").append(addressLine2);
        }
        
        sb.append(", ").append(city)
          .append(", ").append(state)
          .append(" ").append(postalCode)
          .append(", ").append(country);
          
        return sb.toString();
    }
}
