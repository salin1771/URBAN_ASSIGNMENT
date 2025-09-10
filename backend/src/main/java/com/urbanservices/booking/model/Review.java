package com.urbanservices.booking.model;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseEntity {
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_id", nullable = false)
    private Professional professional;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;
    
    @Column(nullable = false)
    private Integer rating; // 1-5 stars
    
    @Column(columnDefinition = "TEXT")
    private String comment;
    
    @Column(name = "is_anonymous")
    private boolean isAnonymous = false;
    
    @Column(name = "admin_reviewed")
    private boolean adminReviewed = false;
    
    // Helper method
    public void setBooking(Booking booking) {
        this.booking = booking;
        if (booking != null) {
            this.customer = booking.getCustomer();
            this.professional = booking.getProfessional();
            this.service = booking.getService();
        }
    }
}
