package com.urbanservices.booking.mapper;

import com.urbanservices.booking.dto.ReviewDto;
import com.urbanservices.booking.model.Booking;
import com.urbanservices.booking.model.Customer;
import com.urbanservices.booking.model.Professional;
import com.urbanservices.booking.model.Review;
import com.urbanservices.booking.model.Service;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-10T20:23:19+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Microsoft)"
)
@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public ReviewDto toDto(Review entity) {
        if ( entity == null ) {
            return null;
        }

        ReviewDto reviewDto = new ReviewDto();

        reviewDto.setBookingId( entityBookingId( entity ) );
        reviewDto.setCustomerName( entityCustomerName( entity ) );
        reviewDto.setCustomerImageUrl( entityCustomerProfileImageUrl( entity ) );
        reviewDto.setProfessionalName( entityProfessionalName( entity ) );
        reviewDto.setServiceName( entityServiceName( entity ) );
        reviewDto.setCreatedAt( entity.getCreatedAt() );
        reviewDto.setUpdatedAt( entity.getUpdatedAt() );
        reviewDto.setId( entity.getId() );
        reviewDto.setRating( entity.getRating() );
        reviewDto.setComment( entity.getComment() );
        reviewDto.setAnonymous( entity.isAnonymous() );

        return reviewDto;
    }

    @Override
    public Review toEntity(ReviewDto dto) {
        if ( dto == null ) {
            return null;
        }

        Review review = new Review();

        review.setRating( dto.getRating() );
        review.setComment( dto.getComment() );
        review.setAnonymous( dto.isAnonymous() );

        mapReferences( dto, review );

        return review;
    }

    @Override
    public void updateEntityFromDto(ReviewDto dto, Review entity) {
        if ( dto == null ) {
            return;
        }

        entity.setRating( dto.getRating() );
        entity.setComment( dto.getComment() );
        entity.setAnonymous( dto.isAnonymous() );

        mapReferences( dto, entity );
    }

    private Long entityBookingId(Review review) {
        if ( review == null ) {
            return null;
        }
        Booking booking = review.getBooking();
        if ( booking == null ) {
            return null;
        }
        Long id = booking.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityCustomerName(Review review) {
        if ( review == null ) {
            return null;
        }
        Customer customer = review.getCustomer();
        if ( customer == null ) {
            return null;
        }
        String name = customer.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String entityCustomerProfileImageUrl(Review review) {
        if ( review == null ) {
            return null;
        }
        Customer customer = review.getCustomer();
        if ( customer == null ) {
            return null;
        }
        String profileImageUrl = customer.getProfileImageUrl();
        if ( profileImageUrl == null ) {
            return null;
        }
        return profileImageUrl;
    }

    private String entityProfessionalName(Review review) {
        if ( review == null ) {
            return null;
        }
        Professional professional = review.getProfessional();
        if ( professional == null ) {
            return null;
        }
        String name = professional.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String entityServiceName(Review review) {
        if ( review == null ) {
            return null;
        }
        Service service = review.getService();
        if ( service == null ) {
            return null;
        }
        String name = service.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
