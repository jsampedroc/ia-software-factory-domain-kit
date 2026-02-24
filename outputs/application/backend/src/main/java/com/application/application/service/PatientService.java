package com.application.application.service;

import com.application.domain.model.Patient;
import com.application.domain.valueobject.PatientId;
import com.application.domain.port.PatientRepositoryPort;
import com.application.application.dto.PatientDTO;
import com.application.domain.exception.DomainException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PatientService {

    private final PatientRepositoryPort patientRepositoryPort;

    @Transactional
    public PatientDTO createPatient(PatientDTO patientDTO) {
        validatePatientData(patientDTO);
        checkDuplicateDni(patientDTO.dni(), patientDTO.email());

        Patient patient = Patient.create(
                patientDTO.dni(),
                patientDTO.nombre(),
                patientDTO.apellido(),
                patientDTO.fechaNacimiento(),
                patientDTO.telefono(),
                patientDTO.email(),
                patientDTO.direccion()
        );

        Patient savedPatient = patientRepositoryPort.save(patient);
        return mapToDTO(savedPatient);
    }

    public PatientDTO getPatientById(PatientId patientId) {
        return patientRepositoryPort.findById(patientId)
                .map(this::mapToDTO)
                .orElseThrow(() -> new DomainException("Paciente no encontrado con ID: " + patientId));
    }

    public List<PatientDTO> getAllPatients() {
        return patientRepositoryPort.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PatientDTO> getActivePatients() {
        return patientRepositoryPort.findByActive(true).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PatientDTO updatePatient(PatientId patientId, PatientDTO patientDTO) {
        Patient existingPatient = patientRepositoryPort.findById(patientId)
                .orElseThrow(() -> new DomainException("Paciente no encontrado con ID: " + patientId));

        validatePatientData(patientDTO);
        checkDuplicateDniForUpdate(patientDTO.dni(), patientDTO.email(), patientId);

        existingPatient.update(
                patientDTO.dni(),
                patientDTO.nombre(),
                patientDTO.apellido(),
                patientDTO.fechaNacimiento(),
                patientDTO.telefono(),
                patientDTO.email(),
                patientDTO.direccion(),
                patientDTO.activo()
        );

        Patient updatedPatient = patientRepositoryPort.save(existingPatient);
        return mapToDTO(updatedPatient);
    }

    @Transactional
    public void deactivatePatient(PatientId patientId) {
        Patient patient = patientRepositoryPort.findById(patientId)
                .orElseThrow(() -> new DomainException("Paciente no encontrado con ID: " + patientId));

        patient.deactivate();
        patientRepositoryPort.save(patient);
    }

    @Transactional
    public void archiveInactivePatients() {
        LocalDateTime twoYearsAgo = LocalDateTime.now().minusYears(2);
        List<Patient> inactivePatients = patientRepositoryPort.findByActive(false).stream()
                .filter(patient -> patient.getFechaRegistro().isBefore(twoYearsAgo))
                .collect(Collectors.toList());

        inactivePatients.forEach(Patient::archive);
        patientRepositoryPort.saveAll(inactivePatients);
    }

    public boolean canScheduleNewAppointment(PatientId patientId) {
        Patient patient = patientRepositoryPort.findById(patientId)
                .orElseThrow(() -> new DomainException("Paciente no encontrado con ID: " + patientId));

        if (!patient.isActivo()) {
            return false;
        }

        // Verificar si tiene facturas vencidas por más de 60 días
        boolean hasOverdueInvoices = patient.getInvoices().stream()
                .anyMatch(invoice -> invoice.isVencida() && invoice.getDaysOverdue() > 60);

        return !hasOverdueInvoices;
    }

    public int calculateAge(PatientId patientId) {
        Patient patient = patientRepositoryPort.findById(patientId)
                .orElseThrow(() -> new DomainException("Paciente no encontrado con ID: " + patientId));

        LocalDate birthDate = patient.getFechaNacimiento();
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }

    private void validatePatientData(PatientDTO patientDTO) {
        if (patientDTO.dni() == null || patientDTO.dni().trim().isEmpty()) {
            throw new DomainException("El DNI es obligatorio");
        }

        if (patientDTO.nombre() == null || patientDTO.nombre().trim().isEmpty()) {
            throw new DomainException("El nombre es obligatorio");
        }

        if (patientDTO.apellido() == null || patientDTO.apellido().trim().isEmpty()) {
            throw new DomainException("El apellido es obligatorio");
        }

        if (patientDTO.fechaNacimiento() == null) {
            throw new DomainException("La fecha de nacimiento es obligatoria");
        }

        if (patientDTO.fechaNacimiento().isAfter(LocalDate.now().minusYears(1))) {
            throw new DomainException("El paciente debe tener al menos 1 año de edad");
        }

        if (patientDTO.email() != null && !patientDTO.email().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new DomainException("El formato del email no es válido");
        }
    }

    private void checkDuplicateDni(String dni, String email) {
        Optional<Patient> existingByDni = patientRepositoryPort.findByDni(dni);
        if (existingByDni.isPresent()) {
            throw new DomainException("Ya existe un paciente con el DNI: " + dni);
        }

        if (email != null && !email.trim().isEmpty()) {
            Optional<Patient> existingByEmail = patientRepositoryPort.findByEmail(email);
            if (existingByEmail.isPresent()) {
                throw new DomainException("Ya existe un paciente con el email: " + email);
            }
        }
    }

    private void checkDuplicateDniForUpdate(String dni, String email, PatientId currentPatientId) {
        Optional<Patient> existingByDni = patientRepositoryPort.findByDni(dni);
        if (existingByDni.isPresent() && !existingByDni.get().getId().equals(currentPatientId)) {
            throw new DomainException("Ya existe otro paciente con el DNI: " + dni);
        }

        if (email != null && !email.trim().isEmpty()) {
            Optional<Patient> existingByEmail = patientRepositoryPort.findByEmail(email);
            if (existingByEmail.isPresent() && !existingByEmail.get().getId().equals(currentPatientId)) {
                throw new DomainException("Ya existe otro paciente con el email: " + email);
            }
        }
    }

    private PatientDTO mapToDTO(Patient patient) {
        return new PatientDTO(
                patient.getId(),
                patient.getDni(),
                patient.getNombre(),
                patient.getApellido(),
                patient.getFechaNacimiento(),
                patient.getTelefono(),
                patient.getEmail(),
                patient.getDireccion(),
                patient.getFechaRegistro(),
                patient.isActivo()
        );
    }
}