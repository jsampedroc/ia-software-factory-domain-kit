package com.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import java.time.LocalDate;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class StudentResponseDTOTest {

    @Test
    @DisplayName("Given all fields, when building StudentResponseDTO, then all fields are set correctly")
    void testBuilder() {
        UUID id = UUID.randomUUID();
        UUID legalGuardianId = UUID.randomUUID();
        UUID currentClassroomId = UUID.randomUUID();
        LocalDate dateOfBirth = LocalDate.of(2015, 5, 10);
        LocalDate enrollmentDate = LocalDate.of(2023, 3, 1);

        StudentResponseDTO dto = StudentResponseDTO.builder()
                .id(id)
                .legalGuardianId(legalGuardianId)
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(dateOfBirth)
                .identificationNumber("ID123456")
                .enrollmentDate(enrollmentDate)
                .active(true)
                .currentClassroomId(currentClassroomId)
                .build();

        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getLegalGuardianId()).isEqualTo(legalGuardianId);
        assertThat(dto.getFirstName()).isEqualTo("John");
        assertThat(dto.getLastName()).isEqualTo("Doe");
        assertThat(dto.getDateOfBirth()).isEqualTo(dateOfBirth);
        assertThat(dto.getIdentificationNumber()).isEqualTo("ID123456");
        assertThat(dto.getEnrollmentDate()).isEqualTo(enrollmentDate);
        assertThat(dto.isActive()).isTrue();
        assertThat(dto.getCurrentClassroomId()).isEqualTo(currentClassroomId);
    }

    @Test
    @DisplayName("Given two StudentResponseDTOs with same field values, when compared, they are equal")
    void testEqualsAndHashCode() {
        UUID id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID legalGuardianId = UUID.randomUUID();
        UUID currentClassroomId = UUID.randomUUID();
        LocalDate dateOfBirth = LocalDate.of(2015, 5, 10);
        LocalDate enrollmentDate = LocalDate.of(2023, 3, 1);

        StudentResponseDTO dto1 = StudentResponseDTO.builder()
                .id(id)
                .legalGuardianId(legalGuardianId)
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(dateOfBirth)
                .identificationNumber("ID123456")
                .enrollmentDate(enrollmentDate)
                .active(true)
                .currentClassroomId(currentClassroomId)
                .build();

        StudentResponseDTO dto2 = StudentResponseDTO.builder()
                .id(id)
                .legalGuardianId(legalGuardianId)
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(dateOfBirth)
                .identificationNumber("ID123456")
                .enrollmentDate(enrollmentDate)
                .active(true)
                .currentClassroomId(currentClassroomId)
                .build();

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    @DisplayName("Given a StudentResponseDTO, when using toString, it contains all field values")
    void testToString() {
        UUID id = UUID.randomUUID();
        LocalDate dateOfBirth = LocalDate.of(2015, 5, 10);
        LocalDate enrollmentDate = LocalDate.of(2023, 3, 1);

        StudentResponseDTO dto = StudentResponseDTO.builder()
                .id(id)
                .legalGuardianId(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(dateOfBirth)
                .identificationNumber("ID123456")
                .enrollmentDate(enrollmentDate)
                .active(true)
                .currentClassroomId(UUID.randomUUID())
                .build();

        String toStringResult = dto.toString();
        assertThat(toStringResult).contains("John");
        assertThat(toStringResult).contains("Doe");
        assertThat(toStringResult).contains("ID123456");
        assertThat(toStringResult).contains("true");
    }

    @Nested
    @DisplayName("Lombok Generated Methods Tests")
    class LombokMethodsTest {

        @Test
        @DisplayName("Given a StudentResponseDTO, when using no-args constructor, then object is created")
        void testNoArgsConstructor() {
            StudentResponseDTO dto = new StudentResponseDTO();
            assertThat(dto).isNotNull();
        }

        @Test
        @DisplayName("Given a StudentResponseDTO, when using all-args constructor, then all fields are set")
        void testAllArgsConstructor() {
            UUID id = UUID.randomUUID();
            UUID legalGuardianId = UUID.randomUUID();
            UUID currentClassroomId = UUID.randomUUID();
            LocalDate dateOfBirth = LocalDate.of(2015, 5, 10);
            LocalDate enrollmentDate = LocalDate.of(2023, 3, 1);

            StudentResponseDTO dto = new StudentResponseDTO(
                    id,
                    legalGuardianId,
                    "John",
                    "Doe",
                    dateOfBirth,
                    "ID123456",
                    enrollmentDate,
                    true,
                    currentClassroomId
            );

            assertThat(dto.getId()).isEqualTo(id);
            assertThat(dto.getFirstName()).isEqualTo("John");
            assertThat(dto.getLastName()).isEqualTo("Doe");
            assertThat(dto.getIdentificationNumber()).isEqualTo("ID123456");
            assertThat(dto.isActive()).isTrue();
        }

        @Test
        @DisplayName("Given a StudentResponseDTO, when using setters, then fields are updated")
        void testSetters() {
            StudentResponseDTO dto = new StudentResponseDTO();
            UUID newId = UUID.randomUUID();
            LocalDate newDate = LocalDate.now();

            dto.setId(newId);
            dto.setFirstName("Jane");
            dto.setLastName("Smith");
            dto.setDateOfBirth(newDate);
            dto.setActive(false);

            assertThat(dto.getId()).isEqualTo(newId);
            assertThat(dto.getFirstName()).isEqualTo("Jane");
            assertThat(dto.getLastName()).isEqualTo("Smith");
            assertThat(dto.getDateOfBirth()).isEqualTo(newDate);
            assertThat(dto.isActive()).isFalse();
        }
    }
}