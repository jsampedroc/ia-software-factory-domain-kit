package com.application.domain.model;

import com.application.domain.shared.Entity;
import com.application.domain.valueobject.ClinicId;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class Clinic extends Entity<ClinicId> {
    private String codigo;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private LocalTime horarioApertura;
    private LocalTime horarioCierre;
    private Boolean activa;

    private Clinic(ClinicId id,
                   String codigo,
                   String nombre,
                   String direccion,
                   String telefono,
                   String email,
                   LocalTime horarioApertura,
                   LocalTime horarioCierre,
                   Boolean activa) {
        super(id);
        this.codigo = codigo;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.horarioApertura = horarioApertura;
        this.horarioCierre = horarioCierre;
        this.activa = activa;
        validate();
    }

    public static Clinic create(ClinicId id,
                                String codigo,
                                String nombre,
                                String direccion,
                                String telefono,
                                String email,
                                LocalTime horarioApertura,
                                LocalTime horarioCierre) {
        return new Clinic(id, codigo, nombre, direccion, telefono, email, horarioApertura, horarioCierre, true);
    }

    public static Clinic restore(ClinicId id,
                                 String codigo,
                                 String nombre,
                                 String direccion,
                                 String telefono,
                                 String email,
                                 LocalTime horarioApertura,
                                 LocalTime horarioCierre,
                                 Boolean activa) {
        return new Clinic(id, codigo, nombre, direccion, telefono, email, horarioApertura, horarioCierre, activa);
    }

    public void updateInformation(String nombre,
                                  String direccion,
                                  String telefono,
                                  String email,
                                  LocalTime horarioApertura,
                                  LocalTime horarioCierre) {
        this.nombre = Objects.requireNonNull(nombre, "Nombre no puede ser nulo");
        this.direccion = Objects.requireNonNull(direccion, "Dirección no puede ser nula");
        this.telefono = Objects.requireNonNull(telefono, "Teléfono no puede ser nulo");
        this.email = Objects.requireNonNull(email, "Email no puede ser nulo");
        this.horarioApertura = Objects.requireNonNull(horarioApertura, "Horario de apertura no puede ser nulo");
        this.horarioCierre = Objects.requireNonNull(horarioCierre, "Horario de cierre no puede ser nulo");
        validate();
    }

    public void activate() {
        this.activa = true;
    }

    public void deactivate() {
        this.activa = false;
    }

    public boolean isOperationalAt(LocalTime time) {
        Objects.requireNonNull(time, "Hora no puede ser nula");
        return activa && !time.isBefore(horarioApertura) && !time.isAfter(horarioCierre);
    }

    private void validate() {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código de clínica es obligatorio");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Nombre de clínica es obligatorio");
        }
        if (direccion == null || direccion.isBlank()) {
            throw new IllegalArgumentException("Dirección de clínica es obligatoria");
        }
        if (telefono == null || telefono.isBlank()) {
            throw new IllegalArgumentException("Teléfono de clínica es obligatorio");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email de clínica es obligatorio");
        }
        if (horarioApertura == null) {
            throw new IllegalArgumentException("Horario de apertura es obligatorio");
        }
        if (horarioCierre == null) {
            throw new IllegalArgumentException("Horario de cierre es obligatorio");
        }
        if (horarioCierre.isBefore(horarioApertura) || horarioCierre.equals(horarioApertura)) {
            throw new IllegalArgumentException("Horario de cierre debe ser posterior al horario de apertura");
        }
        if (activa == null) {
            throw new IllegalArgumentException("Estado activa no puede ser nulo");
        }
    }
}