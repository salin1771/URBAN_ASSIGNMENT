package com.urbanservices.booking.repository;

import com.urbanservices.booking.model.Service;
import com.urbanservices.booking.model.ServiceCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends BaseRepository<Service, Long> {
    
    @Query("SELECT s FROM Service s WHERE s.category = :category AND s.isActive = true")
    Page<Service> findByCategory(@Param("category") ServiceCategory category, Pageable pageable);
    
    Page<Service> findByIsActive(boolean isActive, Pageable pageable);
    
    @Query("SELECT s FROM Service s WHERE s.isActive = true AND (LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(s.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Service> search(@Param("query") String query, Pageable pageable);
    
    @Query("SELECT s FROM Service s WHERE s.isActive = true AND s.category.id = :categoryId")
    Page<Service> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);
    
    @Query("SELECT s FROM Service s JOIN s.professionals p WHERE p.id = :professionalId")
    Page<Service> findByProfessionalId(@Param("professionalId") Long professionalId, Pageable pageable);
    
    @Query("SELECT DISTINCT s FROM Service s JOIN s.professionals p WHERE p.id IN :professionalIds AND s.isActive = true")
    List<Service> findDistinctByProfessionalIds(@Param("professionalIds") List<Long> professionalIds);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.service.id = :serviceId")
    Double calculateAverageRating(@Param("serviceId") Long serviceId);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.service.id = :serviceId")
    Long countReviews(@Param("serviceId") Long serviceId);
    
    @Query("SELECT s FROM Service s LEFT JOIN Booking b ON s.id = b.service.id " +
           "WHERE s.isActive = true " +
           "GROUP BY s.id " +
           "ORDER BY COUNT(b.id) DESC")
    List<Service> findTopBookedServices(@Param("limit") int limit, Pageable pageable);
    
    @Query("SELECT s, AVG(r.rating) as avgRating FROM Service s " +
           "LEFT JOIN s.bookings b " +
           "LEFT JOIN b.review r " +
           "WHERE s.isActive = true " +
           "GROUP BY s " +
           "HAVING AVG(r.rating) IS NOT NULL " +
           "ORDER BY avgRating DESC")
    Page<Object[]> findTopRated(Pageable pageable);
}
