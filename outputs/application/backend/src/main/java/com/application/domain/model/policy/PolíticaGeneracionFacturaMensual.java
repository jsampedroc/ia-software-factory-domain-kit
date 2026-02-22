package com.application.domain.model.policy;

import com.application.domain.shared.Entity;
import com.application.domain.shared.ValueObject;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

public record PolíticaGeneracionFacturaMensualId(UUID value) implements ValueObject {}

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PolíticaGeneracionFacturaMensual extends Entity<PolíticaGeneracionFacturaMensualId> {
    private String descripcion;
    private LocalDate ultimaEjecucion;
    private boolean activa;

    public PolíticaGeneracionFacturaMensual(PolíticaGeneracionFacturaMensualId id, String descripcion, LocalDate ultimaEjecucion, boolean activa) {
        super(id);
        this.descripcion = descripcion;
        this.ultimaEjecucion = ultimaEjecucion;
        this.activa = activa;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDate getUltimaEjecucion() {
        return ultimaEjecucion;
    }

    public boolean isActiva() {
        return activa;
    }

    public void marcarComoEjecutada(LocalDate fechaEjecucion) {
        this.ultimaEjecucion = fechaEjecucion;
    }

    public void activar() {
        this.activa = true;
    }

    public void desactivar() {
        this.activa = false;
    }
}