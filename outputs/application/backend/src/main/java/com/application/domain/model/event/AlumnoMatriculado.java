package com.application.domain.model.event;

import com.application.domain.shared.Entity;
import com.application.domain.shared.ValueObject;

import java.time.LocalDate;
import java.util.UUID;

public record AlumnoMatriculado(
        UUID alumnoId,
        String numeroMatricula,
        LocalDate fechaMatricula,
        UUID claseId
) implements ValueObject {
}