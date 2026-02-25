package com.application.domain.repository;

import com.application.domain.model.DentistSchedule;
import com.application.domain.valueobject.DentistScheduleId;
import com.application.domain.valueobject.UserId;
import com.application.domain.shared.EntityRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DentistScheduleRepository extends EntityRepository<DentistScheduleId, DentistSchedule> {
    Optional<DentistSchedule> findByDentistId(UserId dentistId);
    boolean existsByDentistIdAndIsActive(UserId dentistId, boolean isActive);
    boolean isDentistAvailable(UserId dentistId, LocalDate date);
}