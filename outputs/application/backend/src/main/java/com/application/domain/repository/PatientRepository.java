package com.application.domain.repository;

import com.application.domain.model.Patient;
import com.application.domain.shared.EntityRepository;
import com.application.domain.valueobject.PatientId;
import com.application.domain.enums.PatientStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PatientRepository extends EntityRepository<PatientId, Patient> {

    Optional<Patient> findById(PatientId patientId);

    List<Patient> findAll();

    List<Patient> findByStatus(PatientStatus status);

    Optional<Patient> findByIdentityDetails(String firstName, String lastName, LocalDate dateOfBirth);

    boolean existsByIdentityDetails(String firstName, String lastName, LocalDate dateOfBirth);

    Patient save(Patient patient);

    void delete(Patient patient);
}