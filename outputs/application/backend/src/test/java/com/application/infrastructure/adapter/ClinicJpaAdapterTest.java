package com.application.infrastructure.adapter;

import com.application.domain.model.Clinic;
import com.application.domain.valueobject.ClinicId;
import com.application.infrastructure.entity.ClinicEntity;
import com.application.infrastructure.repository.ClinicJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
class ClinicJpaAdapterTest {

    @Mock
    private ClinicJpaRepository clinicJpaRepository;

    @InjectMocks
    private ClinicJpaAdapter clinicJpaAdapter;

    @Test
    void save_ShouldSaveClinicAndReturnDomain() {
        ClinicId clinicId = new ClinicId(UUID.randomUUID());
        Clinic clinic = Clinic.create(
                clinicId,
                "CLI-001",
                "Clínica Central",
                "Calle Principal 123",
                "555-1234",
                "central@clinica.com",
                LocalTime.of(8, 0),
                LocalTime.of(18, 0),
                true
        );
        ClinicEntity entity = ClinicEntity.fromDomain(clinic);
        when(clinicJpaRepository.save(any(ClinicEntity.class))).thenReturn(entity);

        Clinic result = clinicJpaAdapter.save(clinic);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(clinicId);
        assertThat(result.getCodigo()).isEqualTo("CLI-001");
        verify(clinicJpaRepository).save(any(ClinicEntity.class));
    }

    @Test
    void findById_WhenEntityExists_ShouldReturnOptionalClinic() {
        ClinicId clinicId = new ClinicId(UUID.randomUUID());
        ClinicEntity entity = mock(ClinicEntity.class);
        Clinic clinic = mock(Clinic.class);
        when(clinicJpaRepository.findById(clinicId.getValue())).thenReturn(Optional.of(entity));
        when(entity.toDomain()).thenReturn(clinic);

        Optional<Clinic> result = clinicJpaAdapter.findById(clinicId);

        assertThat(result).isPresent().contains(clinic);
        verify(clinicJpaRepository).findById(clinicId.getValue());
    }

    @Test
    void findById_WhenEntityDoesNotExist_ShouldReturnEmptyOptional() {
        ClinicId clinicId = new ClinicId(UUID.randomUUID());
        when(clinicJpaRepository.findById(clinicId.getValue())).thenReturn(Optional.empty());

        Optional<Clinic> result = clinicJpaAdapter.findById(clinicId);

        assertThat(result).isEmpty();
        verify(clinicJpaRepository).findById(clinicId.getValue());
    }

    @Test
    void findAll_ShouldReturnListOfClinics() {
        ClinicEntity entity1 = mock(ClinicEntity.class);
        ClinicEntity entity2 = mock(ClinicEntity.class);
        Clinic clinic1 = mock(Clinic.class);
        Clinic clinic2 = mock(Clinic.class);
        when(clinicJpaRepository.findAll()).thenReturn(List.of(entity1, entity2));
        when(entity1.toDomain()).thenReturn(clinic1);
        when(entity2.toDomain()).thenReturn(clinic2);

        List<Clinic> result = clinicJpaAdapter.findAll();

        assertThat(result).hasSize(2).containsExactly(clinic1, clinic2);
        verify(clinicJpaRepository).findAll();
    }

    @Test
    void findAllActive_ShouldReturnListOfActiveClinics() {
        ClinicEntity entity1 = mock(ClinicEntity.class);
        ClinicEntity entity2 = mock(ClinicEntity.class);
        Clinic clinic1 = mock(Clinic.class);
        Clinic clinic2 = mock(Clinic.class);
        when(clinicJpaRepository.findByActivaTrue()).thenReturn(List.of(entity1, entity2));
        when(entity1.toDomain()).thenReturn(clinic1);
        when(entity2.toDomain()).thenReturn(clinic2);

        List<Clinic> result = clinicJpaAdapter.findAllActive();

        assertThat(result).hasSize(2).containsExactly(clinic1, clinic2);
        verify(clinicJpaRepository).findByActivaTrue();
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        ClinicId clinicId = new ClinicId(UUID.randomUUID());

        clinicJpaAdapter.deleteById(clinicId);

        verify(clinicJpaRepository).deleteById(clinicId.getValue());
    }

    @Test
    void existsById_WhenEntityExists_ShouldReturnTrue() {
        ClinicId clinicId = new ClinicId(UUID.randomUUID());
        when(clinicJpaRepository.existsById(clinicId.getValue())).thenReturn(true);

        boolean result = clinicJpaAdapter.existsById(clinicId);

        assertThat(result).isTrue();
        verify(clinicJpaRepository).existsById(clinicId.getValue());
    }

    @Test
    void existsById_WhenEntityDoesNotExist_ShouldReturnFalse() {
        ClinicId clinicId = new ClinicId(UUID.randomUUID());
        when(clinicJpaRepository.existsById(clinicId.getValue())).thenReturn(false);

        boolean result = clinicJpaAdapter.existsById(clinicId);

        assertThat(result).isFalse();
        verify(clinicJpaRepository).existsById(clinicId.getValue());
    }

    @Test
    void findByCode_WhenEntityExists_ShouldReturnOptionalClinic() {
        String code = "CLI-001";
        ClinicEntity entity = mock(ClinicEntity.class);
        Clinic clinic = mock(Clinic.class);
        when(clinicJpaRepository.findByCodigo(code)).thenReturn(Optional.of(entity));
        when(entity.toDomain()).thenReturn(clinic);

        Optional<Clinic> result = clinicJpaAdapter.findByCode(code);

        assertThat(result).isPresent().contains(clinic);
        verify(clinicJpaRepository).findByCodigo(code);
    }

    @Test
    void findByCode_WhenEntityDoesNotExist_ShouldReturnEmptyOptional() {
        String code = "CLI-999";
        when(clinicJpaRepository.findByCodigo(code)).thenReturn(Optional.empty());

        Optional<Clinic> result = clinicJpaAdapter.findByCode(code);

        assertThat(result).isEmpty();
        verify(clinicJpaRepository).findByCodigo(code);
    }
}