package com.urbanservices.booking.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "service_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCategory extends BaseEntity {
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private String imageUrl;
    
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Service> services = new HashSet<>();
    
    @Column(nullable = false)
    private boolean active = true;
    
    public ServiceCategory(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
