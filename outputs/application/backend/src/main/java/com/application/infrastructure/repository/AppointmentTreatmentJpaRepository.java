package com.application.infrastructure.repository;

import com.application.infrastructure.entity.AppointmentTreatmentEntity;
import com.application.infrastructure.entity.AppointmentTreatmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentTreatmentJpaRepository extends JpaRepository<AppointmentTreatmentEntity, AppointmentTreatmentId> {
}