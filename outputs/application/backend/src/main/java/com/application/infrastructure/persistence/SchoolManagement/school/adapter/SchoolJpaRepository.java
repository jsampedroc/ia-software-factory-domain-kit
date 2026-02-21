package com.application.infrastructure.persistence.SchoolManagement.school.adapter;

import com.application.infrastructure.persistence.SchoolManagement.school.entity.SchoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SchoolJpaRepository extends JpaRepository<SchoolEntity, UUID> {
    Optional<SchoolEntity> findByName(String name);
    boolean existsByName(String name);
}