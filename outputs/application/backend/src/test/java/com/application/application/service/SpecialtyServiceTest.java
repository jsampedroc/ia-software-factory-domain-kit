package com.application.application.service;

import com.application.application.dto.SpecialtyDTO;
import com.application.application.mapper.SpecialtyMapper;
import com.application.domain.exception.DomainException;
import com.application.domain.model.Specialty;
import com.application.domain.valueobject.SpecialtyId;
import com.application.domain.port.SpecialtyRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialtyServiceTest {

    @Mock
    private SpecialtyRepositoryPort specialtyRepositoryPort;

    @Mock
    private SpecialtyMapper specialtyMapper;

    @InjectMocks
    private SpecialtyService specialtyService;

    private SpecialtyId testId;
    private Specialty testSpecialty;
    private SpecialtyDTO testSpecialtyDTO;

    @BeforeEach
    void setUp() {
        testId = new SpecialtyId(UUID.randomUUID());
        testSpecialty = new Specialty(testId, "ORT-01", "Ortodoncia", "Especialidad en ortodoncia y brackets");
        testSpecialtyDTO = new SpecialtyDTO(testId.value(), "ORT-01", "Ortodoncia", "Especialidad en ortodoncia y brackets");
    }

    @Test
    void create_ShouldReturnSavedSpecialtyDTO_WhenValidInput() {
        when(specialtyMapper.toDomain(testSpecialtyDTO)).thenReturn(testSpecialty);
        when(specialtyRepositoryPort.existsByCodigo("ORT-01")).thenReturn(false);
        when(specialtyRepositoryPort.save(testSpecialty)).thenReturn(testSpecialty);
        when(specialtyMapper.toDTO(testSpecialty)).thenReturn(testSpecialtyDTO);

        SpecialtyDTO result = specialtyService.create(testSpecialtyDTO);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(testId.value());
        assertThat(result.codigo()).isEqualTo("ORT-01");
        verify(specialtyRepositoryPort).save(testSpecialty);
        verify(specialtyRepositoryPort).existsByCodigo("ORT-01");
    }

    @Test
    void create_ShouldThrowDomainException_WhenSpecialtyDTOIsNull() {
        assertThatThrownBy(() -> specialtyService.create(null))
                .isInstanceOf(DomainException.class)
                .hasMessage("SpecialtyDTO cannot be null");
    }

    @Test
    void create_ShouldThrowDomainException_WhenCodeAlreadyExists() {
        when(specialtyMapper.toDomain(testSpecialtyDTO)).thenReturn(testSpecialty);
        when(specialtyRepositoryPort.existsByCodigo("ORT-01")).thenReturn(true);

        assertThatThrownBy(() -> specialtyService.create(testSpecialtyDTO))
                .isInstanceOf(DomainException.class)
                .hasMessage("Specialty with code 'ORT-01' already exists");
    }

    @Test
    void create_ShouldThrowDomainException_WhenCodeIsEmpty() {
        SpecialtyDTO invalidDTO = new SpecialtyDTO(UUID.randomUUID(), " ", "Nombre", "Desc");
        Specialty invalidSpecialty = new Specialty(testId, " ", "Nombre", "Desc");
        when(specialtyMapper.toDomain(invalidDTO)).thenReturn(invalidSpecialty);

        assertThatThrownBy(() -> specialtyService.create(invalidDTO))
                .isInstanceOf(DomainException.class)
                .hasMessage("Specialty code is required");
    }

    @Test
    void update_ShouldReturnUpdatedSpecialtyDTO_WhenValidInput() {
        SpecialtyDTO updatedDTO = new SpecialtyDTO(testId.value(), "ORT-02", "Ortodoncia Avanzada", "Desc actualizada");
        Specialty updatedSpecialty = new Specialty(testId, "ORT-02", "Ortodoncia Avanzada", "Desc actualizada");

        when(specialtyRepositoryPort.findById(testId)).thenReturn(Optional.of(testSpecialty));
        when(specialtyMapper.toDomain(updatedDTO)).thenReturn(updatedSpecialty);
        when(specialtyRepositoryPort.existsByCodigo("ORT-02")).thenReturn(false);
        when(specialtyRepositoryPort.save(any(Specialty.class))).thenReturn(updatedSpecialty);
        when(specialtyMapper.toDTO(updatedSpecialty)).thenReturn(updatedDTO);

        SpecialtyDTO result = specialtyService.update(testId, updatedDTO);

        assertThat(result).isNotNull();
        assertThat(result.codigo()).isEqualTo("ORT-02");
        assertThat(result.nombre()).isEqualTo("Ortodoncia Avanzada");
        verify(specialtyRepositoryPort).save(any(Specialty.class));
    }

    @Test
    void update_ShouldThrowDomainException_WhenIdOrDTOIsNull() {
        assertThatThrownBy(() -> specialtyService.update(null, testSpecialtyDTO))
                .isInstanceOf(DomainException.class)
                .hasMessage("ID and SpecialtyDTO cannot be null");

        assertThatThrownBy(() -> specialtyService.update(testId, null))
                .isInstanceOf(DomainException.class)
                .hasMessage("ID and SpecialtyDTO cannot be null");
    }

    @Test
    void update_ShouldThrowDomainException_WhenSpecialtyNotFound() {
        when(specialtyRepositoryPort.findById(testId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> specialtyService.update(testId, testSpecialtyDTO))
                .isInstanceOf(DomainException.class)
                .hasMessage("Specialty not found with id: " + testId.value());
    }

    @Test
    void delete_ShouldCallRepositoryDelete_WhenSpecialtyExists() {
        when(specialtyRepositoryPort.existsById(testId)).thenReturn(true);
        doNothing().when(specialtyRepositoryPort).deleteById(testId);

        specialtyService.delete(testId);

        verify(specialtyRepositoryPort).deleteById(testId);
    }

    @Test
    void delete_ShouldThrowDomainException_WhenIdIsNull() {
        assertThatThrownBy(() -> specialtyService.delete(null))
                .isInstanceOf(DomainException.class)
                .hasMessage("Specialty ID cannot be null");
    }

    @Test
    void delete_ShouldThrowDomainException_WhenSpecialtyNotFound() {
        when(specialtyRepositoryPort.existsById(testId)).thenReturn(false);

        assertThatThrownBy(() -> specialtyService.delete(testId))
                .isInstanceOf(DomainException.class)
                .hasMessage("Specialty not found with id: " + testId.value());
    }

    @Test
    void findById_ShouldReturnSpecialtyDTO_WhenExists() {
        when(specialtyRepositoryPort.findById(testId)).thenReturn(Optional.of(testSpecialty));
        when(specialtyMapper.toDTO(testSpecialty)).thenReturn(testSpecialtyDTO);

        Optional<SpecialtyDTO> result = specialtyService.findById(testId);

        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(testId.value());
    }

    @Test
    void findById_ShouldReturnEmptyOptional_WhenNotExists() {
        when(specialtyRepositoryPort.findById(testId)).thenReturn(Optional.empty());

        Optional<SpecialtyDTO> result = specialtyService.findById(testId);

        assertThat(result).isEmpty();
    }

    @Test
    void findById_ShouldThrowDomainException_WhenIdIsNull() {
        assertThatThrownBy(() -> specialtyService.findById(null))
                .isInstanceOf(DomainException.class)
                .hasMessage("Specialty ID cannot be null");
    }

    @Test
    void findAll_ShouldReturnListOfSpecialtyDTO() {
        List<Specialty> specialties = List.of(testSpecialty);
        when(specialtyRepositoryPort.findAll()).thenReturn(specialties);
        when(specialtyMapper.toDTO(testSpecialty)).thenReturn(testSpecialtyDTO);

        List<SpecialtyDTO> result = specialtyService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).codigo()).isEqualTo("ORT-01");
    }

    @Test
    void findByCodigo_ShouldReturnListOfSpecialtyDTO() {
        List<Specialty> specialties = List.of(testSpecialty);
        when(specialtyRepositoryPort.findByCodigo("ORT-01")).thenReturn(specialties);
        when(specialtyMapper.toDTO(testSpecialty)).thenReturn(testSpecialtyDTO);

        List<SpecialtyDTO> result = specialtyService.findByCodigo("ORT-01");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).codigo()).isEqualTo("ORT-01");
    }

    @Test
    void findByCodigo_ShouldThrowDomainException_WhenCodeIsNullOrEmpty() {
        assertThatThrownBy(() -> specialtyService.findByCodigo(null))
                .isInstanceOf(DomainException.class)
                .hasMessage("Specialty code cannot be null or empty");

        assertThatThrownBy(() -> specialtyService.findByCodigo(" "))
                .isInstanceOf(DomainException.class)
                .hasMessage("Specialty code cannot be null or empty");
    }

    @Test
    void findByNombreContaining_ShouldReturnListOfSpecialtyDTO() {
        List<Specialty> specialties = List.of(testSpecialty);
        when(specialtyRepositoryPort.findByNombreContaining("Ortodon")).thenReturn(specialties);
        when(specialtyMapper.toDTO(testSpecialty)).thenReturn(testSpecialtyDTO);

        List<SpecialtyDTO> result = specialtyService.findByNombreContaining("Ortodon");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).nombre()).isEqualTo("Ortodoncia");
    }

    @Test
    void findByNombreContaining_ShouldThrowDomainException_WhenNameIsNullOrEmpty() {
        assertThatThrownBy(() -> specialtyService.findByNombreContaining(null))
                .isInstanceOf(DomainException.class)
                .hasMessage("Specialty name cannot be null or empty");

        assertThatThrownBy(() -> specialtyService.findByNombreContaining(" "))
                .isInstanceOf(DomainException.class)
                .hasMessage("Specialty name cannot be null or empty");
    }

    @Test
    void existsByCodigo_ShouldReturnTrue_WhenCodeExists() {
        when(specialtyRepositoryPort.existsByCodigo("ORT-01")).thenReturn(true);

        boolean result = specialtyService.existsByCodigo("ORT-01");

        assertThat(result).isTrue();
    }

    @Test
    void existsByCodigo_ShouldThrowDomainException_WhenCodeIsNullOrEmpty() {
        assertThatThrownBy(() -> specialtyService.existsByCodigo(null))
                .isInstanceOf(DomainException.class)
                .hasMessage("Specialty code cannot be null or empty");
    }

    @Test
    void existsByNombre_ShouldReturnTrue_WhenNameExists() {
        when(specialtyRepositoryPort.existsByNombre("Ortodoncia")).thenReturn(true);

        boolean result = specialtyService.existsByNombre("Ortodoncia");

        assertThat(result).isTrue();
    }

    @Test
    void existsByNombre_ShouldThrowDomainException_WhenNameIsNullOrEmpty() {
        assertThatThrownBy(() -> specialtyService.existsByNombre(null))
                .isInstanceOf(DomainException.class)
                .hasMessage("Specialty name cannot be null or empty");
    }

    @Test
    void validateSpecialty_ShouldThrowDomainException_WhenCodeExceedsLength() {
        Specialty invalidSpecialty = new Specialty(testId, "A".repeat(21), "Nombre", "Desc");
        when(specialtyRepositoryPort.existsByCodigo(anyString())).thenReturn(false);

        assertThatThrownBy(() -> specialtyService.create(testSpecialtyDTO))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void validateSpecialty_ShouldThrowDomainException_WhenNameExceedsLength() {
        Specialty invalidSpecialty = new Specialty(testId, "COD", "N".repeat(101), "Desc");
        when(specialtyMapper.toDomain(testSpecialtyDTO)).thenReturn(invalidSpecialty);
        when(specialtyRepositoryPort.existsByCodigo(anyString())).thenReturn(false);

        assertThatThrownBy(() -> specialtyService.create(testSpecialtyDTO))
                .isInstanceOf(DomainException.class)
                .hasMessage("Specialty name cannot exceed 100 characters");
    }

    @Test
    void validateSpecialty_ShouldThrowDomainException_WhenDescriptionExceedsLength() {
        Specialty invalidSpecialty = new Specialty(testId, "COD", "Nombre", "D".repeat(501));
        when(specialtyMapper.toDomain(testSpecialtyDTO)).thenReturn(invalidSpecialty);
        when(specialtyRepositoryPort.existsByCodigo(anyString())).thenReturn(false);

        assertThatThrownBy(() -> specialtyService.create(testSpecialtyDTO))
                .isInstanceOf(DomainException.class)
                .hasMessage("Specialty description cannot exceed 500 characters");
    }
}