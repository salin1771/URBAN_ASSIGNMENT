package com.urbanservices.booking.repository;

import com.urbanservices.booking.model.Professional;
import com.urbanservices.booking.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    Page<Review> findByProfessional(Professional professional, Pageable pageable);
    
    @Query("SELECT r FROM Review r WHERE r.professional.id = :professionalId")
    Page<Review> findByProfessionalId(@Param("professionalId") Long professionalId, Pageable pageable);
    
    @Query("SELECT r FROM Review r WHERE r.service.id = :serviceId")
    Page<Review> findByServiceId(@Param("serviceId") Long serviceId, Pageable pageable);
    
    @Query("SELECT r FROM Review r WHERE r.customer.id = :customerId")
    Page<Review> findByCustomerId(@Param("customerId") Long customerId, Pageable pageable);
    
    @Query("SELECT r FROM Review r WHERE r.booking.id = :bookingId")
    Optional<Review> findByBookingId(@Param("bookingId") Long bookingId);
    
    @Query("SELECT COUNT(r) > 0 FROM Review r WHERE r.customer.id = :customerId AND r.professional.id = :professionalId")
    boolean existsByCustomerIdAndProfessionalId(@Param("customerId") Long customerId, @Param("professionalId") Long professionalId);
    
    @Query("SELECT COUNT(r) > 0 FROM Review r WHERE r.customer.id = :customerId AND r.service.id = :serviceId")
    boolean existsByCustomerIdAndServiceId(@Param("customerId") Long customerId, @Param("serviceId") Long serviceId);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.professional.id = :professionalId")
    Double calculateAverageRatingByProfessionalId(@Param("professionalId") Long professionalId);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.service.id = :serviceId")
    Double calculateAverageRatingByServiceId(@Param("serviceId") Long serviceId);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.professional.id = :professionalId")
    long countByProfessionalId(@Param("professionalId") Long professionalId);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.service.id = :serviceId")
    long countByServiceId(@Param("serviceId") Long serviceId);
    
    @Query("SELECT r FROM Review r WHERE r.professional.id = :professionalId ORDER BY r.createdAt DESC")
    List<Review> findLatestReviewsByProfessionalId(@Param("professionalId") Long professionalId, Pageable pageable);
    
    @Query("SELECT r FROM Review r WHERE r.service.id = :serviceId ORDER BY r.createdAt DESC")
    List<Review> findLatestReviewsByServiceId(@Param("serviceId") Long serviceId, Pageable pageable);
    
    @Query("SELECT r FROM Review r WHERE r.rating = :rating")
    Page<Review> findByRating(@Param("rating") int rating, Pageable pageable);
    
    @Query("SELECT r FROM Review r WHERE r.professional.id = :professionalId AND r.rating = :rating")
    Page<Review> findByProfessionalIdAndRating(
            @Param("professionalId") Long professionalId,
            @Param("rating") int rating,
            Pageable pageable);
    
    @Query("SELECT r FROM Review r WHERE r.service.id = :serviceId AND r.rating = :rating")
    Page<Review> findByServiceIdAndRating(
            @Param("serviceId") Long serviceId,
            @Param("rating") int rating,
            Pageable pageable);
    @Query("SELECT r FROM Review r WHERE r.professional.id = :professionalId ORDER BY r.createdAt DESC")
    Page<Review> findLatestByProfessionalId(@Param("professionalId") Long professionalId, Pageable pageable);
    
    @Query("SELECT r FROM Review r WHERE r.service.id = :serviceId ORDER BY r.createdAt DESC")
    Page<Review> findLatestByServiceId(@Param("serviceId") Long serviceId, Pageable pageable);
}
