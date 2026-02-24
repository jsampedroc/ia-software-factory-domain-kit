package com.application.infrastructure.repository;

import com.application.infrastructure.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentJpaRepository extends JpaRepository<AppointmentEntity, UUID> {

    List<AppointmentEntity> findByPatientId(UUID patientId);

    List<AppointmentEntity> findByDentistId(UUID dentistId);

    List<AppointmentEntity> findByClinicId(UUID clinicId);

    List<AppointmentEntity> findByConsultingRoomId(UUID consultingRoomId);

    List<AppointmentEntity> findByEstado(String estado);

    @Query("SELECT a FROM AppointmentEntity a WHERE a.dentistId = :dentistId AND a.fechaHora BETWEEN :start AND :end")
    List<AppointmentEntity> findByDentistIdAndDateRange(
            @Param("dentistId") UUID dentistId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("SELECT a FROM AppointmentEntity a WHERE a.patientId = :patientId AND a.estado IN :estados")
    List<AppointmentEntity> findByPatientIdAndEstadoIn(
            @Param("patientId") UUID patientId,
            @Param("estados") List<String> estados);

    @Query("SELECT a FROM AppointmentEntity a WHERE a.clinicId = :clinicId AND a.fechaHora BETWEEN :start AND :end")
    List<AppointmentEntity> findByClinicIdAndDateRange(
            @Param("clinicId") UUID clinicId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(a) FROM AppointmentEntity a WHERE a.patientId = :patientId AND a.estado IN :estados")
    long countByPatientIdAndEstadoIn(
            @Param("patientId") UUID patientId,
            @Param("estados") List<String> estados);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM AppointmentEntity a " +
           "WHERE a.dentistId = :dentistId AND a.estado NOT IN ('COMPLETADA', 'CANCELADA') " +
           "AND a.fechaHora <= :endTime AND (a.fechaHora + (a.duracionMinutos * INTERVAL '1 minute')) > :startTime")
    boolean existsOverlappingAppointmentForDentist(
            @Param("dentistId") UUID dentistId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
}