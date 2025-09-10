package com.urbanservices.booking.service;

import com.urbanservices.booking.dto.BookingDto;
import com.urbanservices.booking.exception.BookingConflictException;
import com.urbanservices.booking.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingService extends BaseService<BookingDto, com.urbanservices.booking.model.Booking, Long> {
    
    Page<BookingDto> findByCustomerId(Long customerId, Pageable pageable);
    
    Page<BookingDto> findByProfessionalId(Long professionalId, Pageable pageable);
    
    Page<BookingDto> findByServiceId(Long serviceId, Pageable pageable);
    
    Page<BookingDto> findByStatus(String status, Pageable pageable);
    
    List<BookingDto> findCustomerUpcomingBookings(Long customerId, int limit);
    
    List<BookingDto> findProfessionalUpcomingBookings(Long professionalId, int limit);
    
    List<BookingDto> findCustomerPastBookings(Long customerId, int page, int size);
    
    List<BookingDto> findProfessionalPastBookings(Long professionalId, int page, int size);
    
    BookingDto createBooking(BookingDto bookingDto) throws BookingConflictException, ResourceNotFoundException;
    
    BookingDto rescheduleBooking(Long bookingId, LocalDateTime newDateTime) 
            throws ResourceNotFoundException, BookingConflictException;
    
    void cancelBooking(Long bookingId, String reason) throws ResourceNotFoundException;
    
    void completeBooking(Long bookingId) throws ResourceNotFoundException;
    
    boolean isTimeSlotAvailable(Long professionalId, LocalDateTime startTime, LocalDateTime endTime, Long excludeBookingId);
    
    List<LocalDateTime> findAvailableTimeSlots(Long professionalId, LocalDateTime date, int durationInMinutes);
    
    BigDecimal calculateBookingTotal(BookingDto bookingDto) throws ResourceNotFoundException;
    
    void sendBookingConfirmation(Long bookingId) throws ResourceNotFoundException;
    
    void sendReminderForUpcomingBookings();
}
