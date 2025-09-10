package com.urbanservices.booking.repository;

import com.urbanservices.booking.model.Professional;
import com.urbanservices.booking.model.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfessionalRepository extends BaseRepository<Professional, Long> {
    
    @Query("SELECT p FROM Professional p JOIN p.services s WHERE s.id = :serviceId")
    Page<Professional> findByServiceId(@Param("serviceId") Long serviceId, Pageable pageable);
    
    @Query("SELECT p FROM Professional p WHERE p.isAvailable = true")
    Page<Professional> findAvailableProfessionals(Pageable pageable);
    
    @Query("SELECT p FROM Professional p JOIN p.services s WHERE s.id = :serviceId AND p.isAvailable = true")
    Page<Professional> findAvailableProfessionalsByService(@Param("serviceId") Long serviceId, Pageable pageable);
    
    @Query("SELECT p FROM Professional p WHERE p.isVerified = true")
    Page<Professional> findVerifiedProfessionals(Pageable pageable);
    
    @Query("SELECT p.services FROM Professional p WHERE p.id = :professionalId")
    List<Service> findServicesByProfessionalId(@Param("professionalId") Long professionalId);
    
    @Query("SELECT p, AVG(r.rating) as avgRating FROM Professional p LEFT JOIN p.reviews r GROUP BY p HAVING AVG(r.rating) IS NOT NULL ORDER BY avgRating DESC")
    Page<Object[]> findTopRated(Pageable pageable);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.professional.id = :professionalId")
    Double calculateAverageRating(@Param("professionalId") Long professionalId);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.professional.id = :professionalId")
    Long countReviews(@Param("professionalId") Long professionalId);
    
    @Query("SELECT p FROM Professional p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.email) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Professional> search(@Param("query") String query, Pageable pageable);
    
    @Query("SELECT p FROM Professional p JOIN p.bookings b GROUP BY p ORDER BY COUNT(b) DESC")
    List<Professional> findTopBooked(@Param("limit") int limit, Pageable pageable);
    
    @Query(value = "SELECT p.* FROM professionals p " +
            "JOIN professional_services ps ON p.id = ps.professional_id " +
            "WHERE ps.service_id = :serviceId " +
            "AND (6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) * " +
            "cos(radians(p.longitude) - radians(:longitude)) + sin(radians(:latitude)) * " +
            "sin(radians(p.latitude)))) <= :radiusInKm", nativeQuery = true)
    List<Professional> findNearbyProfessionals(
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("radiusInKm") Double radiusInKm,
            @Param("serviceId") Long serviceId
    );
}
