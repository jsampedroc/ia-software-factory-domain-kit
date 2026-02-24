package com.application.infrastructure.repository;

import com.application.infrastructure.entity.DentistClinicScheduleEntity;
import com.application.infrastructure.entity.DentistEntity;
import com.application.infrastructure.entity.ClinicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DentistClinicScheduleJpaRepository extends JpaRepository<DentistClinicScheduleEntity, UUID> {

    List<DentistClinicScheduleEntity> findByDentist(DentistEntity dentist);

    List<DentistClinicScheduleEntity> findByClinic(ClinicEntity clinic);

    Optional<DentistClinicScheduleEntity> findByDentistAndClinicAndScheduleDate(DentistEntity dentist, ClinicEntity clinic, LocalDate scheduleDate);

    @Query("SELECT dcs FROM DentistClinicScheduleEntity dcs WHERE dcs.dentist = :dentist AND dcs.scheduleDate BETWEEN :startDate AND :endDate")
    List<DentistClinicScheduleEntity> findByDentistAndDateRange(@Param("dentist") DentistEntity dentist, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT dcs FROM DentistClinicScheduleEntity dcs WHERE dcs.clinic = :clinic AND dcs.scheduleDate BETWEEN :startDate AND :endDate")
    List<DentistClinicScheduleEntity> findByClinicAndDateRange(@Param("clinic") ClinicEntity clinic, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT dcs FROM DentistClinicScheduleEntity dcs WHERE dcs.clinic = :clinic AND dcs.scheduleDate = :date AND dcs.isActive = true")
    List<DentistClinicScheduleEntity> findActiveSchedulesByClinicAndDate(@Param("clinic") ClinicEntity clinic, @Param("date") LocalDate date);

    boolean existsByDentistAndClinicAndScheduleDateAndIsActiveTrue(DentistEntity dentist, ClinicEntity clinic, LocalDate scheduleDate);

    void deleteByDentist(DentistEntity dentist);

    void deleteByClinic(ClinicEntity clinic);
}