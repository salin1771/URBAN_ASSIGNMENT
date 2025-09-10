package com.urbanservices.booking.service;

import com.urbanservices.booking.dto.ServiceDto;
import com.urbanservices.booking.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ServiceService extends BaseService<ServiceDto, com.urbanservices.booking.model.Service, Long> {
    
    Page<ServiceDto> findByCategoryId(Long categoryId, Pageable pageable);
    
    Page<ServiceDto> findByProfessionalId(Long professionalId, Pageable pageable);
    
    Page<ServiceDto> search(String query, Pageable pageable);
    
    Page<ServiceDto> findActiveServices(Pageable pageable);
    
    ServiceDto updateAverageRating(Long serviceId);
    
    List<ServiceDto> findTopBookedServices(int limit);
    
    List<ServiceDto> findTopRatedServices(int limit);
    
    void updateServiceStatus(Long id, boolean active) throws ResourceNotFoundException;
}
