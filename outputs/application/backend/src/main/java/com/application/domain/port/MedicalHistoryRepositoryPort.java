package com.application.domain.port;

import com.application.domain.model.MedicalHistory;
import com.application.domain.valueobject.MedicalHistoryId;
import com.application.domain.valueobject.PatientId;
import com.application.domain.shared.EntityRepository;

import java.util.Optional;

public interface MedicalHistoryRepositoryPort extends EntityRepository<MedicalHistory, MedicalHistoryId> {
    Optional<MedicalHistory> findByPatientId(PatientId patientId);
    boolean existsByPatientId(PatientId patientId);
}