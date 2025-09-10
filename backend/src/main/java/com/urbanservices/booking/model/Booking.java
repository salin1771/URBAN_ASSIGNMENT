package com.urbanservices.booking.model;

import com.urbanservices.booking.model.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_id", nullable = false)
    private Professional professional;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;
    
    @Column(name = "booking_date", nullable = false)
    private LocalDateTime bookingDate;
    
    @Column(name = "end_date")
    private LocalDateTime endDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING;
    
    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;
    
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookingAddon> addons = new HashSet<>();
    
    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private Review review;
    
    @Column(columnDefinition = "TEXT")
    private String specialInstructions;
    
    @Column(name = "cancellation_reason")
    private String cancellationReason;
    
    @Column(name = "cancelled_by")
    private String cancelledBy; // 'CUSTOMER' or 'PROFESSIONAL' or 'SYSTEM'
    
    @Column(name = "rescheduled_from")
    private Long rescheduledFrom; // ID of the previous booking if this is a reschedule
    
    // Helper methods
    public void addAddon(ServiceAddon addon, int quantity) {
        BookingAddon bookingAddon = new BookingAddon(this, addon, quantity);
        addons.add(bookingAddon);
    }
    
    public void removeAddon(BookingAddon bookingAddon) {
        addons.remove(bookingAddon);
        bookingAddon.setBooking(null);
    }
    
    public void setReview(Review review) {
        if (review == null) {
            if (this.review != null) {
                this.review.setBooking(null);
            }
        } else {
            review.setBooking(this);
        }
        this.review = review;
    }
}
