package com.application.application.dto;

import com.application.domain.valueobject.AppointmentId;
import com.application.domain.valueobject.AppointmentStatus;
import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.DentistId;
import com.application.domain.valueobject.ClinicId;
import com.application.domain.valueobject.ConsultingRoomId;
import com.application.domain.valueobject.TreatmentId;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record AppointmentDTO(
        UUID appointmentId,
        LocalDateTime fechaHora,
        Integer duracionMinutos,
        AppointmentStatus estado,
        String motivo,
        String notas,
        UUID patientId,
        UUID dentistId,
        UUID clinicId,
        UUID consultingRoomId,
        Set<UUID> treatmentIds
) {
    public static AppointmentDTO fromDomain(com.application.domain.model.Appointment appointment) {
        return new AppointmentDTO(
                appointment.getId().value(),
                appointment.getFechaHora(),
                appointment.getDuracionMinutos(),
                appointment.getEstado(),
                appointment.getMotivo(),
                appointment.getNotas(),
                appointment.getPatientId().value(),
                appointment.getDentistId().value(),
                appointment.getClinicId().value(),
                appointment.getConsultingRoomId().value(),
                appointment.getTreatmentIds().stream()
                        .map(TreatmentId::value)
                        .collect(Collectors.toSet())
        );
    }

    public com.application.domain.model.Appointment toDomain() {
        return new com.application.domain.model.Appointment(
                new AppointmentId(appointmentId),
                fechaHora,
                duracionMinutos,
                estado,
                motivo,
                notas,
                new PatientId(patientId),
                new DentistId(dentistId),
                new ClinicId(clinicId),
                new ConsultingRoomId(consultingRoomId),
                treatmentIds.stream()
                        .map(TreatmentId::new)
                        .collect(Collectors.toSet())
        );
    }
}