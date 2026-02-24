package com.application.domain.model;

import com.application.domain.shared.Entity;
import com.application.domain.valueobject.DentistId;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class Dentist extends Entity<DentistId> {
    private String licenciaMedica;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private LocalDate fechaContratacion;
    private Boolean activo;

    private Dentist(DentistId id,
                    String licenciaMedica,
                    String nombre,
                    String apellido,
                    String telefono,
                    String email,
                    LocalDate fechaContratacion,
                    Boolean activo) {
        super(id);
        this.licenciaMedica = licenciaMedica;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.fechaContratacion = fechaContratacion;
        this.activo = activo;
        validate();
    }

    public static Dentist create(DentistId id,
                                 String licenciaMedica,
                                 String nombre,
                                 String apellido,
                                 String telefono,
                                 String email,
                                 LocalDate fechaContratacion) {
        return new Dentist(id, licenciaMedica, nombre, apellido, telefono, email, fechaContratacion, true);
    }

    public void update(String licenciaMedica,
                       String nombre,
                       String apellido,
                       String telefono,
                       String email,
                       LocalDate fechaContratacion) {
        this.licenciaMedica = licenciaMedica;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.fechaContratacion = fechaContratacion;
        validate();
    }

    public void activate() {
        this.activo = true;
    }

    public void deactivate() {
        this.activo = false;
    }

    public boolean isActive() {
        return Boolean.TRUE.equals(activo);
    }

    private void validate() {
        if (licenciaMedica == null || licenciaMedica.isBlank()) {
            throw new IllegalArgumentException("La licencia médica es obligatoria");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (apellido == null || apellido.isBlank()) {
            throw new IllegalArgumentException("El apellido es obligatorio");
        }
        if (telefono == null || telefono.isBlank()) {
            throw new IllegalArgumentException("El teléfono es obligatorio");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }
        if (fechaContratacion == null) {
            throw new IllegalArgumentException("La fecha de contratación es obligatoria");
        }
        if (fechaContratacion.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de contratación no puede ser futura");
        }
    }
}