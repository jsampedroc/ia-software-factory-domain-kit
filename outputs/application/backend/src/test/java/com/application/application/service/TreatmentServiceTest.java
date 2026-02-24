package com.application.application.service;

import com.application.application.dto.TreatmentDTO;
import com.application.application.mapper.TreatmentMapper;
import com.application.domain.exception.DomainException;
import com.application.domain.model.Treatment;
import com.application.domain.port.TreatmentRepositoryPort;
import com.application.domain.valueobject.TreatmentId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TreatmentServiceTest {

    @Mock
    private TreatmentRepositoryPort treatmentRepositoryPort;

    @Mock
    private TreatmentMapper treatmentMapper;

    @InjectMocks
    private TreatmentService treatmentService;

    private TreatmentId treatmentId;
    private TreatmentDTO validTreatmentDTO;
    private Treatment mockTreatment;

    @BeforeEach
    void setUp() {
        treatmentId = new TreatmentId(UUID.randomUUID());

        validTreatmentDTO = new TreatmentDTO(
                treatmentId,
                "TRAT-001",
                "Limpieza Dental",
                "Limpieza profesional de dientes",
                45,
                new BigDecimal("75.00"),
                true
        );

        mockTreatment = mock(Treatment.class);
        when(mockTreatment.getId()).thenReturn(treatmentId);
    }

    @Test
    void createTreatment_WithValidData_ShouldReturnTreatmentDTO() {
        // Arrange
        Treatment savedTreatment = mock(Treatment.class);
        when(treatmentRepositoryPort.save(any(Treatment.class))).thenReturn(savedTreatment);
        when(treatmentMapper.toDTO(savedTreatment)).thenReturn(validTreatmentDTO);

        // Act
        TreatmentDTO result = treatmentService.createTreatment(validTreatmentDTO);

        // Assert
        assertThat(result).isEqualTo(validTreatmentDTO);
        verify(treatmentRepositoryPort).save(any(Treatment.class));
        verify(treatmentMapper).toDTO(savedTreatment);
    }

    @Test
    void createTreatment_WithNullCode_ShouldThrowDomainException() {
        // Arrange
        TreatmentDTO invalidDTO = new TreatmentDTO(
                null, null, "Limpieza", "Desc", 45, new BigDecimal("75.00"), true
        );

        // Act & Assert
        assertThatThrownBy(() -> treatmentService.createTreatment(invalidDTO))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("El código del tratamiento es obligatorio");
        verify(treatmentRepositoryPort, never()).save(any());
    }

    @Test
    void createTreatment_WithZeroCost_ShouldThrowDomainException() {
        // Arrange
        TreatmentDTO invalidDTO = new TreatmentDTO(
                null, "CODE", "Nombre", "Desc", 45, BigDecimal.ZERO, true
        );

        // Act & Assert
        assertThatThrownBy(() -> treatmentService.createTreatment(invalidDTO))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("El costo base debe ser mayor a cero");
    }

    @Test
    void getTreatmentById_WhenTreatmentExists_ShouldReturnOptionalDTO() {
        // Arrange
        when(treatmentRepositoryPort.findById(treatmentId)).thenReturn(Optional.of(mockTreatment));
        when(treatmentMapper.toDTO(mockTreatment)).thenReturn(validTreatmentDTO);

        // Act
        Optional<TreatmentDTO> result = treatmentService.getTreatmentById(treatmentId);

        // Assert
        assertThat(result).isPresent().contains(validTreatmentDTO);
        verify(treatmentRepositoryPort).findById(treatmentId);
        verify(treatmentMapper).toDTO(mockTreatment);
    }

    @Test
    void getTreatmentById_WhenTreatmentNotExists_ShouldReturnEmptyOptional() {
        // Arrange
        when(treatmentRepositoryPort.findById(treatmentId)).thenReturn(Optional.empty());

        // Act
        Optional<TreatmentDTO> result = treatmentService.getTreatmentById(treatmentId);

        // Assert
        assertThat(result).isEmpty();
        verify(treatmentRepositoryPort).findById(treatmentId);
        verify(treatmentMapper, never()).toDTO(any());
    }

    @Test
    void getAllTreatments_ShouldReturnListOfDTOs() {
        // Arrange
        List<Treatment> treatments = List.of(mockTreatment);
        List<TreatmentDTO> dtos = List.of(validTreatmentDTO);
        when(treatmentRepositoryPort.findAll()).thenReturn(treatments);
        when(treatmentMapper.toDTO(mockTreatment)).thenReturn(validTreatmentDTO);

        // Act
        List<TreatmentDTO> result = treatmentService.getAllTreatments();

        // Assert
        assertThat(result).isEqualTo(dtos);
        verify(treatmentRepositoryPort).findAll();
        verify(treatmentMapper).toDTO(mockTreatment);
    }

    @Test
    void getActiveTreatments_ShouldReturnListOfActiveDTOs() {
        // Arrange
        List<Treatment> activeTreatments = List.of(mockTreatment);
        List<TreatmentDTO> dtos = List.of(validTreatmentDTO);
        when(treatmentRepositoryPort.findActiveTreatments()).thenReturn(activeTreatments);
        when(treatmentMapper.toDTO(mockTreatment)).thenReturn(validTreatmentDTO);

        // Act
        List<TreatmentDTO> result = treatmentService.getActiveTreatments();

        // Assert
        assertThat(result).isEqualTo(dtos);
        verify(treatmentRepositoryPort).findActiveTreatments();
        verify(treatmentMapper).toDTO(mockTreatment);
    }

    @Test
    void updateTreatment_WithValidData_ShouldReturnUpdatedDTO() {
        // Arrange
        TreatmentDTO updatedDTO = new TreatmentDTO(
                treatmentId,
                "TRAT-001-UPD",
                "Limpieza Actualizada",
                "Desc actualizada",
                60,
                new BigDecimal("85.00"),
                true
        );
        Treatment updatedTreatment = mock(Treatment.class);

        when(treatmentRepositoryPort.findById(treatmentId)).thenReturn(Optional.of(mockTreatment));
        when(treatmentRepositoryPort.isTreatmentInActivePlan(treatmentId)).thenReturn(false);
        when(mockTreatment.update(
                updatedDTO.codigo(),
                updatedDTO.nombre(),
                updatedDTO.descripcion(),
                updatedDTO.duracionEstimadaMinutos(),
                updatedDTO.costoBase(),
                updatedDTO.activo()
        )).thenReturn(updatedTreatment);
        when(treatmentRepositoryPort.save(updatedTreatment)).thenReturn(updatedTreatment);
        when(treatmentMapper.toDTO(updatedTreatment)).thenReturn(updatedDTO);

        // Act
        TreatmentDTO result = treatmentService.updateTreatment(treatmentId, updatedDTO);

        // Assert
        assertThat(result).isEqualTo(updatedDTO);
        verify(treatmentRepositoryPort).findById(treatmentId);
        verify(treatmentRepositoryPort).isTreatmentInActivePlan(treatmentId);
        verify(mockTreatment).update(
                updatedDTO.codigo(),
                updatedDTO.nombre(),
                updatedDTO.descripcion(),
                updatedDTO.duracionEstimadaMinutos(),
                updatedDTO.costoBase(),
                updatedDTO.activo()
        );
        verify(treatmentRepositoryPort).save(updatedTreatment);
    }

    @Test
    void updateTreatment_WhenTreatmentNotFound_ShouldThrowDomainException() {
        // Arrange
        when(treatmentRepositoryPort.findById(treatmentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> treatmentService.updateTreatment(treatmentId, validTreatmentDTO))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Tratamiento no encontrado");
        verify(treatmentRepositoryPort, never()).save(any());
    }

    @Test
    void updateTreatment_WhenTreatmentInActivePlan_ShouldThrowDomainException() {
        // Arrange
        when(treatmentRepositoryPort.findById(treatmentId)).thenReturn(Optional.of(mockTreatment));
        when(treatmentRepositoryPort.isTreatmentInActivePlan(treatmentId)).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> treatmentService.updateTreatment(treatmentId, validTreatmentDTO))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("No se puede modificar/desactivar un tratamiento que está en un plan activo");
        verify(treatmentRepositoryPort, never()).save(any());
    }

    @Test
    void deactivateTreatment_WhenValid_ShouldDeactivateAndSave() {
        // Arrange
        when(treatmentRepositoryPort.findById(treatmentId)).thenReturn(Optional.of(mockTreatment));
        when(treatmentRepositoryPort.isTreatmentInActivePlan(treatmentId)).thenReturn(false);

        // Act
        treatmentService.deactivateTreatment(treatmentId);

        // Assert
        verify(mockTreatment).deactivate();
        verify(treatmentRepositoryPort).save(mockTreatment);
    }

    @Test
    void deactivateTreatment_WhenTreatmentInActivePlan_ShouldThrowDomainException() {
        // Arrange
        when(treatmentRepositoryPort.findById(treatmentId)).thenReturn(Optional.of(mockTreatment));
        when(treatmentRepositoryPort.isTreatmentInActivePlan(treatmentId)).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> treatmentService.deactivateTreatment(treatmentId))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("No se puede modificar/desactivar un tratamiento que está en un plan activo");
        verify(mockTreatment, never()).deactivate();
        verify(treatmentRepositoryPort, never()).save(any());
    }

    @Test
    void activateTreatment_WhenValid_ShouldActivateAndSave() {
        // Arrange
        when(treatmentRepositoryPort.findById(treatmentId)).thenReturn(Optional.of(mockTreatment));

        // Act
        treatmentService.activateTreatment(treatmentId);

        // Assert
        verify(mockTreatment).activate();
        verify(treatmentRepositoryPort).save(mockTreatment);
    }

    @Test
    void activateTreatment_WhenTreatmentNotFound_ShouldThrowDomainException() {
        // Arrange
        when(treatmentRepositoryPort.findById(treatmentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> treatmentService.activateTreatment(treatmentId))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Tratamiento no encontrado");
        verify(treatmentRepositoryPort, never()).save(any());
    }
}