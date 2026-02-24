package com.application.domain.model;

import com.application.domain.shared.Entity;
import com.application.domain.valueobject.SpecialtyId;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class Specialty extends Entity<SpecialtyId> {
    private String codigo;
    private String nombre;
    private String descripcion;

    public Specialty(SpecialtyId id, String codigo, String nombre, String descripcion) {
        super(id);
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        validate();
    }

    private void validate() {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código de la especialidad no puede estar vacío");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la especialidad no puede estar vacío");
        }
    }

    public void update(String nombre, String descripcion) {
        if (nombre != null && !nombre.isBlank()) {
            this.nombre = nombre;
        }
        if (descripcion != null) {
            this.descripcion = descripcion;
        }
        validate();
    }
}