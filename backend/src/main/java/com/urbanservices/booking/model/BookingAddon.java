package com.urbanservices.booking.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "booking_addons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingAddon extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addon_id", nullable = false)
    private ServiceAddon addon;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(name = "price_at_booking", nullable = false)
    private BigDecimal priceAtBooking;
    
    @Column(name = "name_at_booking")
    private String nameAtBooking;
    
    public BookingAddon(Booking booking, ServiceAddon addon, int quantity) {
        this.booking = booking;
        this.addon = addon;
        this.quantity = quantity;
        this.priceAtBooking = addon.getPrice();
        this.nameAtBooking = addon.getName();
    }
}
