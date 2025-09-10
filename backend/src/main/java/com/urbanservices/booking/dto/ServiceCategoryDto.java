package com.urbanservices.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ServiceCategoryDto {
    private Long id;
    
    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Category name cannot exceed 100 characters")
    private String name;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    
    private String imageUrl;
    private boolean active = true;
    
    // Additional fields for frontend
    private Long serviceCount;
    private Long professionalCount;
    
    // Helper method to update from entity
    public void updateFromEntity(com.urbanservices.booking.model.ServiceCategory category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.imageUrl = category.getImageUrl();
        this.active = category.isActive();
    }
}
