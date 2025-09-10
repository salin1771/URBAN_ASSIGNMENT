package com.urbanservices.booking.mapper;

import com.urbanservices.booking.dto.BaseDto;
import com.urbanservices.booking.model.BaseEntity;
import org.mapstruct.MappingTarget;

import java.util.List;

public interface BaseMapper<D extends BaseDto, E extends BaseEntity> {
    
    D toDto(E entity);
    
    E toEntity(D dto);
    
    List<D> toDtoList(List<E> entities);
    
    List<E> toEntityList(List<D> dtos);
    
    void updateEntityFromDto(D dto, @MappingTarget E entity);
}
