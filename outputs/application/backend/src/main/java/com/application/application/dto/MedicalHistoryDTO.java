package com.application.application.dto;

import com.application.domain.valueobject.MedicalHistoryId;
import com.application.domain.valueobject.PatientId;
import java.time.LocalDateTime;

public record MedicalHistoryDTO(
        MedicalHistoryId historyId,
        PatientId patientId,
        LocalDateTime fechaCreacion,
        LocalDateTime ultimaActualizacion,
        String alergias,
        String condicionesMedicas,
        String medicamentos,
        String observacionesGenerales
) {}