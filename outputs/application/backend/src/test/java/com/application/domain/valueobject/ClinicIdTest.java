package com.application.domain.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ClinicIdTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {
        @Test
        @DisplayName("Should create ClinicId with valid UUID")
        void shouldCreateClinicIdWithValidUUID() {
            UUID uuid = UUID.randomUUID();
            ClinicId clinicId = new ClinicId(uuid);
            assertNotNull(clinicId);
            assertEquals(uuid, clinicId.value());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when UUID is null")
        void shouldThrowIllegalArgumentExceptionWhenUUIDIsNull() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ClinicId(null)
            );
            assertEquals("ClinicId value cannot be null", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Static Factory Method Tests")
    class StaticFactoryMethodTests {
        @Test
        @DisplayName("generate() should create ClinicId with random UUID")
        void generateShouldCreateClinicIdWithRandomUUID() {
            ClinicId clinicId = ClinicId.generate();
            assertNotNull(clinicId);
            assertNotNull(clinicId.value());
        }

        @Test
        @DisplayName("fromString() should create ClinicId from valid UUID string")
        void fromStringShouldCreateClinicIdFromValidUUIDString() {
            String uuidString = "123e4567-e89b-12d3-a456-426614174000";
            ClinicId clinicId = ClinicId.fromString(uuidString);
            assertNotNull(clinicId);
            assertEquals(UUID.fromString(uuidString), clinicId.value());
        }

        @Test
        @DisplayName("fromString() should throw IllegalArgumentException for invalid UUID string")
        void fromStringShouldThrowIllegalArgumentExceptionForInvalidUUIDString() {
            String invalidUuidString = "invalid-uuid";
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ClinicId.fromString(invalidUuidString)
            );
            assertTrue(exception.getMessage().contains("Invalid UUID format for ClinicId"));
            assertNotNull(exception.getCause());
        }

        @Test
        @DisplayName("fromString() should throw IllegalArgumentException for null string")
        void fromStringShouldThrowIllegalArgumentExceptionForNullString() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ClinicId.fromString(null)
            );
            assertTrue(exception.getMessage().contains("Invalid UUID format for ClinicId"));
        }
    }

    @Nested
    @DisplayName("Value Object Contract Tests")
    class ValueObjectContractTests {
        @Test
        @DisplayName("Should implement ValueObject interface")
        void shouldImplementValueObjectInterface() {
            ClinicId clinicId = new ClinicId(UUID.randomUUID());
            assertTrue(clinicId instanceof ValueObject);
        }

        @Test
        @DisplayName("Equals and hashCode should be based on UUID value")
        void equalsAndHashCodeShouldBeBasedOnUUIDValue() {
            UUID uuid = UUID.randomUUID();
            ClinicId clinicId1 = new ClinicId(uuid);
            ClinicId clinicId2 = new ClinicId(uuid);
            ClinicId clinicId3 = new ClinicId(UUID.randomUUID());

            assertEquals(clinicId1, clinicId2);
            assertNotEquals(clinicId1, clinicId3);
            assertEquals(clinicId1.hashCode(), clinicId2.hashCode());
            assertNotEquals(clinicId1.hashCode(), clinicId3.hashCode());
        }

        @Test
        @DisplayName("toString() should return meaningful representation")
        void toStringShouldReturnMeaningfulRepresentation() {
            UUID uuid = UUID.randomUUID();
            ClinicId clinicId = new ClinicId(uuid);
            String stringRepresentation = clinicId.toString();
            assertNotNull(stringRepresentation);
            assertTrue(stringRepresentation.contains(uuid.toString()));
        }
    }
}