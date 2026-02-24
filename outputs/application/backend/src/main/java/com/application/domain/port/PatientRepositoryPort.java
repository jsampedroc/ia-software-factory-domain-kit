package com.application.domain.port;

import com.application.domain.model.Patient;
import com.application.domain.valueobject.PatientId;
import com.application.domain.shared.EntityRepository;

public interface PatientRepositoryPort extends EntityRepository<Patient, PatientId> {
}