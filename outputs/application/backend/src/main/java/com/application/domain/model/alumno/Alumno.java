package com.application.domain.model.alumno;

import com.application.domain.shared.Entity;
import com.application.domain.valueobject.alumno.DocumentoIdentidad;
import com.application.domain.model.event.AlumnoMatriculado;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Alumno extends Entity<Alumno.AlumnoId> {

    private String numeroMatricula;
    private String nombre;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private LocalDate fechaAlta;
    private boolean activo;
    private Set<Tutor> tutores = new HashSet<>();

    protected Alumno() {
        // Constructor protegido para JPA/Hibernate y frameworks
    }

    private Alumno(AlumnoId id, String numeroMatricula, String nombre, String apellidos,
                   LocalDate fechaNacimiento, LocalDate fechaAlta, boolean activo) {
        super(id);
        this.numeroMatricula = numeroMatricula;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaAlta = fechaAlta;
        this.activo = activo;
    }

    public static Alumno crear(String numeroMatricula, String nombre, String apellidos,
                               LocalDate fechaNacimiento, LocalDate fechaAlta) {
        // Validaciones de negocio
        if (fechaAlta.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de alta no puede ser futura.");
        }

        AlumnoId id = new AlumnoId(java.util.UUID.randomUUID());
        Alumno alumno = new Alumno(id, numeroMatricula, nombre, apellidos, fechaNacimiento, fechaAlta, true);
        // Evento de dominio podría registrarse aquí si la creación implica matrícula inmediata
        // alumno.registerEvent(new AlumnoCreado(...));
        return alumno;
    }

    public void matricularEnClase(String claseId) {
        if (!this.activo) {
            throw new IllegalStateException("No se puede matricular a un alumno inactivo.");
        }
        if (this.tutores.isEmpty()) {
            throw new IllegalStateException("Un alumno debe tener al menos un tutor asociado al darse de alta.");
        }
        // Lógica de matriculación...
        this.registerEvent(new AlumnoMatriculado(this.getId().value(), this.numeroMatricula, LocalDate.now(), claseId));
    }

    public void agregarTutor(Tutor tutor) {
        this.tutores.add(tutor);
    }

    public void desactivar() {
        this.activo = false;
    }

    // Getters
    public String getNumeroMatricula() { return numeroMatricula; }
    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public LocalDate getFechaAlta() { return fechaAlta; }
    public boolean isActivo() { return activo; }
    public Set<Tutor> getTutores() { return new HashSet<>(tutores); }

    // ID como Value Object
    public record AlumnoId(java.util.UUID value) implements com.application.domain.shared.ValueObject {}
}