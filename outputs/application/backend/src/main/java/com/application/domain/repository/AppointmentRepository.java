package com.application.domain.repository;

import com.application.domain.shared.EntityRepository;
import com.application.domain.model.Appointment;
import com.application.domain.valueobject.AppointmentId;
import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.UserId;
import com.application.domain.enums.AppointmentStatus;
import com.application.domain.enums.AppointmentType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends EntityRepository<Appointment, AppointmentId> {

    List<Appointment> findByPatientId(PatientId patientId);

    List<Appointment> findByDentistId(UserId dentistId);

    List<Appointment> findByPatientIdAndStatus(PatientId patientId, AppointmentStatus status);

    List<Appointment> findByDentistIdAndStatus(UserId dentistId, AppointmentStatus status);

    List<Appointment> findByScheduledTimeBetween(LocalDateTime start, LocalDateTime end);

    List<Appointment> findByScheduledDate(LocalDate date);

    List<Appointment> findByScheduledDateAndDentistId(LocalDate date, UserId dentistId);

    List<Appointment> findByTypeAndStatus(AppointmentType type, AppointmentStatus status);

    Optional<Appointment> findByAppointmentId(AppointmentId appointmentId);

    boolean existsByPatientIdAndScheduledTimeBetween(PatientId patientId, LocalDateTime start, LocalDateTime end);

    boolean existsByDentistIdAndScheduledTimeBetween(UserId dentistId, LocalDateTime start, LocalDateTime end);

    long countByPatientIdAndStatusAndScheduledTimeAfter(PatientId patientId, AppointmentStatus status, LocalDateTime since);

    List<Appointment> findOverlappingAppointments(UserId dentistId, LocalDateTime startTime, LocalDateTime endTime);
}