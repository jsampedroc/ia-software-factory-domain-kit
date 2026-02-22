package com.application.domain.model.facturacion;

import com.application.domain.shared.Entity;
import com.application.domain.valueobject.facturacion.Dinero;
import com.application.domain.valueobject.facturacion.Periodo;

import java.time.LocalDate;

public class Tarifa extends Entity<Tarifa.TarifaId> {
    private String nombre;
    private String descripcion;
    private Dinero precioMensual;
    private boolean activo;
    private Periodo periodoVigencia;

    public Tarifa(TarifaId id, String nombre, String descripcion, Dinero precioMensual, boolean activo, Periodo periodoVigencia) {
        super(id);
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioMensual = precioMensual;
        this.activo = activo;
        this.periodoVigencia = periodoVigencia;
    }

    protected Tarifa() {
        super();
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Dinero getPrecioMensual() {
        return precioMensual;
    }

    public boolean isActivo() {
        return activo;
    }

    public Periodo getPeriodoVigencia() {
        return periodoVigencia;
    }

    public void actualizarVigencia(Periodo nuevoPeriodo) {
        this.periodoVigencia = nuevoPeriodo;
    }

    public void activar() {
        this.activo = true;
    }

    public void desactivar() {
        this.activo = false;
    }

    public boolean estaVigenteEnFecha(LocalDate fecha) {
        return periodoVigencia != null &&
               !fecha.isBefore(periodoVigencia.fechaInicio()) &&
               !fecha.isAfter(periodoVigencia.fechaFin());
    }

    public record TarifaId(String value) implements com.application.domain.shared.ValueObject {}
}