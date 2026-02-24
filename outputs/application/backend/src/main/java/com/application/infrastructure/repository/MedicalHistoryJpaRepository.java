package com.application.infrastructure.repository;

import com.application.infrastructure.entity.MedicalHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MedicalHistoryJpaRepository extends JpaRepository<MedicalHistoryEntity, UUID> {
}