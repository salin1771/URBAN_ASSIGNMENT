package com.urbanservices.booking.service;

import com.urbanservices.booking.dto.BaseDto;
import com.urbanservices.booking.exception.ResourceNotFoundException;
import com.urbanservices.booking.model.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

public interface BaseService<D extends BaseDto, E extends BaseEntity, ID extends Serializable> {
    
    D create(D dto);
    
    D update(ID id, D dto) throws ResourceNotFoundException;
    
    D getById(ID id) throws ResourceNotFoundException;
    
    void delete(ID id) throws ResourceNotFoundException;
    
    Page<D> findAll(Pageable pageable);
    
    List<D> findAll();
    
    boolean exists(ID id);
    
    long count();
    
    D toDto(E entity);
    
    E toEntity(D dto);
    
    void updateEntityFromDto(D dto, E entity);
}
