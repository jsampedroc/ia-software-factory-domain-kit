package com.application.application.dto;

import com.application.domain.valueobject.DentistId;
import java.time.LocalDate;

public record DentistDTO(
        DentistId dentistId,
        String licenciaMedica,
        String nombre,
        String apellido,
        String telefono,
        String email,
        LocalDate fechaContratacion,
        Boolean activo
) {}