package com.application.domain.port;

import com.application.domain.model.Specialty;
import com.application.domain.valueobject.SpecialtyId;
import com.application.domain.shared.EntityRepository;

public interface SpecialtyRepositoryPort extends EntityRepository<Specialty, SpecialtyId> {
}