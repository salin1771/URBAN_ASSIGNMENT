package com.urbanservices.booking.mapper;

import com.urbanservices.booking.dto.ProfessionalDto;
import com.urbanservices.booking.model.Professional;
import com.urbanservices.booking.model.Address;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.stream.Collectors;

@Mapper(
    componentModel = "spring",
    uses = {ServiceMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProfessionalMapper extends BaseMapper<ProfessionalDto, Professional> {
    
    ProfessionalMapper INSTANCE = Mappers.getMapper(ProfessionalMapper.class);
    
    @Override
    @Mapping(target = "serviceIds", expression = "java(entity.getServices() != null ? entity.getServices().stream().map(s -> s != null ? s.getId() : null).filter(java.util.Objects::nonNull).collect(java.util.stream.Collectors.toSet()) : new java.util.HashSet<>())")
    @Mapping(target = "verified", source = "verified")
    @Mapping(target = "available", source = "available")
    @Mapping(target = "services", ignore = true) // Will be set manually if needed
    @Mapping(target = "address", source = "address.addressLine1")
    @Mapping(target = "city", source = "address.city")
    @Mapping(target = "country", source = "address.country")
    @Mapping(target = "postalCode", source = "address.postalCode")
    @Mapping(target = "state", source = "address.state")
    @Mapping(target = "totalBookings", expression = "java(entity.getBookings() != null ? (long) entity.getBookings().size() : 0L)")
    // Only include fields that exist in ProfessionalDto
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @BeanMapping(ignoreByDefault = true)
    // User fields are already mapped by the base class
    ProfessionalDto toDto(Professional entity);
    
    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "services", ignore = true) // Will be set manually
    @Mapping(target = "documents", ignore = true)
    @Mapping(target = "availabilities", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "bookings", ignore = true) // Will be managed by the service layer
    @Mapping(target = "verified", source = "verified")
    @Mapping(target = "available", source = "available")
    @Mapping(target = "address", expression = "java(mapToAddress(dto))")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    // Security-related fields - these should be handled by the security layer, not mapped from DTO
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    // Other User fields are handled by the base class
    Professional toEntity(ProfessionalDto dto);
    
    /**
     * Maps ProfessionalDto to Address entity
     * @param dto ProfessionalDto containing address information
     * @return Address entity
     */
    default Address mapToAddress(ProfessionalDto dto) {
        if (dto == null || (dto.getAddress() == null && dto.getCity() == null && 
            dto.getState() == null && dto.getPostalCode() == null && dto.getCountry() == null)) {
            return null;
        }
        
        Address address = new Address();
        address.setAddressLine1(dto.getAddress());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPostalCode(dto.getPostalCode());
        address.setCountry(dto.getCountry());
        address.setDefault(false);
        address.setLabel("Professional Address");
        
        return address;
    }
    
    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "services", ignore = true)
    @Mapping(target = "documents", ignore = true)
    @Mapping(target = "availabilities", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "verified", source = "verified")
    @Mapping(target = "available", source = "available")
    @Mapping(target = "address", expression = "java(updateAddressFromDto(dto, entity.getAddress()))")
    void updateEntityFromDto(ProfessionalDto dto, @MappingTarget Professional entity);
    
    /**
     * Updates an existing Address entity from ProfessionalDto
     */
    default Address updateAddressFromDto(ProfessionalDto dto, Address existingAddress) {
        if (dto == null || (dto.getAddress() == null && dto.getCity() == null && 
            dto.getState() == null && dto.getPostalCode() == null && dto.getCountry() == null)) {
            return null;
        }
        
        if (existingAddress == null) {
            return mapToAddress(dto);
        }
        
        existingAddress.setAddressLine1(dto.getAddress());
        existingAddress.setCity(dto.getCity());
        existingAddress.setState(dto.getState());
        existingAddress.setPostalCode(dto.getPostalCode());
        existingAddress.setCountry(dto.getCountry());
        
        return existingAddress;
    }
    
    @AfterMapping
    default void mapServices(ProfessionalDto dto, @MappingTarget Professional professional) {
        if (dto.getServiceIds() != null) {
            professional.setServices(
                dto.getServiceIds().stream()
                    .map(id -> {
                        com.urbanservices.booking.model.Service service = new com.urbanservices.booking.model.Service();
                        service.setId(id);
                        return service;
                    })
                    .collect(Collectors.toSet())
            );
        }
    }
}
