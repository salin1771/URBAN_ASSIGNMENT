package com.urbanservices.booking.repository;

import com.urbanservices.booking.model.Booking;
import com.urbanservices.booking.model.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    Page<Booking> findByCustomerId(Long customerId, Pageable pageable);
    
    Page<Booking> findByProfessionalId(Long professionalId, Pageable pageable);
    
    Page<Booking> findByServiceId(Long serviceId, Pageable pageable);
    
    Page<Booking> findByStatus(BookingStatus status, Pageable pageable);
    
    @Query("SELECT b FROM Booking b WHERE b.customer.id = :customerId AND b.bookingDate >= :startDate AND b.bookingDate < :endDate")
    List<Booking> findCustomerBookingsBetweenDates(
            @Param("customerId") Long customerId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT b FROM Booking b WHERE b.professional.id = :professionalId AND b.bookingDate >= :startDate AND b.bookingDate < :endDate")
    List<Booking> findProfessionalBookingsBetweenDates(
            @Param("professionalId") Long professionalId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT b FROM Booking b WHERE b.professional.id = :professionalId AND " +
           "((b.bookingDate <= :endTime) AND (b.endDate >= :startTime))")
    List<Booking> findConflictingBookings(
            @Param("professionalId") Long professionalId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT b FROM Booking b WHERE b.professional.id = :professionalId AND b.status = 'COMPLETED'")
    Page<Booking> findCompletedBookingsByProfessional(
            @Param("professionalId") Long professionalId,
            Pageable pageable);
            
    @Query("SELECT b FROM Booking b WHERE b.customer.id = :customerId AND b.bookingDate >= :now ORDER BY b.bookingDate ASC")
    Page<Booking> findCustomerUpcomingBookings(
            @Param("customerId") Long customerId,
            @Param("now") LocalDateTime now,
            Pageable pageable);
            
    @Query("SELECT b FROM Booking b WHERE b.professional.id = :professionalId AND b.bookingDate >= :now ORDER BY b.bookingDate ASC")
    Page<Booking> findProfessionalUpcomingBookings(
            @Param("professionalId") Long professionalId,
            @Param("now") LocalDateTime now,
            Pageable pageable);
            
    @Query("SELECT b FROM Booking b WHERE b.customer.id = :customerId AND b.bookingDate < :now")
    Page<Booking> findCustomerPastBookings(
            @Param("customerId") Long customerId,
            @Param("now") LocalDateTime now,
            Pageable pageable);
            
    @Query("SELECT b FROM Booking b WHERE b.professional.id = :professionalId AND b.bookingDate < :now")
    Page<Booking> findProfessionalPastBookings(
            @Param("professionalId") Long professionalId,
            @Param("now") LocalDateTime now,
            Pageable pageable);
            
    @Query("SELECT b FROM Booking b WHERE b.professional.id = :professionalId AND b.bookingDate BETWEEN :startDate AND :endDate")
    List<Booking> findByProfessionalIdAndBookingDateBetween(
            @Param("professionalId") Long professionalId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
            
    @Query("SELECT b FROM Booking b WHERE b.bookingDate BETWEEN :startDate AND :endDate")
    List<Booking> findByBookingDateBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.professional.id = :professionalId AND b.status = 'COMPLETED'")
    Long countCompletedBookingsByProfessional(@Param("professionalId") Long professionalId);
    
    @Query("SELECT b FROM Booking b WHERE b.customer.id = :customerId AND b.status = 'COMPLETED'")
    Page<Booking> findCompletedBookingsByCustomer(
            @Param("customerId") Long customerId,
            Pageable pageable);
    
    @Query("SELECT b FROM Booking b WHERE b.professional.id = :professionalId AND b.bookingDate >= :startDate ORDER BY b.bookingDate ASC")
    List<Booking> findUpcomingBookingsByProfessional(
            @Param("professionalId") Long professionalId,
            @Param("startDate") LocalDateTime startDate);
}
