package com.urbanservices.booking.mapper;

import com.urbanservices.booking.dto.ReviewDto;
import com.urbanservices.booking.model.Review;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ReviewMapper extends BaseMapper<ReviewDto, Review> {
    
    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);
    
    @Override
    @Mapping(target = "bookingId", source = "booking.id")
    @Mapping(target = "customerName", source = "customer.name")
    @Mapping(target = "customerImageUrl", source = "customer.profileImageUrl")
    @Mapping(target = "professionalName", source = "professional.name")
    @Mapping(target = "serviceName", source = "service.name")
    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
    ReviewDto toDto(Review entity);
    
    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booking", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "professional", ignore = true)
    @Mapping(target = "service", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "adminReviewed", ignore = true)
    Review toEntity(ReviewDto dto);
    
    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booking", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "professional", ignore = true)
    @Mapping(target = "service", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "adminReviewed", ignore = true)
    void updateEntityFromDto(ReviewDto dto, @MappingTarget Review entity);
    
    @Override
    default List<ReviewDto> toDtoList(List<Review> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    default List<Review> toEntityList(List<ReviewDto> dtos) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
    
    @AfterMapping
    default void mapReferences(ReviewDto dto, @MappingTarget Review review) {
        // Map booking reference if bookingId is provided
        if (dto.getBookingId() != null) {
            com.urbanservices.booking.model.Booking booking = new com.urbanservices.booking.model.Booking();
            booking.setId(dto.getBookingId());
            review.setBooking(booking);
        }
        
        // Map customer reference if customerId is provided in the DTO (not shown in DTO but can be added if needed)
        // Similarly for professional and service if needed
    }
}
