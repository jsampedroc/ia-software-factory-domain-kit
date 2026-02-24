package com.application.application.dto;

import com.application.domain.valueobject.PatientId;
import com.application.domain.model.Patient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record PatientDTO(
        UUID patientId,
        String dni,
        String nombre,
        String apellido,
        LocalDate fechaNacimiento,
        String telefono,
        String email,
        String direccion,
        LocalDateTime fechaRegistro,
        Boolean activo
) {
    public static PatientDTO fromDomain(Patient patient) {
        return new PatientDTO(
                patient.getId().value(),
                patient.getDni(),
                patient.getNombre(),
                patient.getApellido(),
                patient.getFechaNacimiento(),
                patient.getTelefono(),
                patient.getEmail(),
                patient.getDireccion(),
                patient.getFechaRegistro(),
                patient.getActivo()
        );
    }

    public Patient toDomain() {
        return new Patient(
                new PatientId(patientId),
                dni,
                nombre,
                apellido,
                fechaNacimiento,
                telefono,
                email,
                direccion,
                fechaRegistro,
                activo
        );
    }
}