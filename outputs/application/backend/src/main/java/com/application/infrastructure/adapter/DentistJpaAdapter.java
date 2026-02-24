package com.application.infrastructure.adapter;

import com.application.domain.model.Dentist;
import com.application.domain.port.DentistRepositoryPort;
import com.application.domain.valueobject.DentistId;
import com.application.infrastructure.entity.DentistEntity;
import com.application.infrastructure.repository.DentistJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DentistJpaAdapter implements DentistRepositoryPort {

    private final DentistJpaRepository dentistJpaRepository;

    @Override
    public Dentist save(Dentist dentist) {
        DentistEntity entity = DentistEntity.fromDomain(dentist);
        DentistEntity savedEntity = dentistJpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Dentist> findById(DentistId dentistId) {
        return dentistJpaRepository.findById(dentistId.value())
                .map(DentistEntity::toDomain);
    }

    @Override
    public List<Dentist> findAll() {
        return dentistJpaRepository.findAll().stream()
                .map(DentistEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Dentist> findAllActive() {
        return dentistJpaRepository.findByActivoTrue().stream()
                .map(DentistEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(DentistId dentistId) {
        dentistJpaRepository.deleteById(dentistId.value());
    }

    @Override
    public boolean existsById(DentistId dentistId) {
        return dentistJpaRepository.existsById(dentistId.value());
    }

    @Override
    public Optional<Dentist> findByMedicalLicense(String medicalLicense) {
        return dentistJpaRepository.findByLicenciaMedica(medicalLicense)
                .map(DentistEntity::toDomain);
    }

    @Override
    public List<Dentist> findByClinicId(String clinicId) {
        return dentistJpaRepository.findByClinicId(clinicId).stream()
                .map(DentistEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Dentist> findBySpecialtyId(String specialtyId) {
        return dentistJpaRepository.findBySpecialtyId(specialtyId).stream()
                .map(DentistEntity::toDomain)
                .collect(Collectors.toList());
    }
}