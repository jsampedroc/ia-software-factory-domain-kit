package com.application.domain.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MedicalHistoryIdTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {
        @Test
        @DisplayName("Should create MedicalHistoryId with valid UUID")
        void shouldCreateMedicalHistoryIdWithValidUUID() {
            UUID uuid = UUID.randomUUID();
            MedicalHistoryId medicalHistoryId = new MedicalHistoryId(uuid);
            assertNotNull(medicalHistoryId);
            assertEquals(uuid, medicalHistoryId.value());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when UUID is null")
        void shouldThrowIllegalArgumentExceptionWhenUUIDIsNull() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new MedicalHistoryId(null)
            );
            assertEquals("MedicalHistoryId value cannot be null", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Static Factory Method Tests")
    class StaticFactoryMethodTests {
        @Test
        @DisplayName("generate() should create a new MedicalHistoryId with random UUID")
        void generateShouldCreateNewMedicalHistoryIdWithRandomUUID() {
            MedicalHistoryId medicalHistoryId1 = MedicalHistoryId.generate();
            MedicalHistoryId medicalHistoryId2 = MedicalHistoryId.generate();

            assertNotNull(medicalHistoryId1);
            assertNotNull(medicalHistoryId2);
            assertNotNull(medicalHistoryId1.value());
            assertNotNull(medicalHistoryId2.value());
            assertNotEquals(medicalHistoryId1.value(), medicalHistoryId2.value());
        }

        @Test
        @DisplayName("fromString() should create MedicalHistoryId from valid UUID string")
        void fromStringShouldCreateMedicalHistoryIdFromValidUUIDString() {
            String uuidString = "123e4567-e89b-12d3-a456-426614174000";
            MedicalHistoryId medicalHistoryId = MedicalHistoryId.fromString(uuidString);

            assertNotNull(medicalHistoryId);
            assertEquals(UUID.fromString(uuidString), medicalHistoryId.value());
        }

        @Test
        @DisplayName("fromString() should throw IllegalArgumentException for invalid UUID string")
        void fromStringShouldThrowIllegalArgumentExceptionForInvalidUUIDString() {
            String invalidUuidString = "invalid-uuid";

            assertThrows(
                    IllegalArgumentException.class,
                    () -> MedicalHistoryId.fromString(invalidUuidString)
            );
        }
    }

    @Nested
    @DisplayName("Value Object Contract Tests")
    class ValueObjectContractTests {
        @Test
        @DisplayName("Should implement ValueObject interface")
        void shouldImplementValueObjectInterface() {
            MedicalHistoryId medicalHistoryId = MedicalHistoryId.generate();
            assertTrue(medicalHistoryId instanceof ValueObject);
        }

        @Test
        @DisplayName("Equals and hashCode should be based on UUID value")
        void equalsAndHashCodeShouldBeBasedOnUUIDValue() {
            UUID uuid = UUID.randomUUID();
            MedicalHistoryId medicalHistoryId1 = new MedicalHistoryId(uuid);
            MedicalHistoryId medicalHistoryId2 = new MedicalHistoryId(uuid);
            MedicalHistoryId medicalHistoryId3 = MedicalHistoryId.generate();

            assertEquals(medicalHistoryId1, medicalHistoryId2);
            assertNotEquals(medicalHistoryId1, medicalHistoryId3);
            assertEquals(medicalHistoryId1.hashCode(), medicalHistoryId2.hashCode());
            assertNotEquals(medicalHistoryId1.hashCode(), medicalHistoryId3.hashCode());
        }

        @Test
        @DisplayName("toString() should return meaningful representation")
        void toStringShouldReturnMeaningfulRepresentation() {
            UUID uuid = UUID.randomUUID();
            MedicalHistoryId medicalHistoryId = new MedicalHistoryId(uuid);
            String stringRepresentation = medicalHistoryId.toString();

            assertNotNull(stringRepresentation);
            assertTrue(stringRepresentation.contains(uuid.toString()));
        }
    }
}