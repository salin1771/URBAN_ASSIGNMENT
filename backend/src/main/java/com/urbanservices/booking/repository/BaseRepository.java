package com.urbanservices.booking.repository;

import com.urbanservices.booking.model.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, ID extends Serializable> 
        extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
    // Common repository methods can be added here
}
