package com.urbanservices.booking.mapper;

import com.urbanservices.booking.dto.BookingAddonDto;
import com.urbanservices.booking.dto.BookingDto;
import com.urbanservices.booking.model.Address;
import com.urbanservices.booking.model.Booking;
import com.urbanservices.booking.model.BookingAddon;
import com.urbanservices.booking.model.Customer;
import com.urbanservices.booking.model.Professional;
import com.urbanservices.booking.model.Service;
import com.urbanservices.booking.model.ServiceAddon;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-10T20:23:19+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Microsoft)"
)
@Component
public class BookingMapperImpl implements BookingMapper {

    @Override
    public List<BookingDto> toDtoList(List<Booking> entities) {
        if ( entities == null ) {
            return null;
        }

        List<BookingDto> list = new ArrayList<BookingDto>( entities.size() );
        for ( Booking booking : entities ) {
            list.add( toDto( booking ) );
        }

        return list;
    }

    @Override
    public List<Booking> toEntityList(List<BookingDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<Booking> list = new ArrayList<Booking>( dtos.size() );
        for ( BookingDto bookingDto : dtos ) {
            list.add( toEntity( bookingDto ) );
        }

        return list;
    }

    @Override
    public BookingDto toDto(Booking entity) {
        if ( entity == null ) {
            return null;
        }

        BookingDto bookingDto = new BookingDto();

        bookingDto.setCustomerId( entityCustomerId( entity ) );
        bookingDto.setProfessionalId( entityProfessionalId( entity ) );
        bookingDto.setServiceId( entityServiceId( entity ) );
        bookingDto.setAddressId( entityAddressId( entity ) );
        bookingDto.setCustomerName( entityCustomerName( entity ) );
        bookingDto.setProfessionalName( entityProfessionalName( entity ) );
        bookingDto.setServiceName( entityServiceName( entity ) );
        bookingDto.setServiceImageUrl( entityServiceImageUrl( entity ) );
        bookingDto.setProfessionalRating( entityProfessionalAverageRating( entity ) );
        bookingDto.setCreatedAt( entity.getCreatedAt() );
        bookingDto.setUpdatedAt( entity.getUpdatedAt() );
        bookingDto.setId( entity.getId() );
        bookingDto.setBookingDate( entity.getBookingDate() );
        bookingDto.setEndDate( entity.getEndDate() );
        bookingDto.setStatus( entity.getStatus() );
        bookingDto.setTotalAmount( entity.getTotalAmount() );
        bookingDto.setAddons( bookingAddonSetToBookingAddonDtoSet( entity.getAddons() ) );
        bookingDto.setSpecialInstructions( entity.getSpecialInstructions() );
        bookingDto.setCancellationReason( entity.getCancellationReason() );
        bookingDto.setCancelledBy( entity.getCancelledBy() );
        bookingDto.setRescheduledFrom( entity.getRescheduledFrom() );

        bookingDto.setAddressDetails( entity.getAddress() != null ? entity.getAddress().getFullAddress() : null );
        bookingDto.setProfessionalReviewCount( entity.getProfessional() != null && entity.getProfessional().getReviews() != null ? entity.getProfessional().getReviews().size() : 0 );

        return bookingDto;
    }

    @Override
    public Booking toEntity(BookingDto dto) {
        if ( dto == null ) {
            return null;
        }

        Booking booking = new Booking();

        booking.setAddons( bookingAddonDtoSetToBookingAddonSet( dto.getAddons() ) );
        booking.setBookingDate( dto.getBookingDate() );
        booking.setEndDate( dto.getEndDate() );
        booking.setStatus( dto.getStatus() );
        booking.setTotalAmount( dto.getTotalAmount() );
        booking.setSpecialInstructions( dto.getSpecialInstructions() );
        booking.setCancellationReason( dto.getCancellationReason() );
        booking.setCancelledBy( dto.getCancelledBy() );
        booking.setRescheduledFrom( dto.getRescheduledFrom() );

        mapReferences( dto, booking );

        return booking;
    }

    @Override
    public void updateEntityFromDto(BookingDto dto, Booking entity) {
        if ( dto == null ) {
            return;
        }

        if ( entity.getAddons() != null ) {
            Set<BookingAddon> set = bookingAddonDtoSetToBookingAddonSet( dto.getAddons() );
            if ( set != null ) {
                entity.getAddons().clear();
                entity.getAddons().addAll( set );
            }
            else {
                entity.setAddons( null );
            }
        }
        else {
            Set<BookingAddon> set = bookingAddonDtoSetToBookingAddonSet( dto.getAddons() );
            if ( set != null ) {
                entity.setAddons( set );
            }
        }
        entity.setBookingDate( dto.getBookingDate() );
        entity.setEndDate( dto.getEndDate() );
        entity.setStatus( dto.getStatus() );
        entity.setTotalAmount( dto.getTotalAmount() );
        entity.setSpecialInstructions( dto.getSpecialInstructions() );
        entity.setCancellationReason( dto.getCancellationReason() );
        entity.setCancelledBy( dto.getCancelledBy() );
        entity.setRescheduledFrom( dto.getRescheduledFrom() );

        mapReferences( dto, entity );
    }

    @Override
    public BookingAddonDto toBookingAddonDto(BookingAddon bookingAddon) {
        if ( bookingAddon == null ) {
            return null;
        }

        BookingAddonDto bookingAddonDto = new BookingAddonDto();

        bookingAddonDto.setAddonId( bookingAddonAddonId( bookingAddon ) );
        bookingAddonDto.setName( bookingAddonAddonName( bookingAddon ) );
        bookingAddonDto.setDescription( bookingAddonAddonDescription( bookingAddon ) );
        bookingAddonDto.setPrice( bookingAddon.getPriceAtBooking() );
        bookingAddonDto.setId( bookingAddon.getId() );
        bookingAddonDto.setCreatedAt( bookingAddon.getCreatedAt() );
        bookingAddonDto.setUpdatedAt( bookingAddon.getUpdatedAt() );
        bookingAddonDto.setQuantity( bookingAddon.getQuantity() );

        return bookingAddonDto;
    }

    @Override
    public BookingAddon toBookingAddon(BookingAddonDto dto) {
        if ( dto == null ) {
            return null;
        }

        BookingAddon bookingAddon = new BookingAddon();

        bookingAddon.setId( dto.getId() );
        bookingAddon.setQuantity( dto.getQuantity() );
        bookingAddon.setPriceAtBooking( dto.getPrice() );
        bookingAddon.setNameAtBooking( dto.getName() );
        bookingAddon.setCreatedAt( dto.getCreatedAt() );
        bookingAddon.setUpdatedAt( dto.getUpdatedAt() );

        return bookingAddon;
    }

    private Long entityCustomerId(Booking booking) {
        if ( booking == null ) {
            return null;
        }
        Customer customer = booking.getCustomer();
        if ( customer == null ) {
            return null;
        }
        Long id = customer.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entityProfessionalId(Booking booking) {
        if ( booking == null ) {
            return null;
        }
        Professional professional = booking.getProfessional();
        if ( professional == null ) {
            return null;
        }
        Long id = professional.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entityServiceId(Booking booking) {
        if ( booking == null ) {
            return null;
        }
        Service service = booking.getService();
        if ( service == null ) {
            return null;
        }
        Long id = service.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entityAddressId(Booking booking) {
        if ( booking == null ) {
            return null;
        }
        Address address = booking.getAddress();
        if ( address == null ) {
            return null;
        }
        Long id = address.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityCustomerName(Booking booking) {
        if ( booking == null ) {
            return null;
        }
        Customer customer = booking.getCustomer();
        if ( customer == null ) {
            return null;
        }
        String name = customer.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String entityProfessionalName(Booking booking) {
        if ( booking == null ) {
            return null;
        }
        Professional professional = booking.getProfessional();
        if ( professional == null ) {
            return null;
        }
        String name = professional.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String entityServiceName(Booking booking) {
        if ( booking == null ) {
            return null;
        }
        Service service = booking.getService();
        if ( service == null ) {
            return null;
        }
        String name = service.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String entityServiceImageUrl(Booking booking) {
        if ( booking == null ) {
            return null;
        }
        Service service = booking.getService();
        if ( service == null ) {
            return null;
        }
        String imageUrl = service.getImageUrl();
        if ( imageUrl == null ) {
            return null;
        }
        return imageUrl;
    }

    private Double entityProfessionalAverageRating(Booking booking) {
        if ( booking == null ) {
            return null;
        }
        Professional professional = booking.getProfessional();
        if ( professional == null ) {
            return null;
        }
        Double averageRating = professional.getAverageRating();
        if ( averageRating == null ) {
            return null;
        }
        return averageRating;
    }

    protected Set<BookingAddonDto> bookingAddonSetToBookingAddonDtoSet(Set<BookingAddon> set) {
        if ( set == null ) {
            return null;
        }

        Set<BookingAddonDto> set1 = new LinkedHashSet<BookingAddonDto>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( BookingAddon bookingAddon : set ) {
            set1.add( toBookingAddonDto( bookingAddon ) );
        }

        return set1;
    }

    protected Set<BookingAddon> bookingAddonDtoSetToBookingAddonSet(Set<BookingAddonDto> set) {
        if ( set == null ) {
            return null;
        }

        Set<BookingAddon> set1 = new LinkedHashSet<BookingAddon>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( BookingAddonDto bookingAddonDto : set ) {
            set1.add( toBookingAddon( bookingAddonDto ) );
        }

        return set1;
    }

    private Long bookingAddonAddonId(BookingAddon bookingAddon) {
        if ( bookingAddon == null ) {
            return null;
        }
        ServiceAddon addon = bookingAddon.getAddon();
        if ( addon == null ) {
            return null;
        }
        Long id = addon.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String bookingAddonAddonName(BookingAddon bookingAddon) {
        if ( bookingAddon == null ) {
            return null;
        }
        ServiceAddon addon = bookingAddon.getAddon();
        if ( addon == null ) {
            return null;
        }
        String name = addon.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String bookingAddonAddonDescription(BookingAddon bookingAddon) {
        if ( bookingAddon == null ) {
            return null;
        }
        ServiceAddon addon = bookingAddon.getAddon();
        if ( addon == null ) {
            return null;
        }
        String description = addon.getDescription();
        if ( description == null ) {
            return null;
        }
        return description;
    }
}
