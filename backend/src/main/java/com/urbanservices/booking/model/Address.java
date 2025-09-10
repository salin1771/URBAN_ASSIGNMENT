package com.urbanservices.booking.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @Column(nullable = false)
    private String addressLine1;
    
    private String addressLine2;
    
    @Column(nullable = false)
    private String city;
    
    @Column(nullable = false)
    private String state;
    
    @Column(nullable = false)
    private String postalCode;
    
    @Column(nullable = false)
    private String country;
    
    @Column(name = "is_default")
    private boolean isDefault = false;
    
    private String label; // e.g., Home, Work, etc.
    
    @Column(columnDefinition = "TEXT")
    private String instructions; // Special instructions for delivery
    
    public String getFullAddress() {
        StringBuilder addressBuilder = new StringBuilder();
        
        if (addressLine1 != null && !addressLine1.isEmpty()) {
            addressBuilder.append(addressLine1);
        }
        
        if (addressLine2 != null && !addressLine2.isEmpty()) {
            if (addressBuilder.length() > 0) addressBuilder.append(", ");
            addressBuilder.append(addressLine2);
        }
        
        if (city != null && !city.isEmpty()) {
            if (addressBuilder.length() > 0) addressBuilder.append(", ");
            addressBuilder.append(city);
        }
        
        if (state != null && !state.isEmpty()) {
            if (addressBuilder.length() > 0) addressBuilder.append(", ");
            addressBuilder.append(state);
        }
        
        if (postalCode != null && !postalCode.isEmpty()) {
            if (addressBuilder.length() > 0) addressBuilder.append(" ");
            addressBuilder.append(postalCode);
        }
        
        if (country != null && !country.isEmpty()) {
            if (addressBuilder.length() > 0) addressBuilder.append(", ");
            addressBuilder.append(country);
        }
        
        return addressBuilder.toString();
    }
    
    // Getters and setters are handled by Lombok
}
