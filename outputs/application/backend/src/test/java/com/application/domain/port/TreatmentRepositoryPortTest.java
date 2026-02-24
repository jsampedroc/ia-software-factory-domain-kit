package com.application.domain.port;

import com.application.domain.model.Treatment;
import com.application.domain.valueobject.TreatmentId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TreatmentRepositoryPortTest {

    @Mock
    private TreatmentRepositoryPort treatmentRepositoryPort;

    @Test
    void save_ShouldPersistTreatment() {
        TreatmentId treatmentId = new TreatmentId(UUID.randomUUID());
        Treatment treatment = Treatment.create(
                treatmentId,
                "TR001",
                "Limpieza Dental",
                "Limpieza profesional de dientes",
                45,
                new BigDecimal("75.00"),
                true
        );

        when(treatmentRepositoryPort.save(any(Treatment.class))).thenReturn(treatment);

        Treatment savedTreatment = treatmentRepositoryPort.save(treatment);

        assertThat(savedTreatment).isNotNull();
        assertThat(savedTreatment.getId()).isEqualTo(treatmentId);
        verify(treatmentRepositoryPort, times(1)).save(treatment);
    }

    @Test
    void findById_WhenTreatmentExists_ShouldReturnTreatment() {
        TreatmentId treatmentId = new TreatmentId(UUID.randomUUID());
        Treatment treatment = Treatment.create(
                treatmentId,
                "TR002",
                "Extracción",
                "Extracción de muela del juicio",
                60,
                new BigDecimal("150.00"),
                true
        );

        when(treatmentRepositoryPort.findById(treatmentId)).thenReturn(Optional.of(treatment));

        Optional<Treatment> foundTreatment = treatmentRepositoryPort.findById(treatmentId);

        assertThat(foundTreatment).isPresent();
        assertThat(foundTreatment.get().getId()).isEqualTo(treatmentId);
        verify(treatmentRepositoryPort, times(1)).findById(treatmentId);
    }

    @Test
    void findById_WhenTreatmentNotExists_ShouldReturnEmpty() {
        TreatmentId treatmentId = new TreatmentId(UUID.randomUUID());

        when(treatmentRepositoryPort.findById(treatmentId)).thenReturn(Optional.empty());

        Optional<Treatment> foundTreatment = treatmentRepositoryPort.findById(treatmentId);

        assertThat(foundTreatment).isEmpty();
        verify(treatmentRepositoryPort, times(1)).findById(treatmentId);
    }

    @Test
    void findAll_ShouldReturnAllTreatments() {
        Treatment treatment1 = Treatment.create(
                new TreatmentId(UUID.randomUUID()),
                "TR003",
                "Ortodoncia",
                "Alineación dental",
                90,
                new BigDecimal("500.00"),
                true
        );
        Treatment treatment2 = Treatment.create(
                new TreatmentId(UUID.randomUUID()),
                "TR004",
                "Blanqueamiento",
                "Blanqueamiento dental estético",
                30,
                new BigDecimal("200.00"),
                true
        );
        List<Treatment> treatments = List.of(treatment1, treatment2);

        when(treatmentRepositoryPort.findAll()).thenReturn(treatments);

        List<Treatment> allTreatments = treatmentRepositoryPort.findAll();

        assertThat(allTreatments).hasSize(2);
        verify(treatmentRepositoryPort, times(1)).findAll();
    }

    @Test
    void delete_ShouldRemoveTreatment() {
        TreatmentId treatmentId = new TreatmentId(UUID.randomUUID());

        doNothing().when(treatmentRepositoryPort).delete(treatmentId);

        treatmentRepositoryPort.delete(treatmentId);

        verify(treatmentRepositoryPort, times(1)).delete(treatmentId);
    }

    @Test
    void existsById_WhenTreatmentExists_ShouldReturnTrue() {
        TreatmentId treatmentId = new TreatmentId(UUID.randomUUID());

        when(treatmentRepositoryPort.existsById(treatmentId)).thenReturn(true);

        boolean exists = treatmentRepositoryPort.existsById(treatmentId);

        assertThat(exists).isTrue();
        verify(treatmentRepositoryPort, times(1)).existsById(treatmentId);
    }

    @Test
    void existsById_WhenTreatmentNotExists_ShouldReturnFalse() {
        TreatmentId treatmentId = new TreatmentId(UUID.randomUUID());

        when(treatmentRepositoryPort.existsById(treatmentId)).thenReturn(false);

        boolean exists = treatmentRepositoryPort.existsById(treatmentId);

        assertThat(exists).isFalse();
        verify(treatmentRepositoryPort, times(1)).existsById(treatmentId);
    }

    @Test
    void count_ShouldReturnNumberOfTreatments() {
        when(treatmentRepositoryPort.count()).thenReturn(5L);

        long count = treatmentRepositoryPort.count();

        assertThat(count).isEqualTo(5L);
        verify(treatmentRepositoryPort, times(1)).count();
    }
}