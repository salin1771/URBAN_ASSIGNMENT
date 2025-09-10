package com.urbanservices.booking.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Service extends BaseEntity {
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private ServiceCategory category;
    
    @Column(nullable = false)
    private BigDecimal basePrice;
    
    private String imageUrl;
    
    private Integer durationInMinutes = 60; // Default duration
    
    @Column(name = "is_active")
    private boolean isActive = true;
    
    @ManyToMany(mappedBy = "services")
    private Set<Professional> professionals = new HashSet<>();
    
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ServiceAddon> addons = new HashSet<>();
    
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Booking> bookings = new HashSet<>();
    
    @Transient
    private Double averageRating;
    
    @Transient
    private Integer totalProfessionals;
    
    public Service(String name, String description, ServiceCategory category, BigDecimal basePrice) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.basePrice = basePrice;
    }
    
    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
    
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    public void setCategory(ServiceCategory category) {
        this.category = category;
    }
    
    public ServiceCategory getCategory() {
        return this.category;
    }
    
    public Long getId() {
        return super.getId();
    }
}
