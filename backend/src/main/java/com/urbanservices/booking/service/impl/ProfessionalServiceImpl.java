package com.urbanservices.booking.service.impl;

import com.urbanservices.booking.dto.ProfessionalDto;
import com.urbanservices.booking.dto.ServiceDto;
import com.urbanservices.booking.dto.auth.JwtAuthenticationResponse;
import com.urbanservices.booking.dto.auth.ProfessionalRegistrationRequest;
import com.urbanservices.booking.exception.ResourceNotFoundException;
import com.urbanservices.booking.mapper.ProfessionalMapper;
import com.urbanservices.booking.model.Professional;
import com.urbanservices.booking.model.Service;
import com.urbanservices.booking.security.JwtTokenProvider;
import com.urbanservices.booking.model.enums.UserRole;
import com.urbanservices.booking.repository.ProfessionalRepository;
import com.urbanservices.booking.repository.ServiceRepository;
import com.urbanservices.booking.service.ProfessionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ProfessionalServiceImpl extends BaseServiceImpl<ProfessionalDto, Professional, Long> 
        implements ProfessionalService {

    private final ProfessionalRepository professionalRepository;
    private final ServiceRepository serviceRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Autowired
    public ProfessionalServiceImpl(ProfessionalRepository professionalRepository,
                                 ServiceRepository serviceRepository,
                                 ProfessionalMapper professionalMapper,
                                 PasswordEncoder passwordEncoder,
                                 JwtTokenProvider tokenProvider) {
        super(professionalRepository, professionalMapper, Professional.class);
        this.professionalRepository = professionalRepository;
        this.serviceRepository = serviceRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    @Transactional
    public JwtAuthenticationResponse registerProfessional(ProfessionalRegistrationRequest registrationRequest) {
        // Create and save the professional user
        Professional professional = new Professional();
        professional.setName(registrationRequest.getName());
        professional.setEmail(registrationRequest.getEmail());
        professional.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        professional.setPhone(registrationRequest.getPhone());
        professional.setRole(UserRole.ROLE_PROFESSIONAL);
        professional.setEnabled(true);
        
        // Set default professional properties
        professional.setVerified(false);
        professional.setAvailable(false);
        
        // Save the professional
        Professional savedProfessional = professionalRepository.save(professional);
        
        // Generate JWT tokens
        String accessToken = tokenProvider.generateToken(savedProfessional);
        String refreshToken = tokenProvider.generateRefreshToken(savedProfessional);
        
        return JwtAuthenticationResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .expiresIn(tokenProvider.getJwtExpirationInMs() / 1000) // Convert to seconds
            .userId(savedProfessional.getId())
            .name(savedProfessional.getName())
            .email(savedProfessional.getEmail())
            .role(savedProfessional.getRole().name())
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProfessionalDto> findAvailableProfessionals(Pageable pageable) {
        return professionalRepository.findAvailableProfessionals(pageable)
                .map(this::toDtoWithRating);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProfessionalDto> findVerifiedProfessionals(Pageable pageable) {
        return professionalRepository.findVerifiedProfessionals(pageable)
                .map(this::toDtoWithRating);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProfessionalDto> findByServiceId(Long serviceId, Pageable pageable) {
        return professionalRepository.findByServiceId(serviceId, pageable)
                .map(this::toDtoWithRating);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProfessionalDto> findAvailableByServiceId(Long serviceId, Pageable pageable) {
        return professionalRepository.findAvailableProfessionalsByService(serviceId, pageable)
                .map(this::toDtoWithRating);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProfessionalDto> search(String query, Pageable pageable) {
        return professionalRepository.search(query, pageable)
                .map(this::toDtoWithRating);
    }

    @Override
    @Transactional
    public ProfessionalDto updateAverageRating(Long professionalId) {
        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new ResourceNotFoundException("Professional not found with id: " + professionalId));
        
        Double averageRating = professionalRepository.calculateAverageRating(professionalId);
        professional.setAverageRating(averageRating != null ? averageRating : 0.0);
        
        Professional updatedProfessional = professionalRepository.save(professional);
        return toDto(updatedProfessional);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfessionalDto> findTopRatedProfessionals(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return professionalRepository.findTopRated(pageable).getContent().stream()
                .map(result -> {
                    Professional professional = (Professional) result[0];
                    Double avgRating = (Double) result[1];
                    ProfessionalDto dto = toDto(professional);
                    dto.setAverageRating(avgRating != null ? avgRating : 0.0);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfessionalDto> findTopBookedProfessionals(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return professionalRepository.findTopBooked(limit, pageable).stream()
                .map(this::toDtoWithRating)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateVerificationStatus(Long id, boolean verified) throws ResourceNotFoundException {
        Professional professional = professionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professional not found with id: " + id));
        
        professional.setVerified(verified);
        professionalRepository.save(professional);
    }

    @Override
    @Transactional
    public void updateAvailabilityStatus(Long id, boolean available) throws ResourceNotFoundException {
        Professional professional = professionalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professional not found with id: " + id));
        
        professional.setAvailable(available);
        professionalRepository.save(professional);
    }

    @Override
    @Transactional
    public ProfessionalDto addService(Long professionalId, Long serviceId) throws ResourceNotFoundException {
        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new ResourceNotFoundException("Professional not found with id: " + professionalId));
        
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + serviceId));
        
        professional.getServices().add(service);
        Professional updatedProfessional = professionalRepository.save(professional);
        
        return toDto(updatedProfessional);
    }

    @Override
    @Transactional
    public ProfessionalDto removeService(Long professionalId, Long serviceId) throws ResourceNotFoundException {
        Professional professional = professionalRepository.findById(professionalId)
                .orElseThrow(() -> new ResourceNotFoundException("Professional not found with id: " + professionalId));
        
        boolean removed = professional.getServices().removeIf(service -> service.getId().equals(serviceId));
        
        if (!removed) {
            throw new ResourceNotFoundException("Service not found with id: " + serviceId + " for professional");
        }
        
        Professional updatedProfessional = professionalRepository.save(professional);
        return toDto(updatedProfessional);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfessionalDto> findNearbyProfessionals(Double latitude, Double longitude, Double radiusInKm, Long serviceId) {
        // This is a simplified implementation - in a real app, you'd use a spatial query
        // to find professionals within the specified radius
        List<Professional> nearbyProfessionals = professionalRepository.findNearbyProfessionals(
                latitude, longitude, radiusInKm, serviceId);
        
        return nearbyProfessionals.stream()
                .map(this::toDtoWithRating)
                .collect(Collectors.toList());
    }

    private ProfessionalDto toDtoWithRating(Professional professional) {
        ProfessionalDto dto = toDto(professional);
        
        // Set the services
        Set<ServiceDto> serviceDtos = professional.getServices().stream()
                .map(service -> {
                    ServiceDto serviceDto = new ServiceDto();
                    serviceDto.setId(service.getId());
                    serviceDto.setName(service.getName());
                    serviceDto.setBasePrice(service.getBasePrice());
                    // Add other service fields as needed
                    return serviceDto;
                })
                .collect(Collectors.toSet());
        dto.setServices(serviceDtos);
        
        // Set the average rating if not already set
        if (dto.getAverageRating() == null) {
            Double averageRating = professionalRepository.calculateAverageRating(professional.getId());
            dto.setAverageRating(averageRating != null ? averageRating : 0.0);
        }
        
        // Set the total number of reviews
        Long reviewCount = professionalRepository.countReviews(professional.getId());
        dto.setTotalReviews(reviewCount != null ? reviewCount.intValue() : 0);
        
        // Set the total number of bookings if needed
        // Long bookingCount = professionalRepository.countBookings(professional.getId());
        // dto.setTotalBookings(bookingCount != null ? bookingCount : 0L);
        
        return dto;
    }
}
