package com.application.domain.port;

import com.application.domain.model.Dentist;
import com.application.domain.valueobject.ClinicId;
import com.application.domain.valueobject.DentistId;
import com.application.domain.valueobject.SpecialtyId;
import com.application.domain.shared.EntityRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DentistRepositoryPort extends EntityRepository<Dentist, DentistId> {
    Optional<Dentist> findByMedicalLicense(String medicalLicense);
    List<Dentist> findByClinicId(ClinicId clinicId);
    List<Dentist> findBySpecialtyId(SpecialtyId specialtyId);
    List<Dentist> findActiveDentists();
    List<Dentist> findDentistsAvailableForAppointment(ClinicId clinicId, LocalDateTime dateTime);
    boolean existsByMedicalLicense(String medicalLicense);
}