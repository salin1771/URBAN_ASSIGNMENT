package com.urbanservices.booking.mapper;

import com.urbanservices.booking.dto.AddressDto;
import com.urbanservices.booking.model.Address;
import com.urbanservices.booking.model.Customer;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AddressMapper extends BaseMapper<AddressDto, Address> {
    
    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);
    
    @Override
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "default", source = "default")
    AddressDto toDto(Address entity);
    
    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
@Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "default", source = "default")
    Address toEntity(AddressDto dto);
    
    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
@Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "default", source = "default")
    void updateEntityFromDto(AddressDto dto, @MappingTarget Address entity);
    
    @Override
    default List<AddressDto> toDtoList(List<Address> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    default List<Address> toEntityList(List<AddressDto> dtos) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
    
    @AfterMapping
    default void mapCustomer(AddressDto dto, @MappingTarget Address address) {
        if (dto.getCustomerId() != null) {
            Customer customer = new Customer();
            customer.setId(dto.getCustomerId());
            address.setCustomer(customer);
        }
    }
}
