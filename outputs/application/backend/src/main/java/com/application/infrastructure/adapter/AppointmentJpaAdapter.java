package com.application.infrastructure.adapter;

import com.application.domain.model.Appointment;
import com.application.domain.port.AppointmentRepositoryPort;
import com.application.domain.valueobject.AppointmentId;
import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.DentistId;
import com.application.domain.valueobject.ClinicId;
import com.application.domain.valueobject.ConsultingRoomId;
import com.application.infrastructure.entity.AppointmentEntity;
import com.application.infrastructure.repository.AppointmentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AppointmentJpaAdapter implements AppointmentRepositoryPort {

    private final AppointmentJpaRepository appointmentJpaRepository;
    private final AppointmentEntityMapper mapper;

    @Override
    public Appointment save(Appointment appointment) {
        AppointmentEntity entity = mapper.toEntity(appointment);
        AppointmentEntity savedEntity = appointmentJpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Appointment> findById(AppointmentId appointmentId) {
        return appointmentJpaRepository.findById(appointmentId.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public List<Appointment> findAll() {
        return appointmentJpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(AppointmentId appointmentId) {
        appointmentJpaRepository.deleteById(appointmentId.getValue());
    }

    @Override
    public boolean existsById(AppointmentId appointmentId) {
        return appointmentJpaRepository.existsById(appointmentId.getValue());
    }

    @Override
    public List<Appointment> findByPatientId(PatientId patientId) {
        return appointmentJpaRepository.findByPatientId(patientId.getValue())
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Appointment> findByDentistId(DentistId dentistId) {
        return appointmentJpaRepository.findByDentistId(dentistId.getValue())
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Appointment> findByClinicId(ClinicId clinicId) {
        return appointmentJpaRepository.findByClinicId(clinicId.getValue())
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Component
    static class AppointmentEntityMapper {

        public AppointmentEntity toEntity(Appointment appointment) {
            if (appointment == null) {
                return null;
            }

            AppointmentEntity entity = new AppointmentEntity();
            entity.setId(appointment.getId().getValue());
            entity.setFechaHora(appointment.getFechaHora());
            entity.setDuracionMinutos(appointment.getDuracionMinutos());
            entity.setEstado(appointment.getEstado());
            entity.setMotivo(appointment.getMotivo());
            entity.setNotas(appointment.getNotas());

            if (appointment.getPatientId() != null) {
                entity.setPatientId(appointment.getPatientId().getValue());
            }
            if (appointment.getDentistId() != null) {
                entity.setDentistId(appointment.getDentistId().getValue());
            }
            if (appointment.getClinicId() != null) {
                entity.setClinicId(appointment.getClinicId().getValue());
            }
            if (appointment.getConsultingRoomId() != null) {
                entity.setConsultingRoomId(appointment.getConsultingRoomId().getValue());
            }

            return entity;
        }

        public Appointment toDomain(AppointmentEntity entity) {
            if (entity == null) {
                return null;
            }

            return Appointment.builder()
                    .id(new AppointmentId(entity.getId()))
                    .patientId(entity.getPatientId() != null ? new PatientId(entity.getPatientId()) : null)
                    .dentistId(entity.getDentistId() != null ? new DentistId(entity.getDentistId()) : null)
                    .clinicId(entity.getClinicId() != null ? new ClinicId(entity.getClinicId()) : null)
                    .consultingRoomId(entity.getConsultingRoomId() != null ? new ConsultingRoomId(entity.getConsultingRoomId()) : null)
                    .fechaHora(entity.getFechaHora())
                    .duracionMinutos(entity.getDuracionMinutos())
                    .estado(entity.getEstado())
                    .motivo(entity.getMotivo())
                    .notas(entity.getNotas())
                    .build();
        }
    }
}