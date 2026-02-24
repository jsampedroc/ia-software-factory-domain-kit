package com.application.infrastructure.repository;

import com.application.infrastructure.entity.DentistSpecialtyEntity;
import com.application.infrastructure.entity.DentistSpecialtyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DentistSpecialtyJpaRepository extends JpaRepository<DentistSpecialtyEntity, DentistSpecialtyId> {
}