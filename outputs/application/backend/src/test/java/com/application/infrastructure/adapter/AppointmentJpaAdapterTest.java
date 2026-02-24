package com.application.infrastructure.adapter;

import com.application.domain.model.Appointment;
import com.application.domain.valueobject.*;
import com.application.infrastructure.entity.AppointmentEntity;
import com.application.infrastructure.repository.AppointmentJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentJpaAdapterTest {

    @Mock
    private AppointmentJpaRepository appointmentJpaRepository;

    @Spy
    @InjectMocks
    private AppointmentJpaAdapter.AppointmentEntityMapper mapper;

    @InjectMocks
    private AppointmentJpaAdapter appointmentJpaAdapter;

    private AppointmentId appointmentId;
    private PatientId patientId;
    private DentistId dentistId;
    private ClinicId clinicId;
    private ConsultingRoomId consultingRoomId;
    private Appointment appointment;
    private AppointmentEntity appointmentEntity;

    @BeforeEach
    void setUp() {
        appointmentId = new AppointmentId(UUID.randomUUID());
        patientId = new PatientId(UUID.randomUUID());
        dentistId = new DentistId(UUID.randomUUID());
        clinicId = new ClinicId(UUID.randomUUID());
        consultingRoomId = new ConsultingRoomId(UUID.randomUUID());

        appointment = Appointment.builder()
                .id(appointmentId)
                .patientId(patientId)
                .dentistId(dentistId)
                .clinicId(clinicId)
                .consultingRoomId(consultingRoomId)
                .fechaHora(LocalDateTime.now().plusDays(1))
                .duracionMinutos(30)
                .estado(AppointmentStatus.PROGRAMADA)
                .motivo("Limpieza dental")
                .notas("Paciente con sensibilidad")
                .build();

        appointmentEntity = new AppointmentEntity();
        appointmentEntity.setId(appointmentId.getValue());
        appointmentEntity.setPatientId(patientId.getValue());
        appointmentEntity.setDentistId(dentistId.getValue());
        appointmentEntity.setClinicId(clinicId.getValue());
        appointmentEntity.setConsultingRoomId(consultingRoomId.getValue());
        appointmentEntity.setFechaHora(appointment.getFechaHora());
        appointmentEntity.setDuracionMinutos(appointment.getDuracionMinutos());
        appointmentEntity.setEstado(appointment.getEstado());
        appointmentEntity.setMotivo(appointment.getMotivo());
        appointmentEntity.setNotas(appointment.getNotas());
    }

    @Test
    void save_shouldSaveAndReturnAppointment() {
        when(appointmentJpaRepository.save(any(AppointmentEntity.class))).thenReturn(appointmentEntity);

        Appointment result = appointmentJpaAdapter.save(appointment);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(appointmentId);
        assertThat(result.getPatientId()).isEqualTo(patientId);
        assertThat(result.getDentistId()).isEqualTo(dentistId);
        assertThat(result.getClinicId()).isEqualTo(clinicId);
        assertThat(result.getConsultingRoomId()).isEqualTo(consultingRoomId);
        assertThat(result.getEstado()).isEqualTo(AppointmentStatus.PROGRAMADA);
        verify(appointmentJpaRepository).save(any(AppointmentEntity.class));
    }

    @Test
    void findById_shouldReturnAppointmentWhenExists() {
        when(appointmentJpaRepository.findById(appointmentId.getValue())).thenReturn(Optional.of(appointmentEntity));

        Optional<Appointment> result = appointmentJpaAdapter.findById(appointmentId);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(appointmentId);
        verify(appointmentJpaRepository).findById(appointmentId.getValue());
    }

    @Test
    void findById_shouldReturnEmptyWhenNotExists() {
        when(appointmentJpaRepository.findById(appointmentId.getValue())).thenReturn(Optional.empty());

        Optional<Appointment> result = appointmentJpaAdapter.findById(appointmentId);

        assertThat(result).isEmpty();
        verify(appointmentJpaRepository).findById(appointmentId.getValue());
    }

    @Test
    void findAll_shouldReturnAllAppointments() {
        List<AppointmentEntity> entities = List.of(appointmentEntity);
        when(appointmentJpaRepository.findAll()).thenReturn(entities);

        List<Appointment> result = appointmentJpaAdapter.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(appointmentId);
        verify(appointmentJpaRepository).findAll();
    }

    @Test
    void deleteById_shouldCallRepositoryDelete() {
        doNothing().when(appointmentJpaRepository).deleteById(appointmentId.getValue());

        appointmentJpaAdapter.deleteById(appointmentId);

        verify(appointmentJpaRepository).deleteById(appointmentId.getValue());
    }

    @Test
    void existsById_shouldReturnTrueWhenExists() {
        when(appointmentJpaRepository.existsById(appointmentId.getValue())).thenReturn(true);

        boolean result = appointmentJpaAdapter.existsById(appointmentId);

        assertThat(result).isTrue();
        verify(appointmentJpaRepository).existsById(appointmentId.getValue());
    }

    @Test
    void existsById_shouldReturnFalseWhenNotExists() {
        when(appointmentJpaRepository.existsById(appointmentId.getValue())).thenReturn(false);

        boolean result = appointmentJpaAdapter.existsById(appointmentId);

        assertThat(result).isFalse();
        verify(appointmentJpaRepository).existsById(appointmentId.getValue());
    }

    @Test
    void findByPatientId_shouldReturnAppointmentsForPatient() {
        List<AppointmentEntity> entities = List.of(appointmentEntity);
        when(appointmentJpaRepository.findByPatientId(patientId.getValue())).thenReturn(entities);

        List<Appointment> result = appointmentJpaAdapter.findByPatientId(new AppointmentId(patientId.getValue()));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPatientId()).isEqualTo(patientId);
        verify(appointmentJpaRepository).findByPatientId(patientId.getValue());
    }

    @Test
    void findByDentistId_shouldReturnAppointmentsForDentist() {
        List<AppointmentEntity> entities = List.of(appointmentEntity);
        when(appointmentJpaRepository.findByDentistId(dentistId.getValue())).thenReturn(entities);

        List<Appointment> result = appointmentJpaAdapter.findByDentistId(new AppointmentId(dentistId.getValue()));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDentistId()).isEqualTo(dentistId);
        verify(appointmentJpaRepository).findByDentistId(dentistId.getValue());
    }

    @Test
    void findByClinicId_shouldReturnAppointmentsForClinic() {
        List<AppointmentEntity> entities = List.of(appointmentEntity);
        when(appointmentJpaRepository.findByClinicId(clinicId.getValue())).thenReturn(entities);

        List<Appointment> result = appointmentJpaAdapter.findByClinicId(new AppointmentId(clinicId.getValue()));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getClinicId()).isEqualTo(clinicId);
        verify(appointmentJpaRepository).findByClinicId(clinicId.getValue());
    }

    @Test
    void mapper_toEntity_shouldMapAllFields() {
        AppointmentJpaAdapter.AppointmentEntityMapper mapper = new AppointmentJpaAdapter.AppointmentEntityMapper();
        AppointmentEntity entity = mapper.toEntity(appointment);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(appointmentId.getValue());
        assertThat(entity.getPatientId()).isEqualTo(patientId.getValue());
        assertThat(entity.getDentistId()).isEqualTo(dentistId.getValue());
        assertThat(entity.getClinicId()).isEqualTo(clinicId.getValue());
        assertThat(entity.getConsultingRoomId()).isEqualTo(consultingRoomId.getValue());
        assertThat(entity.getFechaHora()).isEqualTo(appointment.getFechaHora());
        assertThat(entity.getDuracionMinutos()).isEqualTo(appointment.getDuracionMinutos());
        assertThat(entity.getEstado()).isEqualTo(appointment.getEstado());
        assertThat(entity.getMotivo()).isEqualTo(appointment.getMotivo());
        assertThat(entity.getNotas()).isEqualTo(appointment.getNotas());
    }

    @Test
    void mapper_toEntity_shouldReturnNullForNullInput() {
        AppointmentJpaAdapter.AppointmentEntityMapper mapper = new AppointmentJpaAdapter.AppointmentEntityMapper();
        AppointmentEntity entity = mapper.toEntity(null);

        assertThat(entity).isNull();
    }

    @Test
    void mapper_toEntity_shouldHandleNullIds() {
        Appointment appointmentWithoutIds = Appointment.builder()
                .id(appointmentId)
                .patientId(null)
                .dentistId(null)
                .clinicId(null)
                .consultingRoomId(null)
                .fechaHora(LocalDateTime.now())
                .duracionMinutos(30)
                .estado(AppointmentStatus.PROGRAMADA)
                .motivo("Test")
                .notas("Test notes")
                .build();

        AppointmentJpaAdapter.AppointmentEntityMapper mapper = new AppointmentJpaAdapter.AppointmentEntityMapper();
        AppointmentEntity entity = mapper.toEntity(appointmentWithoutIds);

        assertThat(entity).isNotNull();
        assertThat(entity.getPatientId()).isNull();
        assertThat(entity.getDentistId()).isNull();
        assertThat(entity.getClinicId()).isNull();
        assertThat(entity.getConsultingRoomId()).isNull();
    }

    @Test
    void mapper_toDomain_shouldMapAllFields() {
        AppointmentJpaAdapter.AppointmentEntityMapper mapper = new AppointmentJpaAdapter.AppointmentEntityMapper();
        Appointment domain = mapper.toDomain(appointmentEntity);

        assertThat(domain).isNotNull();
        assertThat(domain.getId().getValue()).isEqualTo(appointmentId.getValue());
        assertThat(domain.getPatientId().getValue()).isEqualTo(patientId.getValue());
        assertThat(domain.getDentistId().getValue()).isEqualTo(dentistId.getValue());
        assertThat(domain.getClinicId().getValue()).isEqualTo(clinicId.getValue());
        assertThat(domain.getConsultingRoomId().getValue()).isEqualTo(consultingRoomId.getValue());
        assertThat(domain.getFechaHora()).isEqualTo(appointmentEntity.getFechaHora());
        assertThat(domain.getDuracionMinutos()).isEqualTo(appointmentEntity.getDuracionMinutos());
        assertThat(domain.getEstado()).isEqualTo(appointmentEntity.getEstado());
        assertThat(domain.getMotivo()).isEqualTo(appointmentEntity.getMotivo());
        assertThat(domain.getNotas()).isEqualTo(appointmentEntity.getNotas());
    }

    @Test
    void mapper_toDomain_shouldReturnNullForNullInput() {
        AppointmentJpaAdapter.AppointmentEntityMapper mapper = new AppointmentJpaAdapter.AppointmentEntityMapper();
        Appointment domain = mapper.toDomain(null);

        assertThat(domain).isNull();
    }

    @Test
    void mapper_toDomain_shouldHandleNullIds() {
        AppointmentEntity entityWithoutIds = new AppointmentEntity();
        entityWithoutIds.setId(appointmentId.getValue());
        entityWithoutIds.setPatientId(null);
        entityWithoutIds.setDentistId(null);
        entityWithoutIds.setClinicId(null);
        entityWithoutIds.setConsultingRoomId(null);
        entityWithoutIds.setFechaHora(LocalDateTime.now());
        entityWithoutIds.setDuracionMinutos(30);
        entityWithoutIds.setEstado(AppointmentStatus.PROGRAMADA);
        entityWithoutIds.setMotivo("Test");
        entityWithoutIds.setNotas("Test notes");

        AppointmentJpaAdapter.AppointmentEntityMapper mapper = new AppointmentJpaAdapter.AppointmentEntityMapper();
        Appointment domain = mapper.toDomain(entityWithoutIds);

        assertThat(domain).isNotNull();
        assertThat(domain.getPatientId()).isNull();
        assertThat(domain.getDentistId()).isNull();
        assertThat(domain.getClinicId()).isNull();
        assertThat(domain.getConsultingRoomId()).isNull();
    }
}