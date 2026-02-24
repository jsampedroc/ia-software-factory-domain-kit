package com.application.domain.port;

import com.application.domain.model.Clinic;
import com.application.domain.valueobject.ClinicId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClinicRepositoryPortTest {

    @Mock
    private ClinicRepositoryPort clinicRepositoryPort;

    @Test
    void save_ShouldPersistClinic() {
        ClinicId clinicId = new ClinicId(UUID.randomUUID());
        Clinic clinic = Clinic.create(
                clinicId,
                "CLI-001",
                "Clínica Central",
                "Calle Principal 123",
                "+123456789",
                "central@clinic.com",
                LocalTime.of(8, 0),
                LocalTime.of(18, 0),
                true
        );

        when(clinicRepositoryPort.save(any(Clinic.class))).thenReturn(clinic);

        Clinic savedClinic = clinicRepositoryPort.save(clinic);

        assertThat(savedClinic).isNotNull();
        assertThat(savedClinic.getId()).isEqualTo(clinicId);
        assertThat(savedClinic.getCodigo()).isEqualTo("CLI-001");
        verify(clinicRepositoryPort, times(1)).save(clinic);
    }

    @Test
    void findById_WhenClinicExists_ShouldReturnClinic() {
        ClinicId clinicId = new ClinicId(UUID.randomUUID());
        Clinic clinic = Clinic.create(
                clinicId,
                "CLI-002",
                "Clínica Norte",
                "Avenida Norte 456",
                "+987654321",
                "norte@clinic.com",
                LocalTime.of(9, 0),
                LocalTime.of(17, 0),
                true
        );

        when(clinicRepositoryPort.findById(clinicId)).thenReturn(Optional.of(clinic));

        Optional<Clinic> foundClinic = clinicRepositoryPort.findById(clinicId);

        assertThat(foundClinic).isPresent();
        assertThat(foundClinic.get().getId()).isEqualTo(clinicId);
        assertThat(foundClinic.get().getNombre()).isEqualTo("Clínica Norte");
        verify(clinicRepositoryPort, times(1)).findById(clinicId);
    }

    @Test
    void findById_WhenClinicDoesNotExist_ShouldReturnEmpty() {
        ClinicId clinicId = new ClinicId(UUID.randomUUID());

        when(clinicRepositoryPort.findById(clinicId)).thenReturn(Optional.empty());

        Optional<Clinic> foundClinic = clinicRepositoryPort.findById(clinicId);

        assertThat(foundClinic).isEmpty();
        verify(clinicRepositoryPort, times(1)).findById(clinicId);
    }

    @Test
    void findAll_ShouldReturnListOfClinics() {
        ClinicId clinicId1 = new ClinicId(UUID.randomUUID());
        ClinicId clinicId2 = new ClinicId(UUID.randomUUID());

        Clinic clinic1 = Clinic.create(
                clinicId1,
                "CLI-001",
                "Clínica A",
                "Dirección A",
                "111111",
                "a@clinic.com",
                LocalTime.of(8, 0),
                LocalTime.of(16, 0),
                true
        );
        Clinic clinic2 = Clinic.create(
                clinicId2,
                "CLI-002",
                "Clínica B",
                "Dirección B",
                "222222",
                "b@clinic.com",
                LocalTime.of(9, 0),
                LocalTime.of(17, 0),
                false
        );

        List<Clinic> clinics = List.of(clinic1, clinic2);
        when(clinicRepositoryPort.findAll()).thenReturn(clinics);

        List<Clinic> result = clinicRepositoryPort.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(clinic1, clinic2);
        verify(clinicRepositoryPort, times(1)).findAll();
    }

    @Test
    void delete_ShouldRemoveClinic() {
        ClinicId clinicId = new ClinicId(UUID.randomUUID());
        Clinic clinic = Clinic.create(
                clinicId,
                "CLI-003",
                "Clínica Sur",
                "Calle Sur 789",
                "+555555555",
                "sur@clinic.com",
                LocalTime.of(10, 0),
                LocalTime.of(19, 0),
                true
        );

        doNothing().when(clinicRepositoryPort).delete(clinic);

        clinicRepositoryPort.delete(clinic);

        verify(clinicRepositoryPort, times(1)).delete(clinic);
    }

    @Test
    void existsById_WhenClinicExists_ShouldReturnTrue() {
        ClinicId clinicId = new ClinicId(UUID.randomUUID());

        when(clinicRepositoryPort.existsById(clinicId)).thenReturn(true);

        boolean exists = clinicRepositoryPort.existsById(clinicId);

        assertThat(exists).isTrue();
        verify(clinicRepositoryPort, times(1)).existsById(clinicId);
    }

    @Test
    void existsById_WhenClinicDoesNotExist_ShouldReturnFalse() {
        ClinicId clinicId = new ClinicId(UUID.randomUUID());

        when(clinicRepositoryPort.existsById(clinicId)).thenReturn(false);

        boolean exists = clinicRepositoryPort.existsById(clinicId);

        assertThat(exists).isFalse();
        verify(clinicRepositoryPort, times(1)).existsById(clinicId);
    }

    @Test
    void count_ShouldReturnNumberOfClinics() {
        when(clinicRepositoryPort.count()).thenReturn(5L);

        long count = clinicRepositoryPort.count();

        assertThat(count).isEqualTo(5L);
        verify(clinicRepositoryPort, times(1)).count();
    }
}