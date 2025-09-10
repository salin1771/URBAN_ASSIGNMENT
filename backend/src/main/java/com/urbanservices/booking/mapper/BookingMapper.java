package com.urbanservices.booking.mapper;

import com.urbanservices.booking.dto.BookingDto;
import com.urbanservices.booking.dto.BookingAddonDto;
import com.urbanservices.booking.model.BookingAddon;
import com.urbanservices.booking.model.Booking;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {ProfessionalMapper.class, ServiceMapper.class, AddressMapper.class})
public interface BookingMapper extends BaseMapper<BookingDto, Booking> {
    
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);
    
    @Override
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "professionalId", source = "professional.id")
    @Mapping(target = "serviceId", source = "service.id")
    @Mapping(target = "addressId", source = "address.id")
    @Mapping(target = "customerName", source = "customer.name")
    @Mapping(target = "professionalName", source = "professional.name")
    @Mapping(target = "serviceName", source = "service.name")
    @Mapping(target = "serviceImageUrl", source = "service.imageUrl")
    @Mapping(target = "addressDetails", expression = "java(entity.getAddress() != null ? entity.getAddress().getFullAddress() : null)")
    @Mapping(target = "professionalRating", source = "professional.averageRating")
    @Mapping(target = "professionalReviewCount", expression = "java(entity.getProfessional() != null && entity.getProfessional().getReviews() != null ? entity.getProfessional().getReviews().size() : 0)")
    BookingDto toDto(Booking entity);
    
    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "professional", ignore = true)
    @Mapping(target = "service", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "addons", source = "addons")
    @Mapping(target = "review", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Booking toEntity(BookingDto dto);
    
    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "professional", ignore = true)
    @Mapping(target = "service", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "addons", source = "addons")
    @Mapping(target = "review", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(BookingDto dto, @MappingTarget Booking entity);
    
    @Mapping(target = "addonId", source = "addon.id")
    @Mapping(target = "name", source = "addon.name")
    @Mapping(target = "description", source = "addon.description")
    @Mapping(target = "price", source = "priceAtBooking")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    BookingAddonDto toBookingAddonDto(BookingAddon bookingAddon);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "addon", ignore = true) // Will be set manually in the service layer
    @Mapping(target = "booking", ignore = true) // Will be set manually in the service layer
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "priceAtBooking", source = "price")
    @Mapping(target = "nameAtBooking", source = "name")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    BookingAddon toBookingAddon(BookingAddonDto dto);
    
    @AfterMapping
    default void mapReferences(BookingDto dto, @MappingTarget Booking booking) {
        // Map customer reference
        if (dto.getCustomerId() != null) {
            com.urbanservices.booking.model.Customer customer = new com.urbanservices.booking.model.Customer();
            customer.setId(dto.getCustomerId());
            booking.setCustomer(customer);
        }
        
        // Map professional reference
        if (dto.getProfessionalId() != null) {
            com.urbanservices.booking.model.Professional professional = new com.urbanservices.booking.model.Professional();
            professional.setId(dto.getProfessionalId());
            booking.setProfessional(professional);
        }
        
        // Map service reference
        if (dto.getServiceId() != null) {
            com.urbanservices.booking.model.Service service = new com.urbanservices.booking.model.Service();
            service.setId(dto.getServiceId());
            booking.setService(service);
        }
        
        // Map address reference
        if (dto.getAddressId() != null) {
            com.urbanservices.booking.model.Address address = new com.urbanservices.booking.model.Address();
            address.setId(dto.getAddressId());
            booking.setAddress(address);
        }
    }
}
