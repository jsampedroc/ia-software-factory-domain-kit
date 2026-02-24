package com.application.domain.model;

import com.application.domain.shared.Entity;
import com.application.domain.valueobject.PatientId;
import com.application.domain.exception.DomainException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class Patient extends Entity<PatientId> {
    private String dni;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String email;
    private String direccion;
    private LocalDateTime fechaRegistro;
    private Boolean activo;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern DNI_PATTERN = Pattern.compile("^[0-9]{8}[A-Za-z]$");

    public Patient(PatientId id, String dni, String nombre, String apellido, LocalDate fechaNacimiento,
                   String telefono, String email, String direccion, LocalDateTime fechaRegistro, Boolean activo) {
        super(id);
        setDni(dni);
        setNombre(nombre);
        setApellido(apellido);
        setFechaNacimiento(fechaNacimiento);
        setTelefono(telefono);
        setEmail(email);
        setDireccion(direccion);
        setFechaRegistro(fechaRegistro);
        setActivo(activo);
        validateState();
    }

    public static Patient create(String dni, String nombre, String apellido, LocalDate fechaNacimiento,
                                 String telefono, String email, String direccion) {
        PatientId id = PatientId.generate();
        LocalDateTime now = LocalDateTime.now();
        return new Patient(id, dni, nombre, apellido, fechaNacimiento, telefono, email, direccion, now, true);
    }

    public void updatePersonalInfo(String nombre, String apellido, String telefono, String email, String direccion) {
        setNombre(nombre);
        setApellido(apellido);
        setTelefono(telefono);
        setEmail(email);
        setDireccion(direccion);
        validateState();
    }

    public void deactivate() {
        if (!this.activo) {
            throw new DomainException("El paciente ya se encuentra inactivo.");
        }
        this.activo = false;
    }

    public void activate() {
        if (this.activo) {
            throw new DomainException("El paciente ya se encuentra activo.");
        }
        this.activo = true;
    }

    public boolean isEligibleForNewAppointments() {
        if (!this.activo) {
            return false;
        }
        return true;
    }

    private void setDni(String dni) {
        if (dni == null || dni.isBlank()) {
            throw new DomainException("El DNI no puede estar vacío.");
        }
        if (!DNI_PATTERN.matcher(dni).matches()) {
            throw new DomainException("Formato de DNI inválido. Debe ser 8 dígitos seguidos de una letra.");
        }
        this.dni = dni.trim().toUpperCase();
    }

    private void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new DomainException("El nombre no puede estar vacío.");
        }
        if (nombre.length() > 100) {
            throw new DomainException("El nombre no puede exceder los 100 caracteres.");
        }
        this.nombre = nombre.trim();
    }

    private void setApellido(String apellido) {
        if (apellido == null || apellido.isBlank()) {
            throw new DomainException("El apellido no puede estar vacío.");
        }
        if (apellido.length() > 100) {
            throw new DomainException("El apellido no puede exceder los 100 caracteres.");
        }
        this.apellido = apellido.trim();
    }

    private void setFechaNacimiento(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            throw new DomainException("La fecha de nacimiento no puede estar vacía.");
        }
        if (fechaNacimiento.isAfter(LocalDate.now().minusYears(1))) {
            throw new DomainException("La fecha de nacimiento no puede ser en el último año.");
        }
        this.fechaNacimiento = fechaNacimiento;
    }

    private void setTelefono(String telefono) {
        if (telefono == null || telefono.isBlank()) {
            throw new DomainException("El teléfono no puede estar vacío.");
        }
        if (telefono.length() > 20) {
            throw new DomainException("El teléfono no puede exceder los 20 caracteres.");
        }
        this.telefono = telefono.trim();
    }

    private void setEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new DomainException("El email no puede estar vacío.");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new DomainException("Formato de email inválido.");
        }
        if (email.length() > 150) {
            throw new DomainException("El email no puede exceder los 150 caracteres.");
        }
        this.email = email.trim().toLowerCase();
    }

    private void setDireccion(String direccion) {
        if (direccion == null || direccion.isBlank()) {
            throw new DomainException("La dirección no puede estar vacía.");
        }
        if (direccion.length() > 255) {
            throw new DomainException("La dirección no puede exceder los 255 caracteres.");
        }
        this.direccion = direccion.trim();
    }

    private void setFechaRegistro(LocalDateTime fechaRegistro) {
        if (fechaRegistro == null) {
            throw new DomainException("La fecha de registro no puede estar vacía.");
        }
        if (fechaRegistro.isAfter(LocalDateTime.now())) {
            throw new DomainException("La fecha de registro no puede ser futura.");
        }
        this.fechaRegistro = fechaRegistro;
    }

    private void setActivo(Boolean activo) {
        if (activo == null) {
            throw new DomainException("El estado activo no puede ser nulo.");
        }
        this.activo = activo;
    }

    private void validateState() {
        if (fechaNacimiento != null && fechaRegistro != null && fechaNacimiento.isAfter(fechaRegistro.toLocalDate())) {
            throw new DomainException("La fecha de nacimiento no puede ser posterior a la fecha de registro.");
        }
    }
}