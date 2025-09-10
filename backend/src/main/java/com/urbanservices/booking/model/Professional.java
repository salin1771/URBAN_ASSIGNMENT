package com.urbanservices.booking.model;

import com.urbanservices.booking.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "professionals")
@Getter
@Setter
@NoArgsConstructor
public class Professional extends User {
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;
    
    @Column(columnDefinition = "TEXT")
    private String bio;
    
    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;
    
    @Column(name = "hourly_rate")
    private Double hourlyRate;
    
    @Column(name = "is_verified")
    private boolean isVerified = false;
    
    @Column(name = "is_available")
    private boolean isAvailable = true;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "professional_services",
        joinColumns = @JoinColumn(name = "professional_id"),
        inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private Set<Service> services = new HashSet<>();
    
    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Booking> bookings = new HashSet<>();
    
    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProfessionalDocument> documents = new HashSet<>();
    
    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Availability> availabilities = new HashSet<>();
    
    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Review> reviews = new HashSet<>();
    
    @Transient
    private Double averageRating;
    
    @Transient
    private Integer totalReviews;
    
    public Professional(String name, String email, String password, String phone) {
        super(name, email, password, phone, UserRole.ROLE_PROFESSIONAL, true);
    }
}
