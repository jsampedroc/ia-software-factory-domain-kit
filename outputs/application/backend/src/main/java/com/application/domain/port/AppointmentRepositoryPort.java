package com.application.domain.port;

import com.application.domain.model.Appointment;
import com.application.domain.valueobject.AppointmentId;
import com.application.domain.valueobject.DentistId;
import com.application.domain.valueobject.PatientId;
import com.application.domain.shared.EntityRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepositoryPort extends EntityRepository<Appointment, AppointmentId> {

    List<Appointment> findByPatientId(PatientId patientId);

    List<Appointment> findByDentistId(DentistId dentistId);

    List<Appointment> findByStatus(String status);

    List<Appointment> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    List<Appointment> findByPatientIdAndDateRange(PatientId patientId, LocalDateTime startDate, LocalDateTime endDate);

    List<Appointment> findByDentistIdAndDateRange(DentistId dentistId, LocalDateTime startDate, LocalDateTime endDate);

    Optional<Appointment> findOverlappingAppointment(DentistId dentistId, LocalDateTime startTime, LocalDateTime endTime);

    boolean existsByPatientIdAndStatusIn(PatientId patientId, List<String> statuses);

    long countByPatientIdAndStatus(PatientId patientId, String status);
}