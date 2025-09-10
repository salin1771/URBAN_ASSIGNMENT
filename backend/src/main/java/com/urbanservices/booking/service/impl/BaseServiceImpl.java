package com.urbanservices.booking.service.impl;

import com.urbanservices.booking.dto.BaseDto;
import com.urbanservices.booking.exception.ResourceNotFoundException;
import com.urbanservices.booking.mapper.BaseMapper;
import com.urbanservices.booking.model.BaseEntity;
import com.urbanservices.booking.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
public abstract class BaseServiceImpl<D extends BaseDto, E extends BaseEntity, ID extends Serializable> 
        implements BaseService<D, E, ID> {

    protected final JpaRepository<E, ID> repository;
    protected final BaseMapper<D, E> mapper;
    protected final Class<E> entityClass;

    public BaseServiceImpl(JpaRepository<E, ID> repository, BaseMapper<D, E> mapper, Class<E> entityClass) {
        this.repository = repository;
        this.mapper = mapper;
        this.entityClass = entityClass;
    }

    @Override
    public D create(D dto) {
        E entity = toEntity(dto);
        E savedEntity = repository.save(entity);
        return toDto(savedEntity);
    }

    @Override
    public D update(ID id, D dto) throws ResourceNotFoundException {
        return repository.findById(id)
                .map(existingEntity -> {
                    updateEntityFromDto(dto, existingEntity);
                    E updatedEntity = repository.save(existingEntity);
                    return toDto(updatedEntity);
                })
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("%s not found with id: %s", entityClass.getSimpleName(), id)
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public D getById(ID id) throws ResourceNotFoundException {
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("%s not found with id: %s", entityClass.getSimpleName(), id)
                ));
    }

    @Override
    public void delete(ID id) throws ResourceNotFoundException {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(
                    String.format("%s not found with id: %s", entityClass.getSimpleName(), id)
            );
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<D> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<D> findAll() {
        return repository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(ID id) {
        return repository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }

    @Override
    public D toDto(E entity) {
        return mapper.toDto(entity);
    }

    @Override
    public E toEntity(D dto) {
        return mapper.toEntity(dto);
    }

    @Override
    public void updateEntityFromDto(D dto, E entity) {
        mapper.updateEntityFromDto(dto, entity);
    }
}
