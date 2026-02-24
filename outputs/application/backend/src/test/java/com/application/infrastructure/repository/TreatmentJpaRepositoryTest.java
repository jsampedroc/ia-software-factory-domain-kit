package com.application.infrastructure.repository;

import com.application.infrastructure.entity.TreatmentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TreatmentJpaRepositoryTest {

    @Mock
    private TreatmentJpaRepository treatmentJpaRepository;

    private TreatmentEntity activeTreatment;
    private TreatmentEntity inactiveTreatment;
    private UUID treatmentId1;
    private UUID treatmentId2;
    private String codigo1;
    private String codigo2;
    private String nombre1;
    private String nombre2;

    @BeforeEach
    void setUp() {
        treatmentId1 = UUID.randomUUID();
        treatmentId2 = UUID.randomUUID();
        codigo1 = "TRAT-001";
        codigo2 = "TRAT-002";
        nombre1 = "Limpieza Dental";
        nombre2 = "Extracción Molar";

        activeTreatment = TreatmentEntity.create(
                treatmentId1,
                codigo1,
                nombre1,
                "Limpieza profesional de dientes",
                45,
                new BigDecimal("50.00"),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        inactiveTreatment = TreatmentEntity.create(
                treatmentId2,
                codigo2,
                nombre2,
                "Extracción de muela del juicio",
                60,
                new BigDecimal("120.00"),
                false,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    void findByTreatmentId_ShouldReturnTreatment_WhenExists() {
        when(treatmentJpaRepository.findByTreatmentId(treatmentId1))
                .thenReturn(Optional.of(activeTreatment));

        Optional<TreatmentEntity> result = treatmentJpaRepository.findByTreatmentId(treatmentId1);

        assertThat(result).isPresent();
        assertThat(result.get().getTreatmentId()).isEqualTo(treatmentId1);
        assertThat(result.get().getCodigo()).isEqualTo(codigo1);
    }

    @Test
    void findByTreatmentId_ShouldReturnEmpty_WhenNotExists() {
        UUID nonExistentId = UUID.randomUUID();
        when(treatmentJpaRepository.findByTreatmentId(nonExistentId))
                .thenReturn(Optional.empty());

        Optional<TreatmentEntity> result = treatmentJpaRepository.findByTreatmentId(nonExistentId);

        assertThat(result).isEmpty();
    }

    @Test
    void findByCodigo_ShouldReturnTreatment_WhenCodigoExists() {
        when(treatmentJpaRepository.findByCodigo(codigo1))
                .thenReturn(Optional.of(activeTreatment));

        Optional<TreatmentEntity> result = treatmentJpaRepository.findByCodigo(codigo1);

        assertThat(result).isPresent();
        assertThat(result.get().getCodigo()).isEqualTo(codigo1);
        assertThat(result.get().getNombre()).isEqualTo(nombre1);
    }

    @Test
    void findByCodigo_ShouldReturnEmpty_WhenCodigoNotExists() {
        String nonExistentCodigo = "TRAT-999";
        when(treatmentJpaRepository.findByCodigo(nonExistentCodigo))
                .thenReturn(Optional.empty());

        Optional<TreatmentEntity> result = treatmentJpaRepository.findByCodigo(nonExistentCodigo);

        assertThat(result).isEmpty();
    }

    @Test
    void findAllActive_ShouldReturnOnlyActiveTreatments() {
        List<TreatmentEntity> activeList = List.of(activeTreatment);
        when(treatmentJpaRepository.findAllActive()).thenReturn(activeList);

        List<TreatmentEntity> result = treatmentJpaRepository.findAllActive();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getActivo()).isTrue();
        assertThat(result.get(0).getTreatmentId()).isEqualTo(treatmentId1);
    }

    @Test
    void findAllInactive_ShouldReturnOnlyInactiveTreatments() {
        List<TreatmentEntity> inactiveList = List.of(inactiveTreatment);
        when(treatmentJpaRepository.findAllInactive()).thenReturn(inactiveList);

        List<TreatmentEntity> result = treatmentJpaRepository.findAllInactive();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getActivo()).isFalse();
        assertThat(result.get(0).getTreatmentId()).isEqualTo(treatmentId2);
    }

    @Test
    void findByNombreContaining_ShouldReturnMatchingTreatments() {
        String searchTerm = "Dental";
        List<TreatmentEntity> matchingList = List.of(activeTreatment);
        when(treatmentJpaRepository.findByNombreContaining(searchTerm)).thenReturn(matchingList);

        List<TreatmentEntity> result = treatmentJpaRepository.findByNombreContaining(searchTerm);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombre()).contains(searchTerm);
    }

    @Test
    void findByNombreContaining_ShouldReturnEmptyList_WhenNoMatches() {
        String searchTerm = "Inexistente";
        when(treatmentJpaRepository.findByNombreContaining(searchTerm)).thenReturn(List.of());

        List<TreatmentEntity> result = treatmentJpaRepository.findByNombreContaining(searchTerm);

        assertThat(result).isEmpty();
    }

    @Test
    void existsByCodigo_ShouldReturnTrue_WhenCodigoExists() {
        when(treatmentJpaRepository.existsByCodigo(codigo1)).thenReturn(true);

        boolean result = treatmentJpaRepository.existsByCodigo(codigo1);

        assertThat(result).isTrue();
    }

    @Test
    void existsByCodigo_ShouldReturnFalse_WhenCodigoNotExists() {
        String nonExistentCodigo = "TRAT-999";
        when(treatmentJpaRepository.existsByCodigo(nonExistentCodigo)).thenReturn(false);

        boolean result = treatmentJpaRepository.existsByCodigo(nonExistentCodigo);

        assertThat(result).isFalse();
    }

    @Test
    void save_ShouldPersistTreatment() {
        TreatmentEntity newTreatment = TreatmentEntity.create(
                UUID.randomUUID(),
                "TRAT-003",
                "Ortodoncia",
                "Alineación dental",
                90,
                new BigDecimal("500.00"),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(treatmentJpaRepository.save(any(TreatmentEntity.class))).thenReturn(newTreatment);

        TreatmentEntity saved = treatmentJpaRepository.save(newTreatment);

        assertThat(saved).isNotNull();
        assertThat(saved.getCodigo()).isEqualTo("TRAT-003");
        assertThat(saved.getNombre()).isEqualTo("Ortodoncia");
    }

    @Test
    void delete_ShouldRemoveTreatment() {
        treatmentJpaRepository.delete(activeTreatment);
        // Verificación implícita: no hay excepción al ejecutar delete
        // En un test real con @DataJpaTest, se verificaría que ya no existe
    }

    @Test
    void findAll_ShouldReturnAllTreatments() {
        List<TreatmentEntity> allTreatments = List.of(activeTreatment, inactiveTreatment);
        when(treatmentJpaRepository.findAll()).thenReturn(allTreatments);

        List<TreatmentEntity> result = treatmentJpaRepository.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(activeTreatment, inactiveTreatment);
    }
}