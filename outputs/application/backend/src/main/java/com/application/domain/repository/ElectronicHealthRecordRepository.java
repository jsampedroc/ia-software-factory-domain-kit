package com.application.domain.repository;

import com.application.domain.shared.EntityRepository;
import com.application.domain.model.ElectronicHealthRecord;
import com.application.domain.valueobject.EhrId;
import com.application.domain.valueobject.PatientId;

import java.util.Optional;

public interface ElectronicHealthRecordRepository extends EntityRepository<ElectronicHealthRecord, EhrId> {
    Optional<ElectronicHealthRecord> findByPatientId(PatientId patientId);
    boolean existsByPatientId(PatientId patientId);
}