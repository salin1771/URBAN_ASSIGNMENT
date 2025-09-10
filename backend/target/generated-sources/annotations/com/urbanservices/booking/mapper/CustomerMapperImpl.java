package com.urbanservices.booking.mapper;

import com.urbanservices.booking.dto.CustomerDto;
import com.urbanservices.booking.model.Customer;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-10T20:23:19+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Microsoft)"
)
@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public CustomerDto toDto(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        CustomerDto customerDto = new CustomerDto();

        customerDto.setId( customer.getId() );
        customerDto.setEmail( customer.getEmail() );
        customerDto.setPhone( customer.getPhone() );
        customerDto.setProfileImageUrl( customer.getProfileImageUrl() );
        customerDto.setCreatedAt( customer.getCreatedAt() );
        customerDto.setUpdatedAt( customer.getUpdatedAt() );
        customerDto.setPassword( customer.getPassword() );

        customerDto.setFirstName( customer.getName() != null ? customer.getName().trim().split("\s+")[0] : null );
        customerDto.setLastName( customer.getName() != null && customer.getName().trim().split("\s+").length > 1 ? customer.getName().trim().substring(customer.getName().trim().indexOf(' ') + 1) : "" );

        return customerDto;
    }

    @Override
    public Customer toEntity(CustomerDto customerDto) {
        if ( customerDto == null ) {
            return null;
        }

        Customer customer = new Customer();

        customer.setId( customerDto.getId() );
        customer.setEmail( customerDto.getEmail() );
        customer.setPhone( customerDto.getPhone() );
        customer.setProfileImageUrl( customerDto.getProfileImageUrl() );
        customer.setCreatedAt( customerDto.getCreatedAt() );
        customer.setUpdatedAt( customerDto.getUpdatedAt() );

        customer.setRole( com.urbanservices.booking.model.enums.UserRole.ROLE_CUSTOMER );
        customer.setEnabled( true );
        customer.setName( customerDto.getFirstName() + (customerDto.getLastName() != null && !customerDto.getLastName().isEmpty() ? " " + customerDto.getLastName() : "") );

        return customer;
    }
}
