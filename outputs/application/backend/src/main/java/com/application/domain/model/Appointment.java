package com.application.domain.model;

import com.application.domain.shared.Entity;
import com.application.domain.valueobject.AppointmentId;
import com.application.domain.valueobject.AppointmentStatus;
import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.DentistId;
import com.application.domain.valueobject.ClinicId;
import com.application.domain.valueobject.ConsultingRoomId;
import com.application.domain.valueobject.TreatmentId;
import com.application.domain.exception.DomainException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class Appointment extends Entity<AppointmentId> {

    private LocalDateTime fechaHora;
    private Integer duracionMinutos;
    private AppointmentStatus estado;
    private String motivo;
    private String notas;
    private PatientId pacienteId;
    private DentistId odontologoId;
    private ClinicId clinicaId;
    private ConsultingRoomId consultorioId;
    private Set<TreatmentId> tratamientos;

    public Appointment(
            AppointmentId id,
            LocalDateTime fechaHora,
            Integer duracionMinutos,
            AppointmentStatus estado,
            String motivo,
            String notas,
            PatientId pacienteId,
            DentistId odontologoId,
            ClinicId clinicaId,
            ConsultingRoomId consultorioId,
            Set<TreatmentId> tratamientos
    ) {
        super(id);
        this.fechaHora = fechaHora;
        this.duracionMinutos = duracionMinutos;
        this.estado = estado;
        this.motivo = motivo;
        this.notas = notas;
        this.pacienteId = pacienteId;
        this.odontologoId = odontologoId;
        this.clinicaId = clinicaId;
        this.consultorioId = consultorioId;
        this.tratamientos = tratamientos != null ? new HashSet<>(tratamientos) : new HashSet<>();
        validate();
    }

    public static Appointment create(
            LocalDateTime fechaHora,
            Integer duracionMinutos,
            String motivo,
            String notas,
            PatientId pacienteId,
            DentistId odontologoId,
            ClinicId clinicaId,
            ConsultingRoomId consultorioId,
            Set<TreatmentId> tratamientos
    ) {
        return new Appointment(
                new AppointmentId(UUID.randomUUID()),
                fechaHora,
                duracionMinutos,
                AppointmentStatus.PROGRAMADA,
                motivo,
                notas,
                pacienteId,
                odontologoId,
                clinicaId,
                consultorioId,
                tratamientos
        );
    }

    public void confirmar() {
        if (this.estado != AppointmentStatus.PROGRAMADA) {
            throw new DomainException("Solo las citas PROGRAMADAS pueden ser confirmadas. Estado actual: " + this.estado);
        }
        this.estado = AppointmentStatus.CONFIRMADA;
    }

    public void iniciar() {
        if (this.estado != AppointmentStatus.CONFIRMADA) {
            throw new DomainException("Solo las citas CONFIRMADAS pueden iniciarse. Estado actual: " + this.estado);
        }
        this.estado = AppointmentStatus.EN_CURSO;
    }

    public void completar() {
        if (this.estado != AppointmentStatus.EN_CURSO) {
            throw new DomainException("Solo las citas EN_CURSO pueden completarse. Estado actual: " + this.estado);
        }
        this.estado = AppointmentStatus.COMPLETADA;
    }

    public void cancelar() {
        if (this.estado == AppointmentStatus.COMPLETADA || this.estado == AppointmentStatus.CANCELADA) {
            throw new DomainException("No se puede cancelar una cita en estado " + this.estado);
        }
        this.estado = AppointmentStatus.CANCELADA;
    }

    public boolean esCancelacionTardia() {
        if (this.estado != AppointmentStatus.CANCELADA) {
            return false;
        }
        long horasAnticipacion = ChronoUnit.HOURS.between(LocalDateTime.now(), this.fechaHora);
        return horasAnticipacion < 24;
    }

    public void agregarTratamiento(TreatmentId tratamientoId) {
        if (tratamientoId == null) {
            throw new DomainException("El tratamiento no puede ser nulo");
        }
        this.tratamientos.add(tratamientoId);
    }

    public void removerTratamiento(TreatmentId tratamientoId) {
        if (tratamientoId == null) {
            throw new DomainException("El tratamiento no puede ser nulo");
        }
        this.tratamientos.remove(tratamientoId);
    }

    private void validate() {
        if (fechaHora == null) {
            throw new DomainException("La fecha y hora de la cita son obligatorias");
        }
        if (duracionMinutos == null || duracionMinutos < 15 || duracionMinutos > 180) {
            throw new DomainException("La duración de la cita debe estar entre 15 y 180 minutos");
        }
        if (estado == null) {
            throw new DomainException("El estado de la cita es obligatorio");
        }
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new DomainException("El motivo de la cita es obligatorio");
        }
        if (pacienteId == null) {
            throw new DomainException("El paciente es obligatorio");
        }
        if (odontologoId == null) {
            throw new DomainException("El odontólogo es obligatorio");
        }
        if (clinicaId == null) {
            throw new DomainException("La clínica es obligatoria");
        }
        if (consultorioId == null) {
            throw new DomainException("El consultorio es obligatorio");
        }
        if (fechaHora.isBefore(LocalDateTime.now().plusHours(24))) {
            throw new DomainException("La cita debe programarse con al menos 24 horas de anticipación");
        }
    }
}