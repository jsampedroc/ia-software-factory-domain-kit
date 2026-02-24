package com.application.domain.port;

import com.application.domain.model.Treatment;
import com.application.domain.valueobject.TreatmentId;
import com.application.domain.shared.EntityRepository;

public interface TreatmentRepositoryPort extends EntityRepository<Treatment, TreatmentId> {
}