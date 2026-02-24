package com.application.domain.port;

import com.application.domain.model.Clinic;
import com.application.domain.valueobject.ClinicId;
import com.application.domain.shared.EntityRepository;

public interface ClinicRepositoryPort extends EntityRepository<Clinic, ClinicId> {
}