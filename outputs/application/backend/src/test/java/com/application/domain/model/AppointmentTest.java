package com.application.domain.model;

import com.application.domain.exception.DomainException;
import com.application.domain.valueobject.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AppointmentTest {

    private final AppointmentId appointmentId = new AppointmentId(UUID.randomUUID());
    private final LocalDateTime futureDateTime = LocalDateTime.now().plusDays(2);
    private final PatientId patientId = new PatientId(UUID.randomUUID());
    private final DentistId dentistId = new DentistId(UUID.randomUUID());
    private final ClinicId clinicId = new ClinicId(UUID.randomUUID());
    private final ConsultingRoomId consultingRoomId = new ConsultingRoomId(UUID.randomUUID());
    private final TreatmentId treatmentId = new TreatmentId(UUID.randomUUID());
    private final Set<TreatmentId> treatments = new HashSet<>();

    @Test
    void create_ShouldCreateAppointmentWithProgrammedStatus() {
        Appointment appointment = Appointment.create(
                futureDateTime,
                60,
                "Limpieza dental",
                "Notas de prueba",
                patientId,
                dentistId,
                clinicId,
                consultingRoomId,
                treatments
        );

        assertNotNull(appointment.getId());
        assertEquals(futureDateTime, appointment.getFechaHora());
        assertEquals(60, appointment.getDuracionMinutos());
        assertEquals(AppointmentStatus.PROGRAMADA, appointment.getEstado());
        assertEquals("Limpieza dental", appointment.getMotivo());
        assertEquals("Notas de prueba", appointment.getNotas());
        assertEquals(patientId, appointment.getPacienteId());
        assertEquals(dentistId, appointment.getOdontologoId());
        assertEquals(clinicId, appointment.getClinicaId());
        assertEquals(consultingRoomId, appointment.getConsultorioId());
        assertEquals(treatments, appointment.getTratamientos());
    }

    @Test
    void create_ShouldThrowExceptionWhenDateIsLessThan24Hours() {
        LocalDateTime invalidDateTime = LocalDateTime.now().plusHours(23);

        DomainException exception = assertThrows(DomainException.class, () ->
                Appointment.create(
                        invalidDateTime,
                        60,
                        "Motivo",
                        "Notas",
                        patientId,
                        dentistId,
                        clinicId,
                        consultingRoomId,
                        treatments
                )
        );

        assertTrue(exception.getMessage().contains("La cita debe programarse con al menos 24 horas de anticipación"));
    }

    @Test
    void constructor_ShouldThrowExceptionWhenDurationIsLessThan15() {
        DomainException exception = assertThrows(DomainException.class, () ->
                new Appointment(
                        appointmentId,
                        futureDateTime,
                        14,
                        AppointmentStatus.PROGRAMADA,
                        "Motivo",
                        "Notas",
                        patientId,
                        dentistId,
                        clinicId,
                        consultingRoomId,
                        treatments
                )
        );

        assertTrue(exception.getMessage().contains("La duración de la cita debe estar entre 15 y 180 minutos"));
    }

    @Test
    void constructor_ShouldThrowExceptionWhenDurationIsMoreThan180() {
        DomainException exception = assertThrows(DomainException.class, () ->
                new Appointment(
                        appointmentId,
                        futureDateTime,
                        181,
                        AppointmentStatus.PROGRAMADA,
                        "Motivo",
                        "Notas",
                        patientId,
                        dentistId,
                        clinicId,
                        consultingRoomId,
                        treatments
                )
        );

        assertTrue(exception.getMessage().contains("La duración de la cita debe estar entre 15 y 180 minutos"));
    }

    @Test
    void constructor_ShouldThrowExceptionWhenMotivoIsEmpty() {
        DomainException exception = assertThrows(DomainException.class, () ->
                new Appointment(
                        appointmentId,
                        futureDateTime,
                        60,
                        AppointmentStatus.PROGRAMADA,
                        "",
                        "Notas",
                        patientId,
                        dentistId,
                        clinicId,
                        consultingRoomId,
                        treatments
                )
        );

        assertTrue(exception.getMessage().contains("El motivo de la cita es obligatorio"));
    }

    @Test
    void constructor_ShouldThrowExceptionWhenRequiredIdsAreNull() {
        assertThrows(DomainException.class, () ->
                new Appointment(
                        appointmentId,
                        futureDateTime,
                        60,
                        AppointmentStatus.PROGRAMADA,
                        "Motivo",
                        "Notas",
                        null,
                        dentistId,
                        clinicId,
                        consultingRoomId,
                        treatments
                )
        );

        assertThrows(DomainException.class, () ->
                new Appointment(
                        appointmentId,
                        futureDateTime,
                        60,
                        AppointmentStatus.PROGRAMADA,
                        "Motivo",
                        "Notas",
                        patientId,
                        null,
                        clinicId,
                        consultingRoomId,
                        treatments
                )
        );

        assertThrows(DomainException.class, () ->
                new Appointment(
                        appointmentId,
                        futureDateTime,
                        60,
                        AppointmentStatus.PROGRAMADA,
                        "Motivo",
                        "Notas",
                        patientId,
                        dentistId,
                        null,
                        consultingRoomId,
                        treatments
                )
        );

        assertThrows(DomainException.class, () ->
                new Appointment(
                        appointmentId,
                        futureDateTime,
                        60,
                        AppointmentStatus.PROGRAMADA,
                        "Motivo",
                        "Notas",
                        patientId,
                        dentistId,
                        clinicId,
                        null,
                        treatments
                )
        );
    }

    @Test
    void confirmar_ShouldChangeStatusToConfirmed() {
        Appointment appointment = new Appointment(
                appointmentId,
                futureDateTime,
                60,
                AppointmentStatus.PROGRAMADA,
                "Motivo",
                "Notas",
                patientId,
                dentistId,
                clinicId,
                consultingRoomId,
                treatments
        );

        appointment.confirmar();

        assertEquals(AppointmentStatus.CONFIRMADA, appointment.getEstado());
    }

    @Test
    void confirmar_ShouldThrowExceptionWhenNotProgrammed() {
        Appointment appointment = new Appointment(
                appointmentId,
                futureDateTime,
                60,
                AppointmentStatus.CONFIRMADA,
                "Motivo",
                "Notas",
                patientId,
                dentistId,
                clinicId,
                consultingRoomId,
                treatments
        );

        DomainException exception = assertThrows(DomainException.class, appointment::confirmar);
        assertTrue(exception.getMessage().contains("Solo las citas PROGRAMADAS pueden ser confirmadas"));
    }

    @Test
    void iniciar_ShouldChangeStatusToInProgress() {
        Appointment appointment = new Appointment(
                appointmentId,
                futureDateTime,
                60,
                AppointmentStatus.CONFIRMADA,
                "Motivo",
                "Notas",
                patientId,
                dentistId,
                clinicId,
                consultingRoomId,
                treatments
        );

        appointment.iniciar();

        assertEquals(AppointmentStatus.EN_CURSO, appointment.getEstado());
    }

    @Test
    void iniciar_ShouldThrowExceptionWhenNotConfirmed() {
        Appointment appointment = new Appointment(
                appointmentId,
                futureDateTime,
                60,
                AppointmentStatus.PROGRAMADA,
                "Motivo",
                "Notas",
                patientId,
                dentistId,
                clinicId,
                consultingRoomId,
                treatments
        );

        DomainException exception = assertThrows(DomainException.class, appointment::iniciar);
        assertTrue(exception.getMessage().contains("Solo las citas CONFIRMADAS pueden iniciarse"));
    }

    @Test
    void completar_ShouldChangeStatusToCompleted() {
        Appointment appointment = new Appointment(
                appointmentId,
                futureDateTime,
                60,
                AppointmentStatus.EN_CURSO,
                "Motivo",
                "Notas",
                patientId,
                dentistId,
                clinicId,
                consultingRoomId,
                treatments
        );

        appointment.completar();

        assertEquals(AppointmentStatus.COMPLETADA, appointment.getEstado());
    }

    @Test
    void completar_ShouldThrowExceptionWhenNotInProgress() {
        Appointment appointment = new Appointment(
                appointmentId,
                futureDateTime,
                60,
                AppointmentStatus.CONFIRMADA,
                "Motivo",
                "Notas",
                patientId,
                dentistId,
                clinicId,
                consultingRoomId,
                treatments
        );

        DomainException exception = assertThrows(DomainException.class, appointment::completar);
        assertTrue(exception.getMessage().contains("Solo las citas EN_CURSO pueden completarse"));
    }

    @Test
    void cancelar_ShouldChangeStatusToCancelled() {
        Appointment appointment = new Appointment(
                appointmentId,
                futureDateTime,
                60,
                AppointmentStatus.PROGRAMADA,
                "Motivo",
                "Notas",
                patientId,
                dentistId,
                clinicId,
                consultingRoomId,
                treatments
        );

        appointment.cancelar();

        assertEquals(AppointmentStatus.CANCELADA, appointment.getEstado());
    }

    @Test
    void cancelar_ShouldThrowExceptionWhenAlreadyCompleted() {
        Appointment appointment = new Appointment(
                appointmentId,
                futureDateTime,
                60,
                AppointmentStatus.COMPLETADA,
                "Motivo",
                "Notas",
                patientId,
                dentistId,
                clinicId,
                consultingRoomId,
                treatments
        );

        DomainException exception = assertThrows(DomainException.class, appointment::cancelar);
        assertTrue(exception.getMessage().contains("No se puede cancelar una cita en estado COMPLETADA"));
    }

    @Test
    void cancelar_ShouldThrowExceptionWhenAlreadyCancelled() {
        Appointment appointment = new Appointment(
                appointmentId,
                futureDateTime,
                60,
                AppointmentStatus.CANCELADA,
                "Motivo",
                "Notas",
                patientId,
                dentistId,
                clinicId,
                consultingRoomId,
                treatments
        );

        DomainException exception = assertThrows(DomainException.class, appointment::cancelar);
        assertTrue(exception.getMessage().contains("No se puede cancelar una cita en estado CANCELADA"));
    }

    @Test
    void esCancelacionTardia_ShouldReturnFalseWhenNotCancelled() {
        Appointment appointment = new Appointment(
                appointmentId,
                futureDateTime,
                60,
                AppointmentStatus.PROGRAMADA,
                "Motivo",
                "Notas",
                patientId,
                dentistId,
                clinicId,
                consultingRoomId,
                treatments
        );

        assertFalse(appointment.esCancelacionTardia());
    }

    @Test
    void esCancelacionTardia_ShouldReturnTrueWhenCancelledLessThan24Hours() {
        LocalDateTime nearFuture = LocalDateTime.now().plusHours(12);
        Appointment appointment = new Appointment(
                appointmentId,
                nearFuture,
                60,
                AppointmentStatus.CANCELADA,
                "Motivo",
                "Notas",
                patientId,
                dentistId,
                clinicId,
                consultingRoomId,
                treatments
        );

        assertTrue(appointment.esCancelacionTardia());
    }

    @Test
    void esCancelacionTardia_ShouldReturnFalseWhenCancelledMoreThan24Hours() {
        LocalDateTime farFuture = LocalDateTime.now().plusDays(2);
        Appointment appointment = new Appointment(
                appointmentId,
                farFuture,
                60,
                AppointmentStatus.CANCELADA,
                "Motivo",
                "Notas",
                patientId,
                dentistId,
                clinicId,
                consultingRoomId,
                treatments
        );

        assertFalse(appointment.esCancelacionTardia());
    }

    @Test
    void agregarTratamiento_ShouldAddTreatment() {
        Appointment appointment = new Appointment(
                appointmentId,
                futureDateTime,
                60,
                AppointmentStatus.PROGRAMADA,
                "Motivo",
                "Notas",
                patientId,
                dentistId,
                clinicId,
                consultingRoomId,
                new HashSet<>()
        );

        appointment.agregarTratamiento(treatmentId);

        assertTrue(appointment.getTratamientos().contains(treatmentId));
        assertEquals(1, appointment.getTratamientos().size());
    }

    @Test
    void agregarTratamiento_ShouldThrowExceptionWhenTreatmentIsNull() {
        Appointment appointment = new Appointment(
                appointmentId,
                futureDateTime,
                60,
                AppointmentStatus.PROGRAMADA,
                "Motivo",
                "Notas",
                patientId,
                dentistId,
                clinicId,
                consultingRoomId,
                treatments
        );

        DomainException exception = assertThrows(DomainException.class, () ->
                appointment.agregarTratamiento(null)
        );

        assertTrue(exception.getMessage().contains("El tratamiento no puede ser nulo"));
    }

    @Test
    void removerTratamiento_ShouldRemoveTreatment() {
        Set<TreatmentId> initialTreatments = new HashSet<>();
        initialTreatments.add(treatmentId);

        Appointment appointment = new Appointment(
                appointmentId,
                futureDateTime,
                60,
                AppointmentStatus.PROGRAMADA,
                "Motivo",
                "Notas",
                patientId,
                dentistId,
                clinicId,
                consultingRoomId,
                initialTreatments
        );

        appointment.removerTratamiento(treatmentId);

        assertFalse(appointment.getTratamientos().contains(treatmentId));
        assertTrue(appointment.getTratamientos().isEmpty());
    }

    @Test
    void removerTratamiento_ShouldThrowExceptionWhenTreatmentIsNull() {
        Appointment appointment = new Appointment(
                appointmentId,
                futureDateTime,
                60,
                AppointmentStatus.PROGRAMADA,
                "Motivo",
                "Notas",
                patientId,
                dentistId,
                clinicId,
                consultingRoomId,
                treatments
        );

        DomainException exception = assertThrows(DomainException.class, () ->
                appointment.removerTratamiento(null)
        );

        assertTrue(exception.getMessage().contains("El tratamiento no puede ser nulo"));
    }

    @Test
    void constructor_ShouldCopyTreatmentSet() {
        Set<TreatmentId> originalSet = new HashSet<>();
        originalSet.add(treatmentId);

        Appointment appointment = new Appointment(
                appointmentId,
                futureDateTime,
                60,
                AppointmentStatus.PROGRAMADA,
                "Motivo",
                "Notas",
                patientId,
                dentistId,
                clinicId,
                consultingRoomId,
                originalSet
        );

        originalSet.clear();

        assertFalse(originalSet.contains(treatmentId));
        assertTrue(appointment.getTratamientos().contains(treatmentId));
        assertEquals(1, appointment.getTratamientos().size());
    }

    @Test
    void constructor_ShouldInitializeEmptySetWhenTreatmentsIsNull() {
        Appointment appointment = new Appointment(
                appointmentId,
                futureDateTime,
                60,
                AppointmentStatus.PROGRAMADA,
                "Motivo",
                "Notas",
                patientId,
                dentistId,
                clinicId,
                consultingRoomId,
                null
        );

        assertNotNull(appointment.getTratamientos());
        assertTrue(appointment.getTratamientos().isEmpty());
    }
}