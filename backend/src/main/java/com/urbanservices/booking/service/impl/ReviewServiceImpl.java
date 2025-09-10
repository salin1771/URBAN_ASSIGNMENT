package com.urbanservices.booking.service.impl;

import com.urbanservices.booking.dto.ReviewDto;
import com.urbanservices.booking.exception.ResourceAlreadyExistsException;
import com.urbanservices.booking.exception.ResourceNotFoundException;
import com.urbanservices.booking.model.enums.BookingStatus;
import com.urbanservices.booking.mapper.ReviewMapper;
import com.urbanservices.booking.model.*;
import com.urbanservices.booking.repository.*;
import com.urbanservices.booking.service.ProfessionalService;
import com.urbanservices.booking.service.ReviewService;
import com.urbanservices.booking.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl extends BaseServiceImpl<ReviewDto, Review, Long> implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final ProfessionalService professionalService;
    private final ServiceService serviceService;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository,
                           BookingRepository bookingRepository,
                           ProfessionalService professionalService,
                           ServiceService serviceService,
                           ReviewMapper reviewMapper) {
        super(reviewRepository, reviewMapper, Review.class);
        this.reviewRepository = reviewRepository;
        this.bookingRepository = bookingRepository;
        this.professionalService = professionalService;
        this.serviceService = serviceService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewDto> findByProfessionalId(Long professionalId, Pageable pageable) {
        return reviewRepository.findByProfessionalId(professionalId, pageable)
                .map(this::toDtoWithDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewDto> findByServiceId(Long serviceId, Pageable pageable) {
        return reviewRepository.findByServiceId(serviceId, pageable)
                .map(this::toDtoWithDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewDto> findByCustomerId(Long customerId, Pageable pageable) {
        return reviewRepository.findByCustomerId(customerId, pageable)
                .map(this::toDtoWithDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewDto> findByRating(int rating, Pageable pageable) {
        return reviewRepository.findByRating(rating, pageable)
                .map(this::toDtoWithDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewDto> findByProfessionalIdAndRating(Long professionalId, int rating, Pageable pageable) {
        return reviewRepository.findByProfessionalIdAndRating(professionalId, rating, pageable)
                .map(this::toDtoWithDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewDto> findByServiceIdAndRating(Long serviceId, int rating, Pageable pageable) {
        return reviewRepository.findByServiceIdAndRating(serviceId, rating, pageable)
                .map(this::toDtoWithDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewDto findByBookingId(Long bookingId) throws ResourceNotFoundException {
        return reviewRepository.findByBookingId(bookingId)
                .map(this::toDtoWithDetails)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found for booking ID: " + bookingId));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasCustomerReviewedProfessional(Long customerId, Long professionalId) {
        return reviewRepository.existsByCustomerIdAndProfessionalId(customerId, professionalId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasCustomerReviewedService(Long customerId, Long serviceId) {
        return reviewRepository.existsByCustomerIdAndServiceId(customerId, serviceId);
    }

    @Override
    @Transactional(readOnly = true)
    public double getAverageRatingByProfessionalId(Long professionalId) {
        Double avgRating = reviewRepository.calculateAverageRatingByProfessionalId(professionalId);
        return avgRating != null ? avgRating : 0.0;
    }

    @Override
    @Transactional(readOnly = true)
    public double getAverageRatingByServiceId(Long serviceId) {
        Double avgRating = reviewRepository.calculateAverageRatingByServiceId(serviceId);
        return avgRating != null ? avgRating : 0.0;
    }

    @Override
    @Transactional(readOnly = true)
    public long countByProfessionalId(Long professionalId) {
        return reviewRepository.countByProfessionalId(professionalId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByServiceId(Long serviceId) {
        return reviewRepository.countByServiceId(serviceId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDto> findLatestByProfessionalId(Long professionalId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return reviewRepository.findLatestByProfessionalId(professionalId, pageable)
                .stream()
                .map(this::toDtoWithDetails)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDto> findLatestByServiceId(Long serviceId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return reviewRepository.findLatestByServiceId(serviceId, pageable)
                .stream()
                .map(this::toDtoWithDetails)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateReviewAverages(Long professionalId, Long serviceId) {
        if (professionalId != null) {
            professionalService.updateAverageRating(professionalId);
        }
        if (serviceId != null) {
            serviceService.updateAverageRating(serviceId);
        }
    }

    @Override
    @Transactional
    public ReviewDto create(ReviewDto reviewDto) {
        // Check if review already exists for this booking
        if (reviewDto.getBookingId() != null) {
            Optional<Review> existingReview = reviewRepository.findByBookingId(reviewDto.getBookingId());
            if (existingReview.isPresent()) {
                throw new ResourceAlreadyExistsException("A review already exists for this booking");
            }
        }
        
        // Set the booking, customer, professional, and service
        Booking booking = bookingRepository.findById(reviewDto.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + reviewDto.getBookingId()));
        
        // Validate that the booking is completed
        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new IllegalStateException("Cannot review a booking that is not completed");
        }
        
        // Create the review
        Review review = toEntity(reviewDto);
        review.setBooking(booking);
        review.setCustomer(booking.getCustomer());
        review.setProfessional(booking.getProfessional());
        review.setService(booking.getService());
        
        Review savedReview = reviewRepository.save(review);
        
        // The booking's review field is already set via the review.setBooking(booking) call in the Review entity
        // No need to explicitly set reviewed flag as the presence of the review indicates it's been reviewed
        bookingRepository.save(booking);
        
        // Update the average ratings
        updateReviewAverages(booking.getProfessional().getId(), booking.getService().getId());
        
        return toDtoWithDetails(savedReview);
    }

    @Override
    @Transactional
    public ReviewDto update(Long id, ReviewDto reviewDto) throws ResourceNotFoundException {
        return reviewRepository.findById(id)
                .map(existingReview -> {
                    // Update the review
                    existingReview.setRating(reviewDto.getRating());
                    existingReview.setComment(reviewDto.getComment());
                    existingReview.setAnonymous(reviewDto.isAnonymous());
                    existingReview.setUpdatedAt(java.time.LocalDateTime.now());
                    
                    Review updatedReview = reviewRepository.save(existingReview);
                    
                    // Update the average ratings
                    updateReviewAverages(
                            updatedReview.getProfessional() != null ? updatedReview.getProfessional().getId() : null,
                            updatedReview.getService() != null ? updatedReview.getService().getId() : null
                    );
                    
                    return toDtoWithDetails(updatedReview);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
    }

    @Override
    @Transactional
    public void delete(Long id) throws ResourceNotFoundException {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
        
        Long professionalId = review.getProfessional() != null ? review.getProfessional().getId() : null;
        Long serviceId = review.getService() != null ? review.getService().getId() : null;
        
        // Delete the review
        reviewRepository.delete(review);
        
        // Update the average ratings
        updateReviewAverages(professionalId, serviceId);
    }
    
    private ReviewDto toDtoWithDetails(Review review) {
        ReviewDto dto = toDto(review);
        
        // Add customer details if not anonymous
        if (review.getCustomer() != null && !review.isAnonymous()) {
            dto.setCustomerName(review.getCustomer().getName());
            dto.setCustomerImageUrl(review.getCustomer().getProfileImageUrl());
        } else {
            dto.setCustomerName("Anonymous");
        }
        
        // Add professional details
        if (review.getProfessional() != null) {
            dto.setProfessionalName(review.getProfessional().getName());
        }
        
        // Add service details
        if (review.getService() != null) {
            dto.setServiceName(review.getService().getName());
        }
        
        return dto;
    }
}
