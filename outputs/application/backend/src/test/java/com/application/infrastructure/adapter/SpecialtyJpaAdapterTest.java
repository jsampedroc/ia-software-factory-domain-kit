package com.application.infrastructure.adapter;

import com.application.domain.model.Specialty;
import com.application.domain.valueobject.SpecialtyId;
import com.application.infrastructure.entity.SpecialtyEntity;
import com.application.infrastructure.repository.SpecialtyJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialtyJpaAdapterTest {

    @Mock
    private SpecialtyJpaRepository specialtyJpaRepository;

    @InjectMocks
    private SpecialtyJpaAdapter specialtyJpaAdapter;

    @Test
    void save_shouldConvertEntityAndSave() {
        SpecialtyId specialtyId = new SpecialtyId(UUID.randomUUID());
        Specialty specialty = Specialty.create(
                specialtyId,
                "ORT",
                "Ortodoncia",
                "Especialidad en ortodoncia"
        );
        SpecialtyEntity entity = SpecialtyEntity.fromDomain(specialty);
        when(specialtyJpaRepository.save(any(SpecialtyEntity.class))).thenReturn(entity);

        Specialty result = specialtyJpaAdapter.save(specialty);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(specialtyId);
        assertThat(result.getCodigo()).isEqualTo("ORT");
        verify(specialtyJpaRepository).save(any(SpecialtyEntity.class));
    }

    @Test
    void findById_shouldReturnSpecialtyWhenExists() {
        SpecialtyId specialtyId = new SpecialtyId(UUID.randomUUID());
        Specialty specialty = Specialty.create(
                specialtyId,
                "PER",
                "Periodoncia",
                "Especialidad en periodoncia"
        );
        SpecialtyEntity entity = SpecialtyEntity.fromDomain(specialty);
        when(specialtyJpaRepository.findById(specialtyId.getValue())).thenReturn(Optional.of(entity));

        Optional<Specialty> result = specialtyJpaAdapter.findById(specialtyId);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(specialtyId);
        verify(specialtyJpaRepository).findById(specialtyId.getValue());
    }

    @Test
    void findById_shouldReturnEmptyWhenNotExists() {
        SpecialtyId specialtyId = new SpecialtyId(UUID.randomUUID());
        when(specialtyJpaRepository.findById(specialtyId.getValue())).thenReturn(Optional.empty());

        Optional<Specialty> result = specialtyJpaAdapter.findById(specialtyId);

        assertThat(result).isEmpty();
        verify(specialtyJpaRepository).findById(specialtyId.getValue());
    }

    @Test
    void findAll_shouldReturnListOfSpecialties() {
        SpecialtyId id1 = new SpecialtyId(UUID.randomUUID());
        SpecialtyId id2 = new SpecialtyId(UUID.randomUUID());
        Specialty specialty1 = Specialty.create(id1, "ORT", "Ortodoncia", "Desc1");
        Specialty specialty2 = Specialty.create(id2, "END", "Endodoncia", "Desc2");
        SpecialtyEntity entity1 = SpecialtyEntity.fromDomain(specialty1);
        SpecialtyEntity entity2 = SpecialtyEntity.fromDomain(specialty2);
        when(specialtyJpaRepository.findAll()).thenReturn(List.of(entity1, entity2));

        List<Specialty> result = specialtyJpaAdapter.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(id1);
        assertThat(result.get(1).getId()).isEqualTo(id2);
        verify(specialtyJpaRepository).findAll();
    }

    @Test
    void deleteById_shouldCallRepositoryDelete() {
        SpecialtyId specialtyId = new SpecialtyId(UUID.randomUUID());

        specialtyJpaAdapter.deleteById(specialtyId);

        verify(specialtyJpaRepository).deleteById(specialtyId.getValue());
    }

    @Test
    void existsById_shouldReturnTrueWhenExists() {
        SpecialtyId specialtyId = new SpecialtyId(UUID.randomUUID());
        when(specialtyJpaRepository.existsById(specialtyId.getValue())).thenReturn(true);

        boolean result = specialtyJpaAdapter.existsById(specialtyId);

        assertThat(result).isTrue();
        verify(specialtyJpaRepository).existsById(specialtyId.getValue());
    }

    @Test
    void existsById_shouldReturnFalseWhenNotExists() {
        SpecialtyId specialtyId = new SpecialtyId(UUID.randomUUID());
        when(specialtyJpaRepository.existsById(specialtyId.getValue())).thenReturn(false);

        boolean result = specialtyJpaAdapter.existsById(specialtyId);

        assertThat(result).isFalse();
        verify(specialtyJpaRepository).existsById(specialtyId.getValue());
    }

    @Test
    void findByCode_shouldReturnSpecialtyWhenCodeExists() {
        String code = "ORT";
        SpecialtyId specialtyId = new SpecialtyId(UUID.randomUUID());
        Specialty specialty = Specialty.create(specialtyId, code, "Ortodoncia", "Desc");
        SpecialtyEntity entity = SpecialtyEntity.fromDomain(specialty);
        when(specialtyJpaRepository.findByCodigo(code)).thenReturn(Optional.of(entity));

        Optional<Specialty> result = specialtyJpaAdapter.findByCode(code);

        assertThat(result).isPresent();
        assertThat(result.get().getCodigo()).isEqualTo(code);
        verify(specialtyJpaRepository).findByCodigo(code);
    }

    @Test
    void findByCode_shouldReturnEmptyWhenCodeNotExists() {
        String code = "INVALID";
        when(specialtyJpaRepository.findByCodigo(code)).thenReturn(Optional.empty());

        Optional<Specialty> result = specialtyJpaAdapter.findByCode(code);

        assertThat(result).isEmpty();
        verify(specialtyJpaRepository).findByCodigo(code);
    }

    @Test
    void findByNameContaining_shouldReturnMatchingSpecialties() {
        String nameFragment = "odon";
        SpecialtyId id1 = new SpecialtyId(UUID.randomUUID());
        SpecialtyId id2 = new SpecialtyId(UUID.randomUUID());
        Specialty specialty1 = Specialty.create(id1, "ORT", "Ortodoncia", "Desc1");
        Specialty specialty2 = Specialty.create(id2, "END", "Endodoncia", "Desc2");
        SpecialtyEntity entity1 = SpecialtyEntity.fromDomain(specialty1);
        SpecialtyEntity entity2 = SpecialtyEntity.fromDomain(specialty2);
        when(specialtyJpaRepository.findByNombreContainingIgnoreCase(nameFragment))
                .thenReturn(List.of(entity1, entity2));

        List<Specialty> result = specialtyJpaAdapter.findByNameContaining(nameFragment);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getNombre()).containsIgnoringCase(nameFragment);
        assertThat(result.get(1).getNombre()).containsIgnoringCase(nameFragment);
        verify(specialtyJpaRepository).findByNombreContainingIgnoreCase(nameFragment);
    }
}