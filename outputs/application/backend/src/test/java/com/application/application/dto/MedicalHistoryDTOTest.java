package com.application.application.dto;

import com.application.domain.valueobject.MedicalHistoryId;
import com.application.domain.valueobject.PatientId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class MedicalHistoryDTOTest {

    @Test
    void givenValidParameters_whenCreatingMedicalHistoryDTO_thenAllFieldsAreCorrectlySet() {
        // Given
        MedicalHistoryId historyId = new MedicalHistoryId(UUID.randomUUID());
        PatientId patientId = new PatientId(UUID.randomUUID());
        LocalDateTime fechaCreacion = LocalDateTime.now().minusDays(1);
        LocalDateTime ultimaActualizacion = LocalDateTime.now();
        String alergias = "Penicilina, Ibuprofeno";
        String condicionesMedicas = "Hipertensión controlada";
        String medicamentos = "Losartán 50mg, 1 comprimido diario";
        String observacionesGenerales = "Paciente con buena higiene bucal";

        // When
        MedicalHistoryDTO dto = new MedicalHistoryDTO(
                historyId,
                patientId,
                fechaCreacion,
                ultimaActualizacion,
                alergias,
                condicionesMedicas,
                medicamentos,
                observacionesGenerales
        );

        // Then
        assertThat(dto.historyId()).isEqualTo(historyId);
        assertThat(dto.patientId()).isEqualTo(patientId);
        assertThat(dto.fechaCreacion()).isEqualTo(fechaCreacion);
        assertThat(dto.ultimaActualizacion()).isEqualTo(ultimaActualizacion);
        assertThat(dto.alergias()).isEqualTo(alergias);
        assertThat(dto.condicionesMedicas()).isEqualTo(condicionesMedicas);
        assertThat(dto.medicamentos()).isEqualTo(medicamentos);
        assertThat(dto.observacionesGenerales()).isEqualTo(observacionesGenerales);
    }

    @Test
    void givenTwoDTOsWithSameFieldValues_whenComparing_thenTheyAreEqual() {
        // Given
        UUID sharedHistoryId = UUID.randomUUID();
        UUID sharedPatientId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        MedicalHistoryDTO dto1 = new MedicalHistoryDTO(
                new MedicalHistoryId(sharedHistoryId),
                new PatientId(sharedPatientId),
                now,
                now,
                "Alergia al látex",
                "Diabetes tipo 2",
                "Metformina 850mg",
                "Control periódico requerido"
        );

        MedicalHistoryDTO dto2 = new MedicalHistoryDTO(
                new MedicalHistoryId(sharedHistoryId),
                new PatientId(sharedPatientId),
                now,
                now,
                "Alergia al látex",
                "Diabetes tipo 2",
                "Metformina 850mg",
                "Control periódico requerido"
        );

        // Then
        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    void givenTwoDTOsWithDifferentFieldValues_whenComparing_thenTheyAreNotEqual() {
        // Given
        LocalDateTime now = LocalDateTime.now();

        MedicalHistoryDTO dto1 = new MedicalHistoryDTO(
                new MedicalHistoryId(UUID.randomUUID()),
                new PatientId(UUID.randomUUID()),
                now,
                now,
                "Alergia A",
                "Condición A",
                "Medicamento A",
                "Observación A"
        );

        MedicalHistoryDTO dto2 = new MedicalHistoryDTO(
                new MedicalHistoryId(UUID.randomUUID()),
                new PatientId(UUID.randomUUID()),
                now.plusDays(1),
                now.plusHours(2),
                "Alergia B",
                "Condición B",
                "Medicamento B",
                "Observación B"
        );

        // Then
        assertThat(dto1).isNotEqualTo(dto2);
        assertThat(dto1.hashCode()).isNotEqualTo(dto2.hashCode());
    }

    @Test
    void givenMedicalHistoryDTO_whenCallingToString_thenStringContainsFieldNames() {
        // Given
        MedicalHistoryDTO dto = new MedicalHistoryDTO(
                new MedicalHistoryId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")),
                new PatientId(UUID.fromString("223e4567-e89b-12d3-a456-426614174001")),
                LocalDateTime.of(2024, 1, 15, 10, 30),
                LocalDateTime.of(2024, 1, 20, 14, 45),
                "Ninguna",
                "Ninguna",
                "Ninguno",
                "Paciente sano"
        );

        // When
        String stringRepresentation = dto.toString();

        // Then
        assertThat(stringRepresentation).contains("historyId");
        assertThat(stringRepresentation).contains("patientId");
        assertThat(stringRepresentation).contains("fechaCreacion");
        assertThat(stringRepresentation).contains("ultimaActualizacion");
        assertThat(stringRepresentation).contains("alergias");
        assertThat(stringRepresentation).contains("condicionesMedicas");
        assertThat(stringRepresentation).contains("medicamentos");
        assertThat(stringRepresentation).contains("observacionesGenerales");
    }

    @Test
    void givenDTOWithNullValues_whenCreating_thenDTOIsCreatedSuccessfully() {
        // Given
        MedicalHistoryId historyId = new MedicalHistoryId(UUID.randomUUID());
        PatientId patientId = new PatientId(UUID.randomUUID());
        LocalDateTime fechaCreacion = LocalDateTime.now();
        LocalDateTime ultimaActualizacion = LocalDateTime.now();

        // When
        MedicalHistoryDTO dto = new MedicalHistoryDTO(
                historyId,
                patientId,
                fechaCreacion,
                ultimaActualizacion,
                null,
                null,
                null,
                null
        );

        // Then
        assertThat(dto.historyId()).isEqualTo(historyId);
        assertThat(dto.patientId()).isEqualTo(patientId);
        assertThat(dto.fechaCreacion()).isEqualTo(fechaCreacion);
        assertThat(dto.ultimaActualizacion()).isEqualTo(ultimaActualizacion);
        assertThat(dto.alergias()).isNull();
        assertThat(dto.condicionesMedicas()).isNull();
        assertThat(dto.medicamentos()).isNull();
        assertThat(dto.observacionesGenerales()).isNull();
    }

    @Test
    void givenDTOWithEmptyStrings_whenCreating_thenDTOIsCreatedSuccessfully() {
        // Given
        MedicalHistoryId historyId = new MedicalHistoryId(UUID.randomUUID());
        PatientId patientId = new PatientId(UUID.randomUUID());
        LocalDateTime fechaCreacion = LocalDateTime.now();
        LocalDateTime ultimaActualizacion = LocalDateTime.now();

        // When
        MedicalHistoryDTO dto = new MedicalHistoryDTO(
                historyId,
                patientId,
                fechaCreacion,
                ultimaActualizacion,
                "",
                "",
                "",
                ""
        );

        // Then
        assertThat(dto.alergias()).isEmpty();
        assertThat(dto.condicionesMedicas()).isEmpty();
        assertThat(dto.medicamentos()).isEmpty();
        assertThat(dto.observacionesGenerales()).isEmpty();
    }
}