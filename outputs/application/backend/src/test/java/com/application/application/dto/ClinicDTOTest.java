package com.application.application.dto;

import com.application.domain.model.Clinic;
import com.application.domain.valueobject.ClinicId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ClinicDTOTest {

    @Test
    void givenClinicDomain_whenFromDomain_thenReturnCorrectDTO() {
        // Given
        UUID expectedId = UUID.randomUUID();
        ClinicId clinicId = new ClinicId(expectedId);
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

        // When
        ClinicDTO dto = ClinicDTO.fromDomain(clinic);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(expectedId);
        assertThat(dto.codigo()).isEqualTo("CLI-001");
        assertThat(dto.nombre()).isEqualTo("Clínica Central");
        assertThat(dto.direccion()).isEqualTo("Calle Principal 123");
        assertThat(dto.telefono()).isEqualTo("555-1234");
        assertThat(dto.email()).isEqualTo("central@clinica.com");
        assertThat(dto.horarioApertura()).isEqualTo(LocalTime.of(8, 0));
        assertThat(dto.horarioCierre()).isEqualTo(LocalTime.of(18, 0));
        assertThat(dto.activa()).isTrue();
    }

    @Test
    void givenDTO_whenToDomain_thenReturnCorrectDomainEntity() {
        // Given
        UUID expectedId = UUID.randomUUID();
        ClinicDTO dto = new ClinicDTO(
                expectedId,
                "CLI-002",
                "Clínica Norte",
                "Avenida Norte 456",
                "555-5678",
                "norte@clinica.com",
                LocalTime.of(9, 0),
                LocalTime.of(19, 0),
                false
        );

        // When
        Clinic clinic = dto.toDomain();

        // Then
        assertThat(clinic).isNotNull();
        assertThat(clinic.getId()).isInstanceOf(ClinicId.class);
        assertThat(clinic.getId().value()).isEqualTo(expectedId);
        assertThat(clinic.getCodigo()).isEqualTo("CLI-002");
        assertThat(clinic.getNombre()).isEqualTo("Clínica Norte");
        assertThat(clinic.getDireccion()).isEqualTo("Avenida Norte 456");
        assertThat(clinic.getTelefono()).isEqualTo("555-5678");
        assertThat(clinic.getEmail()).isEqualTo("norte@clinica.com");
        assertThat(clinic.getHorarioApertura()).isEqualTo(LocalTime.of(9, 0));
        assertThat(clinic.getHorarioCierre()).isEqualTo(LocalTime.of(19, 0));
        assertThat(clinic.getActiva()).isFalse();
    }

    @Test
    void givenDTOWithNullId_whenToDomain_thenCreateDomainWithNewId() {
        // Given
        ClinicDTO dto = new ClinicDTO(
                null,
                "CLI-003",
                "Clínica Sur",
                "Calle Sur 789",
                "555-9012",
                "sur@clinica.com",
                LocalTime.of(7, 30),
                LocalTime.of(17, 30),
                true
        );

        // When
        Clinic clinic = dto.toDomain();

        // Then
        assertThat(clinic).isNotNull();
        assertThat(clinic.getId()).isNotNull();
        assertThat(clinic.getId()).isInstanceOf(ClinicId.class);
        assertThat(clinic.getCodigo()).isEqualTo("CLI-003");
        assertThat(clinic.getNombre()).isEqualTo("Clínica Sur");
        assertThat(clinic.getActiva()).isTrue();
    }

    @Test
    void givenTwoDTOsWithSameValues_whenEquals_thenTheyAreEqual() {
        // Given
        UUID id = UUID.randomUUID();
        ClinicDTO dto1 = new ClinicDTO(
                id,
                "CLI-001",
                "Clínica",
                "Dirección",
                "555-0000",
                "test@clinica.com",
                LocalTime.of(8, 0),
                LocalTime.of(18, 0),
                true
        );

        ClinicDTO dto2 = new ClinicDTO(
                id,
                "CLI-001",
                "Clínica",
                "Dirección",
                "555-0000",
                "test@clinica.com",
                LocalTime.of(8, 0),
                LocalTime.of(18, 0),
                true
        );

        // Then
        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    void givenTwoDTOsWithDifferentIds_whenEquals_thenTheyAreNotEqual() {
        // Given
        ClinicDTO dto1 = new ClinicDTO(
                UUID.randomUUID(),
                "CLI-001",
                "Clínica",
                "Dirección",
                "555-0000",
                "test@clinica.com",
                LocalTime.of(8, 0),
                LocalTime.of(18, 0),
                true
        );

        ClinicDTO dto2 = new ClinicDTO(
                UUID.randomUUID(),
                "CLI-001",
                "Clínica",
                "Dirección",
                "555-0000",
                "test@clinica.com",
                LocalTime.of(8, 0),
                LocalTime.of(18, 0),
                true
        );

        // Then
        assertThat(dto1).isNotEqualTo(dto2);
    }
}