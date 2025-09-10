package com.urbanservices.booking.repository;

import com.urbanservices.booking.model.ServiceCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {
    
    Optional<ServiceCategory> findByName(String name);
    
    @Query("SELECT c FROM ServiceCategory c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<ServiceCategory> search(@Param("query") String query, Pageable pageable);
    
    @Query("SELECT c FROM ServiceCategory c WHERE c.active = true")
    Page<ServiceCategory> findActiveCategories(Pageable pageable);
    
    @Query("SELECT c FROM ServiceCategory c WHERE c.active = true AND LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<ServiceCategory> searchActiveCategories(@Param("query") String query, Pageable pageable);
}
