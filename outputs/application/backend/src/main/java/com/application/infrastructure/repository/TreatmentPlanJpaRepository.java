package com.application.infrastructure.repository;

import com.application.infrastructure.entity.TreatmentPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TreatmentPlanJpaRepository extends JpaRepository<TreatmentPlanEntity, UUID> {

    Optional<TreatmentPlanEntity> findByPlanId(UUID planId);

    @Query("SELECT t FROM TreatmentPlanEntity t WHERE t.patient.patientId = :patientId")
    List<TreatmentPlanEntity> findByPatientId(@Param("patientId") UUID patientId);

    @Query("SELECT t FROM TreatmentPlanEntity t WHERE t.dentist.dentistId = :dentistId")
    List<TreatmentPlanEntity> findByDentistId(@Param("dentistId") UUID dentistId);

    @Query("SELECT t FROM TreatmentPlanEntity t WHERE t.status = :status")
    List<TreatmentPlanEntity> findByStatus(@Param("status") String status);

    @Query("SELECT t FROM TreatmentPlanEntity t WHERE t.startDate <= :date AND t.estimatedEndDate >= :date")
    List<TreatmentPlanEntity> findActivePlansByDate(@Param("date") LocalDate date);

    @Query("SELECT t FROM TreatmentPlanEntity t WHERE t.patient.patientId = :patientId AND t.status = :status")
    List<TreatmentPlanEntity> findByPatientIdAndStatus(@Param("patientId") UUID patientId, @Param("status") String status);

    boolean existsByPlanId(UUID planId);
}