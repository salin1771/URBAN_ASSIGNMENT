package com.urbanservices.booking.mapper;

import com.urbanservices.booking.dto.ServiceDto;
import com.urbanservices.booking.model.Service;
import com.urbanservices.booking.model.ServiceCategory;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-10T20:23:19+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Microsoft)"
)
@Component
public class ServiceMapperImpl implements ServiceMapper {

    @Override
    public List<ServiceDto> toDtoList(List<Service> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ServiceDto> list = new ArrayList<ServiceDto>( entities.size() );
        for ( Service service : entities ) {
            list.add( toDto( service ) );
        }

        return list;
    }

    @Override
    public List<Service> toEntityList(List<ServiceDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<Service> list = new ArrayList<Service>( dtos.size() );
        for ( ServiceDto serviceDto : dtos ) {
            list.add( toEntity( serviceDto ) );
        }

        return list;
    }

    @Override
    public ServiceDto toDto(Service service) {
        if ( service == null ) {
            return null;
        }

        ServiceDto serviceDto = new ServiceDto();

        serviceDto.setCategoryId( serviceCategoryId( service ) );
        serviceDto.setCategoryName( serviceCategoryName( service ) );
        serviceDto.setActive( service.isActive() );
        serviceDto.setCreatedAt( service.getCreatedAt() );
        serviceDto.setUpdatedAt( service.getUpdatedAt() );
        serviceDto.setId( service.getId() );
        serviceDto.setName( service.getName() );
        serviceDto.setDescription( service.getDescription() );
        serviceDto.setBasePrice( service.getBasePrice() );
        serviceDto.setDurationInMinutes( service.getDurationInMinutes() );
        serviceDto.setImageUrl( service.getImageUrl() );
        serviceDto.setAverageRating( service.getAverageRating() );
        if ( service.getTotalProfessionals() != null ) {
            serviceDto.setTotalProfessionals( service.getTotalProfessionals().longValue() );
        }

        serviceDto.setTotalBookings( service.getBookings() != null ? (long) service.getBookings().size() : 0L );

        return serviceDto;
    }

    @Override
    public Service toEntity(ServiceDto serviceDto) {
        if ( serviceDto == null ) {
            return null;
        }

        Service service = new Service();

        service.setActive( serviceDto.isActive() );
        service.setId( serviceDto.getId() );
        service.setCreatedAt( serviceDto.getCreatedAt() );
        service.setUpdatedAt( serviceDto.getUpdatedAt() );
        service.setAverageRating( serviceDto.getAverageRating() );
        service.setName( serviceDto.getName() );
        service.setDescription( serviceDto.getDescription() );
        service.setBasePrice( serviceDto.getBasePrice() );
        service.setImageUrl( serviceDto.getImageUrl() );
        service.setDurationInMinutes( serviceDto.getDurationInMinutes() );
        if ( serviceDto.getTotalProfessionals() != null ) {
            service.setTotalProfessionals( serviceDto.getTotalProfessionals().intValue() );
        }

        return service;
    }

    @Override
    public void updateEntityFromDto(ServiceDto dto, Service entity) {
        if ( dto == null ) {
            return;
        }

        entity.setActive( dto.isActive() );
        entity.setCreatedAt( dto.getCreatedAt() );
        entity.setUpdatedAt( dto.getUpdatedAt() );
        entity.setAverageRating( dto.getAverageRating() );
        entity.setName( dto.getName() );
        entity.setDescription( dto.getDescription() );
        entity.setBasePrice( dto.getBasePrice() );
        entity.setImageUrl( dto.getImageUrl() );
        entity.setDurationInMinutes( dto.getDurationInMinutes() );
        if ( dto.getTotalProfessionals() != null ) {
            entity.setTotalProfessionals( dto.getTotalProfessionals().intValue() );
        }
        else {
            entity.setTotalProfessionals( null );
        }
    }

    private Long serviceCategoryId(Service service) {
        if ( service == null ) {
            return null;
        }
        ServiceCategory category = service.getCategory();
        if ( category == null ) {
            return null;
        }
        Long id = category.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String serviceCategoryName(Service service) {
        if ( service == null ) {
            return null;
        }
        ServiceCategory category = service.getCategory();
        if ( category == null ) {
            return null;
        }
        String name = category.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
