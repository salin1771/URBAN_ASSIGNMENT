package com.urbanservices.booking.service;

import com.urbanservices.booking.dto.ProfessionalDto;
import com.urbanservices.booking.dto.auth.JwtAuthenticationResponse;
import com.urbanservices.booking.dto.auth.ProfessionalRegistrationRequest;
import com.urbanservices.booking.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProfessionalService extends BaseService<ProfessionalDto, com.urbanservices.booking.model.Professional, Long> {
    
    JwtAuthenticationResponse registerProfessional(ProfessionalRegistrationRequest registrationRequest);
    
    Page<ProfessionalDto> findAvailableProfessionals(Pageable pageable);
    
    Page<ProfessionalDto> findVerifiedProfessionals(Pageable pageable);
    
    Page<ProfessionalDto> findByServiceId(Long serviceId, Pageable pageable);
    
    Page<ProfessionalDto> findAvailableByServiceId(Long serviceId, Pageable pageable);
    
    Page<ProfessionalDto> search(String query, Pageable pageable);
    
    ProfessionalDto updateAverageRating(Long professionalId);
    
    List<ProfessionalDto> findTopRatedProfessionals(int limit);
    
    List<ProfessionalDto> findTopBookedProfessionals(int limit);
    
    void updateVerificationStatus(Long id, boolean verified) throws ResourceNotFoundException;
    
    void updateAvailabilityStatus(Long id, boolean available) throws ResourceNotFoundException;
    
    ProfessionalDto addService(Long professionalId, Long serviceId) throws ResourceNotFoundException;
    
    ProfessionalDto removeService(Long professionalId, Long serviceId) throws ResourceNotFoundException;
    
    List<ProfessionalDto> findNearbyProfessionals(Double latitude, Double longitude, Double radiusInKm, Long serviceId);
}
