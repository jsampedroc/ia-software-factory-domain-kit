package com.application.domain.model;

import com.application.domain.shared.Entity;
import com.application.domain.valueobject.TreatmentId;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class Treatment extends Entity<TreatmentId> {
    private String codigo;
    private String nombre;
    private String descripcion;
    private Integer duracionEstimadaMinutos;
    private BigDecimal costoBase;
    private Boolean activo;

    private Treatment(TreatmentId id,
                     String codigo,
                     String nombre,
                     String descripcion,
                     Integer duracionEstimadaMinutos,
                     BigDecimal costoBase,
                     Boolean activo) {
        super(id);
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracionEstimadaMinutos = duracionEstimadaMinutos;
        this.costoBase = costoBase;
        this.activo = activo;
        validate();
    }

    public static Treatment create(TreatmentId id,
                                   String codigo,
                                   String nombre,
                                   String descripcion,
                                   Integer duracionEstimadaMinutos,
                                   BigDecimal costoBase) {
        return new Treatment(id, codigo, nombre, descripcion, duracionEstimadaMinutos, costoBase, true);
    }

    public static Treatment withId(TreatmentId id,
                                   String codigo,
                                   String nombre,
                                   String descripcion,
                                   Integer duracionEstimadaMinutos,
                                   BigDecimal costoBase,
                                   Boolean activo) {
        return new Treatment(id, codigo, nombre, descripcion, duracionEstimadaMinutos, costoBase, activo);
    }

    public void update(String nombre,
                       String descripcion,
                       Integer duracionEstimadaMinutos,
                       BigDecimal costoBase) {
        if (!this.activo) {
            throw new IllegalStateException("Cannot update an inactive treatment");
        }
        this.nombre = Objects.requireNonNull(nombre, "Nombre cannot be null");
        this.descripcion = Objects.requireNonNull(descripcion, "Descripcion cannot be null");
        this.duracionEstimadaMinutos = Objects.requireNonNull(duracionEstimadaMinutos, "DuracionEstimadaMinutos cannot be null");
        this.costoBase = Objects.requireNonNull(costoBase, "CostoBase cannot be null");
        validate();
    }

    public void deactivate() {
        this.activo = false;
    }

    public void activate() {
        this.activo = true;
    }

    private void validate() {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Treatment code cannot be null or blank");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Treatment name cannot be null or blank");
        }
        if (duracionEstimadaMinutos == null || duracionEstimadaMinutos <= 0) {
            throw new IllegalArgumentException("Estimated duration must be positive");
        }
        if (costoBase == null || costoBase.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Base cost cannot be negative");
        }
    }
}