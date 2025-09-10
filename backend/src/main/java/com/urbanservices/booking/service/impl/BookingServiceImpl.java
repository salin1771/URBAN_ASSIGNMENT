package com.urbanservices.booking.service.impl;

import java.math.BigDecimal;
import com.urbanservices.booking.dto.BookingDto;
import com.urbanservices.booking.exception.BookingConflictException;
import com.urbanservices.booking.exception.ResourceNotFoundException;
import com.urbanservices.booking.mapper.BookingMapper;
import com.urbanservices.booking.model.Booking;
import com.urbanservices.booking.model.Professional;
import com.urbanservices.booking.model.ServiceAddon;
import com.urbanservices.booking.model.Service;
import com.urbanservices.booking.model.enums.BookingStatus;
import com.urbanservices.booking.repository.BookingRepository;
import com.urbanservices.booking.repository.ProfessionalRepository;
import com.urbanservices.booking.repository.ServiceRepository;
import com.urbanservices.booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
// Using fully qualified name to avoid conflict with Service entity
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class BookingServiceImpl extends BaseServiceImpl<BookingDto, Booking, Long> implements BookingService {

    private final BookingRepository bookingRepository;
    private final ProfessionalRepository professionalRepository;
    private final ServiceRepository serviceRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                            ProfessionalRepository professionalRepository,
                            ServiceRepository serviceRepository,
                            BookingMapper bookingMapper) {
        super(bookingRepository, bookingMapper, Booking.class);
        this.bookingRepository = bookingRepository;
        this.professionalRepository = professionalRepository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookingDto> findByCustomerId(Long customerId, Pageable pageable) {
        return bookingRepository.findByCustomerId(customerId, pageable)
                .map(this::toDtoWithDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookingDto> findByProfessionalId(Long professionalId, Pageable pageable) {
        return bookingRepository.findByProfessionalId(professionalId, pageable)
                .map(this::toDtoWithDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookingDto> findByServiceId(Long serviceId, Pageable pageable) {
        return bookingRepository.findByServiceId(serviceId, pageable)
                .map(this::toDtoWithDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookingDto> findByStatus(String status, Pageable pageable) {
        try {
            BookingStatus bookingStatus = BookingStatus.valueOf(status.toUpperCase());
            return bookingRepository.findByStatus(bookingStatus, pageable)
                    .map(this::toDtoWithDetails);
        } catch (IllegalArgumentException e) {
            return Page.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> findCustomerUpcomingBookings(Long customerId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return bookingRepository.findCustomerUpcomingBookings(customerId, LocalDateTime.now(), pageable)
                .getContent().stream()
                .map(this::toDtoWithDetails)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> findProfessionalUpcomingBookings(Long professionalId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return bookingRepository.findProfessionalUpcomingBookings(professionalId, LocalDateTime.now(), pageable)
                .getContent().stream()
                .map(this::toDtoWithDetails)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> findCustomerPastBookings(Long customerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookingRepository.findCustomerPastBookings(customerId, LocalDateTime.now(), pageable).getContent().stream()
                .map(this::toDtoWithDetails)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> findProfessionalPastBookings(Long professionalId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookingRepository.findProfessionalPastBookings(professionalId, LocalDateTime.now(), pageable).getContent().stream()
                .map(this::toDtoWithDetails)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookingDto createBooking(BookingDto bookingDto) throws BookingConflictException, ResourceNotFoundException {
        // Validate professional exists
        Professional professional = professionalRepository.findById(bookingDto.getProfessionalId())
                .orElseThrow(() -> new ResourceNotFoundException("Professional not found with id: " + bookingDto.getProfessionalId()));
        
        // Validate service exists and get duration
        com.urbanservices.booking.model.Service service = serviceRepository.findById(bookingDto.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + bookingDto.getServiceId()));
        
        // Calculate end time
        LocalDateTime startTime = bookingDto.getBookingDate();
        Integer duration = service.getDurationInMinutes() != null ? service.getDurationInMinutes() : 60; // Default to 60 minutes if null
        LocalDateTime endTime = startTime.plusMinutes(duration);
        
        // Check for time slot availability
        if (!isTimeSlotAvailable(professional.getId(), startTime, endTime, null)) {
            throw new BookingConflictException("The selected time slot is not available");
        }
        
        // Set end time in DTO
        bookingDto.setEndDate(endTime);
        
        // Set initial status
        bookingDto.setStatus(BookingStatus.PENDING);
        
        // Calculate and set total amount
        BigDecimal totalAmount = calculateBookingTotal(bookingDto);
        bookingDto.setTotalAmount(totalAmount);
        
        // Create the booking
        Booking booking = toEntity(bookingDto);
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());
        
        Booking savedBooking = bookingRepository.save(booking);
        
        // Send confirmation email (async)
        sendBookingConfirmation(savedBooking.getId());
        
        return toDtoWithDetails(savedBooking);
    }

    @Override
    @Transactional
    public BookingDto rescheduleBooking(Long bookingId, LocalDateTime newDateTime) 
            throws ResourceNotFoundException, BookingConflictException {
        
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));
        
        // Check if booking can be rescheduled
        if (booking.getStatus() != BookingStatus.PENDING && booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Cannot reschedule a booking that is " + booking.getStatus());
        }
        
        // Calculate new end time
        LocalDateTime newEndTime = newDateTime.plusMinutes(booking.getService().getDurationInMinutes());
        
        // Check for time slot availability
        if (!isTimeSlotAvailable(booking.getProfessional().getId(), newDateTime, newEndTime, bookingId)) {
            throw new BookingConflictException("The selected time slot is not available");
        }
        
        // Update booking times
        booking.setBookingDate(newDateTime);
        booking.setEndDate(newEndTime);
        booking.setStatus(BookingStatus.RESCHEDULED);
        booking.setUpdatedAt(LocalDateTime.now());
        
        Booking updatedBooking = bookingRepository.save(booking);
        
        // Send rescheduling notification (async)
        sendReschedulingNotification(updatedBooking);
        
        return toDtoWithDetails(updatedBooking);
    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingId, String reason) throws ResourceNotFoundException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));
        
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            return; // Already cancelled
        }
        
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancellationReason(reason);
        booking.setUpdatedAt(LocalDateTime.now());
        
        bookingRepository.save(booking);
        
        // Send cancellation notification (async)
        sendCancellationNotification(booking);
    }

    @Override
    @Transactional
    public void completeBooking(Long bookingId) throws ResourceNotFoundException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));
        
        if (booking.getStatus() != BookingStatus.IN_PROGRESS) {
            throw new IllegalStateException("Cannot complete a booking that is " + booking.getStatus());
        }
        
        booking.setStatus(BookingStatus.COMPLETED);
        booking.setUpdatedAt(LocalDateTime.now());
        
        bookingRepository.save(booking);
        
        // Send completion notification (async)
        sendCompletionNotification(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isTimeSlotAvailable(Long professionalId, LocalDateTime startTime, LocalDateTime endTime, Long excludeBookingId) {
        List<Booking> conflictingBookings = bookingRepository.findConflictingBookings(
                professionalId, startTime, endTime);
        
        if (excludeBookingId != null) {
            conflictingBookings.removeIf(booking -> booking.getId().equals(excludeBookingId));
        }
        
        return conflictingBookings.isEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocalDateTime> findAvailableTimeSlots(Long professionalId, LocalDateTime date, int durationInMinutes) {
        // Get professional's working hours for the given date
        // This is a simplified version - in a real app, you'd get this from the professional's schedule
        LocalTime workStart = LocalTime.of(9, 0); // 9 AM
        LocalTime workEnd = LocalTime.of(17, 0);   // 5 PM
        
        // Get existing bookings for the day
        LocalDateTime dayStart = date.with(LocalTime.MIN);
        LocalDateTime dayEnd = date.with(LocalTime.MAX);
        List<Booking> dayBookings = bookingRepository.findByProfessionalIdAndBookingDateBetween(
                professionalId, dayStart, dayEnd);
        
        // Generate time slots
        List<LocalDateTime> availableSlots = new ArrayList<>();
        LocalDateTime slotStart = date.with(workStart);
        LocalDateTime slotEnd = slotStart.plusMinutes(durationInMinutes);
        
        while (slotEnd.isBefore(date.with(workEnd))) {
            boolean isAvailable = true;
            
            // Check against existing bookings
            for (Booking booking : dayBookings) {
                if (isTimeSlotOverlap(slotStart, slotEnd, booking.getBookingDate(), booking.getEndDate())) {
                    isAvailable = false;
                    break;
                }
            }
            
            if (isAvailable) {
                availableSlots.add(slotStart);
            }
            
            // Move to next slot (30-minute intervals)
            slotStart = slotStart.plusMinutes(30);
            slotEnd = slotStart.plusMinutes(durationInMinutes);
        }
        
        return availableSlots;
    }

    @Override
    public void sendBookingConfirmation(Long bookingId) throws ResourceNotFoundException {
        // In a real implementation, this would send an email or notification
        // For now, we'll just log it
        System.out.println("Sending booking confirmation for booking ID: " + bookingId);
    }

    @Scheduled(cron = "0 0 9 * * ?") // Run every day at 9 AM
    public void sendReminderForUpcomingBookings() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1);
        
        // Find all bookings for tomorrow
        List<Booking> tomorrowBookings = bookingRepository.findByBookingDateBetween(
                tomorrow.with(LocalTime.MIN),
                tomorrow.with(LocalTime.MAX)
        );
        
        // Send reminders
        for (Booking booking : tomorrowBookings) {
            sendReminder(booking);
        }
    }
    
    // Helper methods
    private boolean isTimeSlotOverlap(LocalDateTime start1, LocalDateTime end1, 
                                     LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
    
    private BookingDto toDtoWithDetails(Booking booking) {
        BookingDto dto = toDto(booking);
        
        // Add professional details
        if (booking.getProfessional() != null) {
            dto.setProfessionalName(booking.getProfessional().getName());
            dto.setProfessionalRating(booking.getProfessional().getAverageRating());
        }
        
        // Add service details
        if (booking.getService() != null) {
            dto.setServiceName(booking.getService().getName());
            dto.setServiceImageUrl(booking.getService().getImageUrl());
        }
        
        // Add address details
        if (booking.getAddress() != null) {
            dto.setAddressDetails(booking.getAddress().getFullAddress());
        }
        
        return dto;
    }
    
    private void sendReschedulingNotification(Booking booking) {
        // Implementation would send email/notification
        System.out.println("Sending rescheduling notification for booking: " + booking.getId());
    }
    
    private void sendCancellationNotification(Booking booking) {
        // Implementation would send email/notification
        System.out.println("Sending cancellation notification for booking: " + booking.getId());
    }
    
    private void sendCompletionNotification(Booking booking) {
        // Implementation would send email/notification
        System.out.println("Sending completion notification for booking: " + booking.getId());
    }
    
    private void sendReminder(Booking booking) {
        // Implementation would send reminder email/notification
        System.out.println("Sending reminder for booking: " + booking.getId() + " at " + booking.getBookingDate());
    }
    
    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateBookingTotal(BookingDto bookingDto) throws ResourceNotFoundException {
        // Get the base service
        Service service = serviceRepository.findById(bookingDto.getServiceId())
            .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + bookingDto.getServiceId()));
        
        // Start with the base price of the service
        BigDecimal total = service.getBasePrice();
        
        // Add the cost of any selected add-ons
        if (bookingDto.getAddons() != null && !bookingDto.getAddons().isEmpty()) {
            List<ServiceAddon> addons = service.getAddons().stream()
                .filter(addon -> bookingDto.getAddons().stream()
                    .anyMatch(dtoAddon -> dtoAddon.getId().equals(addon.getId())))
                .collect(Collectors.toList());
                
            for (ServiceAddon addon : addons) {
                total = total.add(addon.getPrice());
            }
        }
        
        // Apply duration-based pricing if end date is available
        if (bookingDto.getBookingDate() != null && bookingDto.getEndDate() != null) {
            long hours = Duration.between(bookingDto.getBookingDate(), bookingDto.getEndDate()).toHours();
            if (hours > 1) {
                // For multi-hour bookings, multiply the total by the number of hours
                total = total.multiply(BigDecimal.valueOf(hours));
            }
        }
        
        return total;
    }
}
