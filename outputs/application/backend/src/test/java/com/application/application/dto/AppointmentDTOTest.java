package com.application.application.dto;

import com.application.domain.model.Appointment;
import com.application.domain.valueobject.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AppointmentDTOTest {

    @Test
    void givenValidAppointmentDomain_whenFromDomain_thenReturnCorrectDTO() {
        // Given
        UUID appointmentUUID = UUID.randomUUID();
        UUID patientUUID = UUID.randomUUID();
        UUID dentistUUID = UUID.randomUUID();
        UUID clinicUUID = UUID.randomUUID();
        UUID roomUUID = UUID.randomUUID();
        UUID treatment1UUID = UUID.randomUUID();
        UUID treatment2UUID = UUID.randomUUID();

        AppointmentId appointmentId = new AppointmentId(appointmentUUID);
        PatientId patientId = new PatientId(patientUUID);
        DentistId dentistId = new DentistId(dentistUUID);
        ClinicId clinicId = new ClinicId(clinicUUID);
        ConsultingRoomId consultingRoomId = new ConsultingRoomId(roomUUID);
        Set<TreatmentId> treatmentIds = Set.of(
                new TreatmentId(treatment1UUID),
                new TreatmentId(treatment2UUID)
        );

        LocalDateTime fechaHora = LocalDateTime.now().plusDays(1);
        Integer duracionMinutos = 45;
        AppointmentStatus estado = AppointmentStatus.PROGRAMADA;
        String motivo = "Limpieza dental";
        String notas = "Paciente con sensibilidad";

        Appointment appointment = new Appointment(
                appointmentId,
                fechaHora,
                duracionMinutos,
                estado,
                motivo,
                notas,
                patientId,
                dentistId,
                clinicId,
                consultingRoomId,
                treatmentIds
        );

        // When
        AppointmentDTO dto = AppointmentDTO.fromDomain(appointment);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.appointmentId()).isEqualTo(appointmentUUID);
        assertThat(dto.fechaHora()).isEqualTo(fechaHora);
        assertThat(dto.duracionMinutos()).isEqualTo(duracionMinutos);
        assertThat(dto.estado()).isEqualTo(estado);
        assertThat(dto.motivo()).isEqualTo(motivo);
        assertThat(dto.notas()).isEqualTo(notas);
        assertThat(dto.patientId()).isEqualTo(patientUUID);
        assertThat(dto.dentistId()).isEqualTo(dentistUUID);
        assertThat(dto.clinicId()).isEqualTo(clinicUUID);
        assertThat(dto.consultingRoomId()).isEqualTo(roomUUID);
        assertThat(dto.treatmentIds()).containsExactlyInAnyOrder(treatment1UUID, treatment2UUID);
    }

    @Test
    void givenValidDTO_whenToDomain_thenReturnCorrectAppointment() {
        // Given
        UUID appointmentUUID = UUID.randomUUID();
        UUID patientUUID = UUID.randomUUID();
        UUID dentistUUID = UUID.randomUUID();
        UUID clinicUUID = UUID.randomUUID();
        UUID roomUUID = UUID.randomUUID();
        UUID treatment1UUID = UUID.randomUUID();
        UUID treatment2UUID = UUID.randomUUID();

        LocalDateTime fechaHora = LocalDateTime.now().plusDays(2);
        Integer duracionMinutos = 60;
        AppointmentStatus estado = AppointmentStatus.CONFIRMADA;
        String motivo = "Extracción molar";
        String notas = "Requiere anestesia local";
        Set<UUID> treatmentIds = Set.of(treatment1UUID, treatment2UUID);

        AppointmentDTO dto = new AppointmentDTO(
                appointmentUUID,
                fechaHora,
                duracionMinutos,
                estado,
                motivo,
                notas,
                patientUUID,
                dentistUUID,
                clinicUUID,
                roomUUID,
                treatmentIds
        );

        // When
        Appointment domain = dto.toDomain();

        // Then
        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isInstanceOf(AppointmentId.class);
        assertThat(domain.getId().value()).isEqualTo(appointmentUUID);
        assertThat(domain.getFechaHora()).isEqualTo(fechaHora);
        assertThat(domain.getDuracionMinutos()).isEqualTo(duracionMinutos);
        assertThat(domain.getEstado()).isEqualTo(estado);
        assertThat(domain.getMotivo()).isEqualTo(motivo);
        assertThat(domain.getNotas()).isEqualTo(notas);
        assertThat(domain.getPatientId()).isInstanceOf(PatientId.class);
        assertThat(domain.getPatientId().value()).isEqualTo(patientUUID);
        assertThat(domain.getDentistId()).isInstanceOf(DentistId.class);
        assertThat(domain.getDentistId().value()).isEqualTo(dentistUUID);
        assertThat(domain.getClinicId()).isInstanceOf(ClinicId.class);
        assertThat(domain.getClinicId().value()).isEqualTo(clinicUUID);
        assertThat(domain.getConsultingRoomId()).isInstanceOf(ConsultingRoomId.class);
        assertThat(domain.getConsultingRoomId().value()).isEqualTo(roomUUID);
        assertThat(domain.getTreatmentIds()).hasSize(2);
        assertThat(domain.getTreatmentIds().stream()
                .map(TreatmentId::value)
                .collect(Collectors.toSet()))
                .containsExactlyInAnyOrder(treatment1UUID, treatment2UUID);
    }

    @Test
    void givenDTOWithEmptyTreatmentIds_whenToDomain_thenReturnAppointmentWithEmptySet() {
        // Given
        AppointmentDTO dto = new AppointmentDTO(
                UUID.randomUUID(),
                LocalDateTime.now().plusDays(1),
                30,
                AppointmentStatus.PROGRAMADA,
                "Consulta",
                "",
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                Set.of()
        );

        // When
        Appointment domain = dto.toDomain();

        // Then
        assertThat(domain).isNotNull();
        assertThat(domain.getTreatmentIds()).isEmpty();
    }

    @Test
    void givenTwoEqualDTOs_whenEquals_thenTheyAreEqual() {
        UUID fixedUUID = UUID.randomUUID();
        LocalDateTime fixedTime = LocalDateTime.now();

        AppointmentDTO dto1 = new AppointmentDTO(
                fixedUUID,
                fixedTime,
                30,
                AppointmentStatus.PROGRAMADA,
                "Test",
                "",
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                Set.of()
        );

        AppointmentDTO dto2 = new AppointmentDTO(
                fixedUUID,
                fixedTime,
                30,
                AppointmentStatus.PROGRAMADA,
                "Test",
                "",
                dto1.patientId(),
                dto1.dentistId(),
                dto1.clinicId(),
                dto1.consultingRoomId(),
                Set.of()
        );

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    void givenTwoDifferentDTOs_whenEquals_thenTheyAreNotEqual() {
        AppointmentDTO dto1 = new AppointmentDTO(
                UUID.randomUUID(),
                LocalDateTime.now(),
                30,
                AppointmentStatus.PROGRAMADA,
                "Test1",
                "",
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                Set.of()
        );

        AppointmentDTO dto2 = new AppointmentDTO(
                UUID.randomUUID(),
                LocalDateTime.now().plusHours(1),
                45,
                AppointmentStatus.CONFIRMADA,
                "Test2",
                "Notas",
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                Set.of(UUID.randomUUID())
        );

        assertThat(dto1).isNotEqualTo(dto2);
        assertThat(dto1.hashCode()).isNotEqualTo(dto2.hashCode());
    }

    @Test
    void givenDomainAndDTOConversion_whenRoundTrip_thenDataIsPreserved() {
        // Given
        UUID appointmentUUID = UUID.randomUUID();
        AppointmentId appointmentId = new AppointmentId(appointmentUUID);
        LocalDateTime fechaHora = LocalDateTime.now().plusDays(3);
        Set<TreatmentId> treatmentIds = Set.of(
                new TreatmentId(UUID.randomUUID()),
                new TreatmentId(UUID.randomUUID())
        );

        Appointment original = new Appointment(
                appointmentId,
                fechaHora,
                90,
                AppointmentStatus.EN_CURSO,
                "Ortodoncia",
                "Ajuste de brackets",
                new PatientId(UUID.randomUUID()),
                new DentistId(UUID.randomUUID()),
                new ClinicId(UUID.randomUUID()),
                new ConsultingRoomId(UUID.randomUUID()),
                treatmentIds
        );

        // When
        AppointmentDTO dto = AppointmentDTO.fromDomain(original);
        Appointment converted = dto.toDomain();

        // Then
        assertThat(converted.getId().value()).isEqualTo(original.getId().value());
        assertThat(converted.getFechaHora()).isEqualTo(original.getFechaHora());
        assertThat(converted.getDuracionMinutos()).isEqualTo(original.getDuracionMinutos());
        assertThat(converted.getEstado()).isEqualTo(original.getEstado());
        assertThat(converted.getMotivo()).isEqualTo(original.getMotivo());
        assertThat(converted.getNotas()).isEqualTo(original.getNotas());
        assertThat(converted.getPatientId().value()).isEqualTo(original.getPatientId().value());
        assertThat(converted.getDentistId().value()).isEqualTo(original.getDentistId().value());
        assertThat(converted.getClinicId().value()).isEqualTo(original.getClinicId().value());
        assertThat(converted.getConsultingRoomId().value()).isEqualTo(original.getConsultingRoomId().value());
        assertThat(converted.getTreatmentIds()).containsExactlyInAnyOrderElementsOf(original.getTreatmentIds());
    }
}