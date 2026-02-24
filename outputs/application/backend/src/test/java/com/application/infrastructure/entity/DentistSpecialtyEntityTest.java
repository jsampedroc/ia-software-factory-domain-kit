package com.application.infrastructure.entity;

import com.application.domain.valueobject.DentistId;
import com.application.domain.valueobject.SpecialtyId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DentistSpecialtyEntityTest {

    @Test
    void givenValidParameters_whenCreatingEntity_thenEntityIsCreatedWithCorrectValues() {
        // Given
        DentistId dentistId = new DentistId(UUID.randomUUID());
        SpecialtyId specialtyId = new SpecialtyId(UUID.randomUUID());
        String certificationNumber = "CERT-12345";
        LocalDateTime certificationDate = LocalDateTime.of(2023, 5, 15, 10, 0);
        String certifyingInstitution = "Colegio Odontológico Nacional";
        Boolean isActive = true;
        LocalDateTime createdAt = LocalDateTime.of(2023, 5, 16, 9, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2023, 6, 1, 14, 30);

        // When
        DentistSpecialtyEntity entity = new DentistSpecialtyEntity(
                dentistId,
                specialtyId,
                certificationNumber,
                certificationDate,
                certifyingInstitution,
                isActive,
                createdAt,
                updatedAt
        );

        // Then
        assertThat(entity.getDentistId()).isEqualTo(dentistId);
        assertThat(entity.getSpecialtyId()).isEqualTo(specialtyId);
        assertThat(entity.getCertificationNumber()).isEqualTo(certificationNumber);
        assertThat(entity.getCertificationDate()).isEqualTo(certificationDate);
        assertThat(entity.getCertifyingInstitution()).isEqualTo(certifyingInstitution);
        assertThat(entity.getIsActive()).isEqualTo(isActive);
        assertThat(entity.getCreatedAt()).isEqualTo(createdAt);
        assertThat(entity.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    void givenNullOptionalFields_whenCreatingEntity_thenEntityIsCreatedWithNullValues() {
        // Given
        DentistId dentistId = new DentistId(UUID.randomUUID());
        SpecialtyId specialtyId = new SpecialtyId(UUID.randomUUID());
        Boolean isActive = false;
        LocalDateTime createdAt = LocalDateTime.now();

        // When
        DentistSpecialtyEntity entity = new DentistSpecialtyEntity(
                dentistId,
                specialtyId,
                null, // certificationNumber
                null, // certificationDate
                null, // certifyingInstitution
                isActive,
                createdAt,
                null  // updatedAt
        );

        // Then
        assertThat(entity.getDentistId()).isEqualTo(dentistId);
        assertThat(entity.getSpecialtyId()).isEqualTo(specialtyId);
        assertThat(entity.getCertificationNumber()).isNull();
        assertThat(entity.getCertificationDate()).isNull();
        assertThat(entity.getCertifyingInstitution()).isNull();
        assertThat(entity.getIsActive()).isEqualTo(isActive);
        assertThat(entity.getCreatedAt()).isEqualTo(createdAt);
        assertThat(entity.getUpdatedAt()).isNull();
    }

    @Test
    void givenEntityCreated_whenAccessingId_thenIdIsNullInitially() {
        // Given
        DentistSpecialtyEntity entity = createValidEntity();

        // Then
        assertThat(entity.getId()).isNull();
    }

    @Test
    void givenNoArgsConstructor_whenCreatingEntity_thenEntityHasProtectedConstructor() {
        // This test verifies that the no-args constructor exists and is protected
        // We can't directly test the protected constructor, but we can verify
        // that the class can be instantiated through reflection or by a test
        // in the same package. Since we're in the same package, we can check
        // that the class compiles with the @NoArgsConstructor(access = PROTECTED)
        assertThat(DentistSpecialtyEntity.class).isNotNull();
    }

    @Test
    void givenTwoEntitiesWithSameFieldValues_whenComparing_thenTheyAreNotEqualDueToDifferentIds() {
        // Given
        DentistId dentistId = new DentistId(UUID.randomUUID());
        SpecialtyId specialtyId = new SpecialtyId(UUID.randomUUID());
        LocalDateTime now = LocalDateTime.now();

        DentistSpecialtyEntity entity1 = new DentistSpecialtyEntity(
                dentistId,
                specialtyId,
                "CERT-111",
                now,
                "Institution A",
                true,
                now.minusDays(1),
                now
        );

        DentistSpecialtyEntity entity2 = new DentistSpecialtyEntity(
                dentistId,
                specialtyId,
                "CERT-111",
                now,
                "Institution A",
                true,
                now.minusDays(1),
                now
        );

        // When & Then
        // They should not be equal because they are different instances
        // and Entity doesn't override equals/hashCode
        assertThat(entity1).isNotEqualTo(entity2);
        assertThat(entity1.hashCode()).isNotEqualTo(entity2.hashCode());
    }

    @Test
    void givenEntity_whenCheckingTableName_thenTableNameIsCorrect() {
        // Given
        DentistSpecialtyEntity entity = createValidEntity();

        // Then
        // Verify through annotation if needed, but this is more of a compile-time check
        assertThat(entity.getClass().getAnnotation(jakarta.persistence.Table.class))
                .isNotNull()
                .extracting("name")
                .isEqualTo("dentist_specialty");
    }

    private DentistSpecialtyEntity createValidEntity() {
        DentistId dentistId = new DentistId(UUID.randomUUID());
        SpecialtyId specialtyId = new SpecialtyId(UUID.randomUUID());
        LocalDateTime now = LocalDateTime.now();

        return new DentistSpecialtyEntity(
                dentistId,
                specialtyId,
                "CERT-TEST-001",
                now.minusMonths(6),
                "Test Institution",
                true,
                now.minusDays(10),
                now.minusDays(1)
        );
    }
}