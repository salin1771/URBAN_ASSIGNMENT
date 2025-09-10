package com.urbanservices.booking.service.impl;

import com.urbanservices.booking.dto.ServiceDto;
import com.urbanservices.booking.exception.ResourceNotFoundException;
import com.urbanservices.booking.mapper.ServiceMapper;
import com.urbanservices.booking.model.Service;
import com.urbanservices.booking.model.ServiceCategory;
import com.urbanservices.booking.repository.ServiceCategoryRepository;
import com.urbanservices.booking.repository.ServiceRepository;
import com.urbanservices.booking.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
// Using fully qualified name to avoid conflict with Service entity
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service("serviceService")
public class ServiceServiceImpl extends BaseServiceImpl<ServiceDto, Service, Long> implements ServiceService {

    private final ServiceRepository serviceRepository;
    private final ServiceCategoryRepository categoryRepository;

    @Autowired
    public ServiceServiceImpl(ServiceRepository serviceRepository,
                            ServiceCategoryRepository categoryRepository,
                            ServiceMapper serviceMapper) {
        super(serviceRepository, serviceMapper, Service.class);
        this.serviceRepository = serviceRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ServiceDto> findByCategoryId(Long categoryId, Pageable pageable) {
        return serviceRepository.findByCategoryId(categoryId, pageable)
                .map(this::toDtoWithRating);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ServiceDto> findByProfessionalId(Long professionalId, Pageable pageable) {
        return serviceRepository.findByProfessionalId(professionalId, pageable)
                .map(this::toDtoWithRating);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ServiceDto> search(String query, Pageable pageable) {
        return serviceRepository.search(query, pageable)
                .map(this::toDtoWithRating);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ServiceDto> findActiveServices(Pageable pageable) {
        return serviceRepository.findByIsActive(true, pageable)
                .map(this::toDtoWithRating);
    }

    @Override
    @Transactional
    public ServiceDto updateAverageRating(Long serviceId) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + serviceId));
        
        Double averageRating = serviceRepository.calculateAverageRating(serviceId);
        service.setAverageRating(averageRating != null ? averageRating : 0.0);
        
        Service updatedService = serviceRepository.save(service);
        return toDto(updatedService);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceDto> findTopBookedServices(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return serviceRepository.findTopBookedServices(limit, pageable).stream()
                .map(this::toDtoWithRating)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceDto> findTopRatedServices(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return serviceRepository.findTopRated(pageable).getContent().stream()
                .map(result -> {
                    Service service = (Service) result[0];
                    Double avgRating = (Double) result[1];
                    ServiceDto dto = toDto(service);
                    dto.setAverageRating(avgRating != null ? avgRating : 0.0);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateServiceStatus(Long id, boolean active) throws ResourceNotFoundException {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + id));
        
        service.setActive(active);
        serviceRepository.save(service);
    }

    @Override
    @Transactional
    public ServiceDto create(ServiceDto dto) {
        // Set default values if not provided
        if (dto.getDurationInMinutes() == null) {
            dto.setDurationInMinutes(60); // Default to 1 hour
        }
        
        // Set the category
        if (dto.getCategoryId() != null) {
            ServiceCategory category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + dto.getCategoryId()));
            
            Service service = toEntity(dto);
            service.setCategory(category);
            
            Service savedService = serviceRepository.save(service);
            return toDto(savedService);
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public ServiceDto update(Long id, ServiceDto dto) throws ResourceNotFoundException {
        return serviceRepository.findById(id)
                .map(existingService -> {
                    // Update basic fields
                    updateEntityFromDto(dto, existingService);
                    
                    // Update category if changed
                    if (dto.getCategoryId() != null && 
                        (existingService.getCategory() == null || 
                         !existingService.getCategory().getId().equals(dto.getCategoryId()))) {
                        
                        ServiceCategory category = categoryRepository.findById(dto.getCategoryId())
                                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + dto.getCategoryId()));
                        existingService.setCategory(category);
                    }
                    
                    Service updatedService = serviceRepository.save(existingService);
                    return toDto(updatedService);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + id));
    }

    private ServiceDto toDtoWithRating(Service service) {
        ServiceDto dto = toDto(service);
        
        if (service != null) {
            // Calculate and set average rating
            Double averageRating = serviceRepository.calculateAverageRating(service.getId());
            dto.setAverageRating(averageRating != null ? averageRating : 0.0);
            
            // Set other calculated fields if needed
            Long totalProfessionals = service.getProfessionals() != null ? 
                (long) service.getProfessionals().size() : 0L;
            dto.setTotalProfessionals(totalProfessionals);
            
            Long totalBookings = service.getBookings() != null ? 
                (long) service.getBookings().size() : 0L;
            dto.setTotalBookings(totalBookings);
        }
        
        return dto;
    }
}
