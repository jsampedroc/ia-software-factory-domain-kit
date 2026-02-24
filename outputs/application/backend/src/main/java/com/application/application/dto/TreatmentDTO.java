package com.application.application.dto;

import com.application.domain.model.Treatment;
import com.application.domain.valueobject.TreatmentId;
import java.math.BigDecimal;
import java.util.UUID;

public record TreatmentDTO(
        UUID treatmentId,
        String codigo,
        String nombre,
        String descripcion,
        Integer duracionEstimadaMinutos,
        BigDecimal costoBase,
        Boolean activo
) {
    public static TreatmentDTO fromDomain(Treatment treatment) {
        return new TreatmentDTO(
                treatment.getId().value(),
                treatment.getCodigo(),
                treatment.getNombre(),
                treatment.getDescripcion(),
                treatment.getDuracionEstimadaMinutos(),
                treatment.getCostoBase(),
                treatment.getActivo()
        );
    }

    public Treatment toDomain() {
        return Treatment.builder()
                .id(TreatmentId.of(treatmentId))
                .codigo(codigo)
                .nombre(nombre)
                .descripcion(descripcion)
                .duracionEstimadaMinutos(duracionEstimadaMinutos)
                .costoBase(costoBase)
                .activo(activo)
                .build();
    }
}