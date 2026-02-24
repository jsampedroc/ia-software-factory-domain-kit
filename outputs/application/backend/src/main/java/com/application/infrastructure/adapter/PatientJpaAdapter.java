package com.application.infrastructure.adapter;

import com.application.domain.model.Patient;
import com.application.domain.port.PatientRepositoryPort;
import com.application.domain.valueobject.PatientId;
import com.application.infrastructure.entity.PatientEntity;
import com.application.infrastructure.repository.PatientJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PatientJpaAdapter implements PatientRepositoryPort {

    private final PatientJpaRepository patientJpaRepository;

    @Override
    public Patient save(Patient patient) {
        PatientEntity entity = PatientEntity.fromDomain(patient);
        PatientEntity savedEntity = patientJpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Patient> findById(PatientId patientId) {
        return patientJpaRepository.findById(patientId.getValue())
                .map(PatientEntity::toDomain);
    }

    @Override
    public List<Patient> findAll() {
        return patientJpaRepository.findAll().stream()
                .map(PatientEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(PatientId patientId) {
        patientJpaRepository.deleteById(patientId.getValue());
    }

    @Override
    public boolean existsById(PatientId patientId) {
        return patientJpaRepository.existsById(patientId.getValue());
    }

    @Override
    public Optional<Patient> findByDni(String dni) {
        return patientJpaRepository.findByDni(dni)
                .map(PatientEntity::toDomain);
    }

    @Override
    public List<Patient> findByActive(boolean active) {
        return patientJpaRepository.findByActive(active).stream()
                .map(PatientEntity::toDomain)
                .collect(Collectors.toList());
    }
}