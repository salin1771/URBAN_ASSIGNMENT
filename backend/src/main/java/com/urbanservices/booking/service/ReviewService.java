package com.urbanservices.booking.service;

import com.urbanservices.booking.dto.ReviewDto;
import com.urbanservices.booking.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService extends BaseService<ReviewDto, com.urbanservices.booking.model.Review, Long> {
    
    Page<ReviewDto> findByProfessionalId(Long professionalId, Pageable pageable);
    
    Page<ReviewDto> findByServiceId(Long serviceId, Pageable pageable);
    
    Page<ReviewDto> findByCustomerId(Long customerId, Pageable pageable);
    
    Page<ReviewDto> findByRating(int rating, Pageable pageable);
    
    Page<ReviewDto> findByProfessionalIdAndRating(Long professionalId, int rating, Pageable pageable);
    
    Page<ReviewDto> findByServiceIdAndRating(Long serviceId, int rating, Pageable pageable);
    
    ReviewDto findByBookingId(Long bookingId) throws ResourceNotFoundException;
    
    boolean hasCustomerReviewedProfessional(Long customerId, Long professionalId);
    
    boolean hasCustomerReviewedService(Long customerId, Long serviceId);
    
    double getAverageRatingByProfessionalId(Long professionalId);
    
    double getAverageRatingByServiceId(Long serviceId);
    
    long countByProfessionalId(Long professionalId);
    
    long countByServiceId(Long serviceId);
    
    List<ReviewDto> findLatestByProfessionalId(Long professionalId, int limit);
    
    List<ReviewDto> findLatestByServiceId(Long serviceId, int limit);
    
    void updateReviewAverages(Long professionalId, Long serviceId);
}
