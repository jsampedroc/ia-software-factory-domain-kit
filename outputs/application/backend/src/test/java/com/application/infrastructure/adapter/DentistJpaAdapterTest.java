package com.application.infrastructure.adapter;

import com.application.domain.model.Dentist;
import com.application.domain.valueobject.DentistId;
import com.application.infrastructure.entity.DentistEntity;
import com.application.infrastructure.repository.DentistJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DentistJpaAdapterTest {

    @Mock
    private DentistJpaRepository dentistJpaRepository;

    @InjectMocks
    private DentistJpaAdapter dentistJpaAdapter;

    @Test
    void save_ShouldSaveAndReturnDomain() {
        // Given
        DentistId dentistId = new DentistId(UUID.randomUUID());
        Dentist dentist = Dentist.create(
                dentistId,
                "MED12345",
                "Juan",
                "Pérez",
                "+123456789",
                "juan.perez@clinica.com",
                LocalDate.of(2020, 1, 15),
                true
        );
        DentistEntity entity = DentistEntity.fromDomain(dentist);
        when(dentistJpaRepository.save(any(DentistEntity.class))).thenReturn(entity);

        // When
        Dentist savedDentist = dentistJpaAdapter.save(dentist);

        // Then
        assertThat(savedDentist).isNotNull();
        assertThat(savedDentist.getId()).isEqualTo(dentistId);
        verify(dentistJpaRepository, times(1)).save(any(DentistEntity.class));
    }

    @Test
    void findById_WhenExists_ShouldReturnOptionalDomain() {
        // Given
        DentistId dentistId = new DentistId(UUID.randomUUID());
        DentistEntity entity = mock(DentistEntity.class);
        Dentist dentist = mock(Dentist.class);
        when(dentistJpaRepository.findById(dentistId.value())).thenReturn(Optional.of(entity));
        when(entity.toDomain()).thenReturn(dentist);

        // When
        Optional<Dentist> result = dentistJpaAdapter.findById(dentistId);

        // Then
        assertThat(result).isPresent().contains(dentist);
        verify(dentistJpaRepository, times(1)).findById(dentistId.value());
    }

    @Test
    void findById_WhenNotExists_ShouldReturnEmptyOptional() {
        // Given
        DentistId dentistId = new DentistId(UUID.randomUUID());
        when(dentistJpaRepository.findById(dentistId.value())).thenReturn(Optional.empty());

        // When
        Optional<Dentist> result = dentistJpaAdapter.findById(dentistId);

        // Then
        assertThat(result).isEmpty();
        verify(dentistJpaRepository, times(1)).findById(dentistId.value());
    }

    @Test
    void findAll_ShouldReturnListOfDomain() {
        // Given
        DentistEntity entity1 = mock(DentistEntity.class);
        DentistEntity entity2 = mock(DentistEntity.class);
        Dentist dentist1 = mock(Dentist.class);
        Dentist dentist2 = mock(Dentist.class);
        when(dentistJpaRepository.findAll()).thenReturn(List.of(entity1, entity2));
        when(entity1.toDomain()).thenReturn(dentist1);
        when(entity2.toDomain()).thenReturn(dentist2);

        // When
        List<Dentist> result = dentistJpaAdapter.findAll();

        // Then
        assertThat(result).hasSize(2).containsExactly(dentist1, dentist2);
        verify(dentistJpaRepository, times(1)).findAll();
    }

    @Test
    void findAllActive_ShouldReturnListOfActiveDomain() {
        // Given
        DentistEntity entity1 = mock(DentistEntity.class);
        DentistEntity entity2 = mock(DentistEntity.class);
        Dentist dentist1 = mock(Dentist.class);
        Dentist dentist2 = mock(Dentist.class);
        when(dentistJpaRepository.findByActivoTrue()).thenReturn(List.of(entity1, entity2));
        when(entity1.toDomain()).thenReturn(dentist1);
        when(entity2.toDomain()).thenReturn(dentist2);

        // When
        List<Dentist> result = dentistJpaAdapter.findAllActive();

        // Then
        assertThat(result).hasSize(2).containsExactly(dentist1, dentist2);
        verify(dentistJpaRepository, times(1)).findByActivoTrue();
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        // Given
        DentistId dentistId = new DentistId(UUID.randomUUID());

        // When
        dentistJpaAdapter.deleteById(dentistId);

        // Then
        verify(dentistJpaRepository, times(1)).deleteById(dentistId.value());
    }

    @Test
    void existsById_WhenExists_ShouldReturnTrue() {
        // Given
        DentistId dentistId = new DentistId(UUID.randomUUID());
        when(dentistJpaRepository.existsById(dentistId.value())).thenReturn(true);

        // When
        boolean result = dentistJpaAdapter.existsById(dentistId);

        // Then
        assertThat(result).isTrue();
        verify(dentistJpaRepository, times(1)).existsById(dentistId.value());
    }

    @Test
    void existsById_WhenNotExists_ShouldReturnFalse() {
        // Given
        DentistId dentistId = new DentistId(UUID.randomUUID());
        when(dentistJpaRepository.existsById(dentistId.value())).thenReturn(false);

        // When
        boolean result = dentistJpaAdapter.existsById(dentistId);

        // Then
        assertThat(result).isFalse();
        verify(dentistJpaRepository, times(1)).existsById(dentistId.value());
    }

    @Test
    void findByMedicalLicense_WhenExists_ShouldReturnOptionalDomain() {
        // Given
        String medicalLicense = "MED98765";
        DentistEntity entity = mock(DentistEntity.class);
        Dentist dentist = mock(Dentist.class);
        when(dentistJpaRepository.findByLicenciaMedica(medicalLicense)).thenReturn(Optional.of(entity));
        when(entity.toDomain()).thenReturn(dentist);

        // When
        Optional<Dentist> result = dentistJpaAdapter.findByMedicalLicense(medicalLicense);

        // Then
        assertThat(result).isPresent().contains(dentist);
        verify(dentistJpaRepository, times(1)).findByLicenciaMedica(medicalLicense);
    }

    @Test
    void findByMedicalLicense_WhenNotExists_ShouldReturnEmptyOptional() {
        // Given
        String medicalLicense = "NONEXISTENT";
        when(dentistJpaRepository.findByLicenciaMedica(medicalLicense)).thenReturn(Optional.empty());

        // When
        Optional<Dentist> result = dentistJpaAdapter.findByMedicalLicense(medicalLicense);

        // Then
        assertThat(result).isEmpty();
        verify(dentistJpaRepository, times(1)).findByLicenciaMedica(medicalLicense);
    }

    @Test
    void findByClinicId_ShouldReturnListOfDomain() {
        // Given
        String clinicId = UUID.randomUUID().toString();
        DentistEntity entity1 = mock(DentistEntity.class);
        DentistEntity entity2 = mock(DentistEntity.class);
        Dentist dentist1 = mock(Dentist.class);
        Dentist dentist2 = mock(Dentist.class);
        when(dentistJpaRepository.findByClinicId(clinicId)).thenReturn(List.of(entity1, entity2));
        when(entity1.toDomain()).thenReturn(dentist1);
        when(entity2.toDomain()).thenReturn(dentist2);

        // When
        List<Dentist> result = dentistJpaAdapter.findByClinicId(clinicId);

        // Then
        assertThat(result).hasSize(2).containsExactly(dentist1, dentist2);
        verify(dentistJpaRepository, times(1)).findByClinicId(clinicId);
    }

    @Test
    void findBySpecialtyId_ShouldReturnListOfDomain() {
        // Given
        String specialtyId = UUID.randomUUID().toString();
        DentistEntity entity1 = mock(DentistEntity.class);
        DentistEntity entity2 = mock(DentistEntity.class);
        Dentist dentist1 = mock(Dentist.class);
        Dentist dentist2 = mock(Dentist.class);
        when(dentistJpaRepository.findBySpecialtyId(specialtyId)).thenReturn(List.of(entity1, entity2));
        when(entity1.toDomain()).thenReturn(dentist1);
        when(entity2.toDomain()).thenReturn(dentist2);

        // When
        List<Dentist> result = dentistJpaAdapter.findBySpecialtyId(specialtyId);

        // Then
        assertThat(result).hasSize(2).containsExactly(dentist1, dentist2);
        verify(dentistJpaRepository, times(1)).findBySpecialtyId(specialtyId);
    }
}