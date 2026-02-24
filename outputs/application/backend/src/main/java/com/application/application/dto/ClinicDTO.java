package com.application.application.dto;

import com.application.domain.model.Clinic;
import com.application.domain.valueobject.ClinicId;

import java.time.LocalTime;
import java.util.UUID;

public record ClinicDTO(
        UUID id,
        String codigo,
        String nombre,
        String direccion,
        String telefono,
        String email,
        LocalTime horarioApertura,
        LocalTime horarioCierre,
        Boolean activa
) {
    public static ClinicDTO fromDomain(Clinic clinic) {
        return new ClinicDTO(
                clinic.getId().value(),
                clinic.getCodigo(),
                clinic.getNombre(),
                clinic.getDireccion(),
                clinic.getTelefono(),
                clinic.getEmail(),
                clinic.getHorarioApertura(),
                clinic.getHorarioCierre(),
                clinic.getActiva()
        );
    }

    public Clinic toDomain() {
        return Clinic.builder()
                .id(ClinicId.of(id))
                .codigo(codigo)
                .nombre(nombre)
                .direccion(direccion)
                .telefono(telefono)
                .email(email)
                .horarioApertura(horarioApertura)
                .horarioCierre(horarioCierre)
                .activa(activa)
                .build();
    }
}