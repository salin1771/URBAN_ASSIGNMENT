package com.urbanservices.booking.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "service_addons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceAddon extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private BigDecimal price;
    
    @Column(name = "duration_in_minutes")
    private Integer durationInMinutes = 0; // Additional time this addon requires
    
    @Column(name = "is_active")
    private boolean isActive = true;
    
    public ServiceAddon(Service service, String name, String description, BigDecimal price) {
        this.service = service;
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
