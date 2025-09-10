package com.urbanservices.booking.mapper;

import com.urbanservices.booking.dto.ServiceDto;
import com.urbanservices.booking.model.Service;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ServiceMapper extends BaseMapper<ServiceDto, Service> {
    
    ServiceMapper INSTANCE = Mappers.getMapper(ServiceMapper.class);
    
    @Override
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "totalBookings", expression = "java(service.getBookings() != null ? (long) service.getBookings().size() : 0L)")
    ServiceDto toDto(Service service);
    
    @Override
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "professionals", ignore = true)
    @Mapping(target = "addons", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "active", source = "active")
    Service toEntity(ServiceDto serviceDto);
    
    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "professionals", ignore = true)
    @Mapping(target = "addons", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "active", source = "active")
    void updateEntityFromDto(ServiceDto dto, @MappingTarget Service entity);
}
