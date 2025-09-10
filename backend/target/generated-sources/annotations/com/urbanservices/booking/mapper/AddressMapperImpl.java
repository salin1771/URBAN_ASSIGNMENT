package com.urbanservices.booking.mapper;

import com.urbanservices.booking.dto.AddressDto;
import com.urbanservices.booking.model.Address;
import com.urbanservices.booking.model.Customer;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-10T20:23:19+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Microsoft)"
)
@Component
public class AddressMapperImpl implements AddressMapper {

    @Override
    public AddressDto toDto(Address entity) {
        if ( entity == null ) {
            return null;
        }

        AddressDto addressDto = new AddressDto();

        addressDto.setCustomerId( entityCustomerId( entity ) );
        addressDto.setDefault( entity.isDefault() );
        addressDto.setCreatedAt( entity.getCreatedAt() );
        addressDto.setUpdatedAt( entity.getUpdatedAt() );
        addressDto.setId( entity.getId() );
        addressDto.setAddressLine1( entity.getAddressLine1() );
        addressDto.setAddressLine2( entity.getAddressLine2() );
        addressDto.setCity( entity.getCity() );
        addressDto.setState( entity.getState() );
        addressDto.setPostalCode( entity.getPostalCode() );
        addressDto.setCountry( entity.getCountry() );
        addressDto.setLabel( entity.getLabel() );
        addressDto.setInstructions( entity.getInstructions() );

        return addressDto;
    }

    @Override
    public Address toEntity(AddressDto dto) {
        if ( dto == null ) {
            return null;
        }

        Address address = new Address();

        address.setDefault( dto.isDefault() );
        address.setAddressLine1( dto.getAddressLine1() );
        address.setAddressLine2( dto.getAddressLine2() );
        address.setCity( dto.getCity() );
        address.setState( dto.getState() );
        address.setPostalCode( dto.getPostalCode() );
        address.setCountry( dto.getCountry() );
        address.setLabel( dto.getLabel() );
        address.setInstructions( dto.getInstructions() );

        mapCustomer( dto, address );

        return address;
    }

    @Override
    public void updateEntityFromDto(AddressDto dto, Address entity) {
        if ( dto == null ) {
            return;
        }

        entity.setDefault( dto.isDefault() );
        entity.setAddressLine1( dto.getAddressLine1() );
        entity.setAddressLine2( dto.getAddressLine2() );
        entity.setCity( dto.getCity() );
        entity.setState( dto.getState() );
        entity.setPostalCode( dto.getPostalCode() );
        entity.setCountry( dto.getCountry() );
        entity.setLabel( dto.getLabel() );
        entity.setInstructions( dto.getInstructions() );

        mapCustomer( dto, entity );
    }

    private Long entityCustomerId(Address address) {
        if ( address == null ) {
            return null;
        }
        Customer customer = address.getCustomer();
        if ( customer == null ) {
            return null;
        }
        Long id = customer.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
