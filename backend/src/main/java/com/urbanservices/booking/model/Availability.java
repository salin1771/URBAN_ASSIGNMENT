package com.urbanservices.booking.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "availabilities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Availability extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_id", nullable = false)
    private Professional professional;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;
    
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
    
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
    
    @Column(name = "is_recurring", nullable = false)
    private boolean isRecurring = true;
    
    @Column(name = "specific_date")
    private LocalDate specificDate; // For one-time availability overrides
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    public Availability(Professional professional, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.professional = professional;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    public boolean isAvailable(LocalDateTime dateTime) {
        if (isRecurring) {
            return dateTime.getDayOfWeek() == dayOfWeek &&
                   !dateTime.toLocalTime().isBefore(startTime) &&
                   !dateTime.toLocalTime().isAfter(endTime);
        } else {
            return specificDate != null &&
                   dateTime.toLocalDate().isEqual(specificDate) &&
                   !dateTime.toLocalTime().isBefore(startTime) &&
                   !dateTime.toLocalTime().isAfter(endTime);
        }
    }
}
