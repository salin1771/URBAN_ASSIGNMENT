package com.urbanservices.booking.mapper;

import com.urbanservices.booking.dto.ProfessionalDto;
import com.urbanservices.booking.model.Address;
import com.urbanservices.booking.model.Professional;
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
public class ProfessionalMapperImpl implements ProfessionalMapper {

    @Override
    public List<ProfessionalDto> toDtoList(List<Professional> entities) {
        if ( entities == null ) {
            return null;
        }

        List<ProfessionalDto> list = new ArrayList<ProfessionalDto>( entities.size() );
        for ( Professional professional : entities ) {
            list.add( toDto( professional ) );
        }

        return list;
    }

    @Override
    public List<Professional> toEntityList(List<ProfessionalDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<Professional> list = new ArrayList<Professional>( dtos.size() );
        for ( ProfessionalDto professionalDto : dtos ) {
            list.add( toEntity( professionalDto ) );
        }

        return list;
    }

    @Override
    public ProfessionalDto toDto(Professional entity) {
        if ( entity == null ) {
            return null;
        }

        ProfessionalDto professionalDto = new ProfessionalDto();

        professionalDto.setVerified( entity.isVerified() );
        professionalDto.setAvailable( entity.isAvailable() );
        professionalDto.setAddress( entityAddressAddressLine1( entity ) );
        professionalDto.setCity( entityAddressCity( entity ) );
        professionalDto.setCountry( entityAddressCountry( entity ) );
        professionalDto.setPostalCode( entityAddressPostalCode( entity ) );
        professionalDto.setState( entityAddressState( entity ) );
        professionalDto.setCreatedAt( entity.getCreatedAt() );
        professionalDto.setUpdatedAt( entity.getUpdatedAt() );

        professionalDto.setServiceIds( entity.getServices() != null ? entity.getServices().stream().map(s -> s != null ? s.getId() : null).filter(java.util.Objects::nonNull).collect(java.util.stream.Collectors.toSet()) : new java.util.HashSet<>() );
        professionalDto.setTotalBookings( entity.getBookings() != null ? (long) entity.getBookings().size() : 0L );

        return professionalDto;
    }

    @Override
    public Professional toEntity(ProfessionalDto dto) {
        if ( dto == null ) {
            return null;
        }

        Professional professional = new Professional();

        professional.setVerified( dto.isVerified() );
        professional.setAvailable( dto.isAvailable() );
        professional.setName( dto.getName() );
        professional.setEmail( dto.getEmail() );
        professional.setPhone( dto.getPhone() );
        professional.setProfileImageUrl( dto.getProfileImageUrl() );
        professional.setBio( dto.getBio() );
        professional.setYearsOfExperience( dto.getYearsOfExperience() );
        professional.setHourlyRate( dto.getHourlyRate() );
        professional.setAverageRating( dto.getAverageRating() );
        professional.setTotalReviews( dto.getTotalReviews() );

        professional.setAddress( mapToAddress(dto) );

        mapServices( dto, professional );

        return professional;
    }

    @Override
    public void updateEntityFromDto(ProfessionalDto dto, Professional entity) {
        if ( dto == null ) {
            return;
        }

        entity.setVerified( dto.isVerified() );
        entity.setAvailable( dto.isAvailable() );
        if ( dto.getCreatedAt() != null ) {
            entity.setCreatedAt( dto.getCreatedAt() );
        }
        if ( dto.getUpdatedAt() != null ) {
            entity.setUpdatedAt( dto.getUpdatedAt() );
        }
        if ( dto.getName() != null ) {
            entity.setName( dto.getName() );
        }
        if ( dto.getEmail() != null ) {
            entity.setEmail( dto.getEmail() );
        }
        if ( dto.getPhone() != null ) {
            entity.setPhone( dto.getPhone() );
        }
        if ( dto.getProfileImageUrl() != null ) {
            entity.setProfileImageUrl( dto.getProfileImageUrl() );
        }
        if ( dto.getBio() != null ) {
            entity.setBio( dto.getBio() );
        }
        if ( dto.getYearsOfExperience() != null ) {
            entity.setYearsOfExperience( dto.getYearsOfExperience() );
        }
        if ( dto.getHourlyRate() != null ) {
            entity.setHourlyRate( dto.getHourlyRate() );
        }
        if ( dto.getAverageRating() != null ) {
            entity.setAverageRating( dto.getAverageRating() );
        }
        if ( dto.getTotalReviews() != null ) {
            entity.setTotalReviews( dto.getTotalReviews() );
        }

        entity.setAddress( updateAddressFromDto(dto, entity.getAddress()) );

        mapServices( dto, entity );
    }

    private String entityAddressAddressLine1(Professional professional) {
        if ( professional == null ) {
            return null;
        }
        Address address = professional.getAddress();
        if ( address == null ) {
            return null;
        }
        String addressLine1 = address.getAddressLine1();
        if ( addressLine1 == null ) {
            return null;
        }
        return addressLine1;
    }

    private String entityAddressCity(Professional professional) {
        if ( professional == null ) {
            return null;
        }
        Address address = professional.getAddress();
        if ( address == null ) {
            return null;
        }
        String city = address.getCity();
        if ( city == null ) {
            return null;
        }
        return city;
    }

    private String entityAddressCountry(Professional professional) {
        if ( professional == null ) {
            return null;
        }
        Address address = professional.getAddress();
        if ( address == null ) {
            return null;
        }
        String country = address.getCountry();
        if ( country == null ) {
            return null;
        }
        return country;
    }

    private String entityAddressPostalCode(Professional professional) {
        if ( professional == null ) {
            return null;
        }
        Address address = professional.getAddress();
        if ( address == null ) {
            return null;
        }
        String postalCode = address.getPostalCode();
        if ( postalCode == null ) {
            return null;
        }
        return postalCode;
    }

    private String entityAddressState(Professional professional) {
        if ( professional == null ) {
            return null;
        }
        Address address = professional.getAddress();
        if ( address == null ) {
            return null;
        }
        String state = address.getState();
        if ( state == null ) {
            return null;
        }
        return state;
    }
}
