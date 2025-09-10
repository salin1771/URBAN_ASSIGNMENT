package com.urbanservices.booking.mapper;

import com.urbanservices.booking.dto.CustomerDto;
import com.urbanservices.booking.model.Customer;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = {LocalDateTime.class})
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", expression = "java(customer.getName() != null ? customer.getName().trim().split(\"\\s+\")[0] : null)")
    @Mapping(target = "lastName", expression = "java(customer.getName() != null && customer.getName().trim().split(\"\\s+\").length > 1 ? customer.getName().trim().substring(customer.getName().trim().indexOf(' ') + 1) : \"\")")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "profileImageUrl", source = "profileImageUrl")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    CustomerDto toDto(Customer customer);

    @InheritInverseConfiguration
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", expression = "java(com.urbanservices.booking.model.enums.UserRole.ROLE_CUSTOMER)")
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "emailVerificationToken", ignore = true)
    @Mapping(target = "resetToken", ignore = true)
    @Mapping(target = "resetTokenExpiry", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "name", expression = "java(customerDto.getFirstName() + (customerDto.getLastName() != null && !customerDto.getLastName().isEmpty() ? \" \" + customerDto.getLastName() : \"\"))")
    Customer toEntity(CustomerDto customerDto);
}
