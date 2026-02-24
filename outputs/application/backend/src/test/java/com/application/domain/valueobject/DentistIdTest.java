package com.application.domain.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DentistIdTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {
        @Test
        @DisplayName("Should create DentistId with valid UUID")
        void shouldCreateDentistIdWithValidUUID() {
            UUID uuid = UUID.randomUUID();
            DentistId dentistId = new DentistId(uuid);
            assertNotNull(dentistId);
            assertEquals(uuid, dentistId.value());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when UUID is null")
        void shouldThrowIllegalArgumentExceptionWhenUUIDIsNull() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DentistId(null)
            );
            assertEquals("DentistId value cannot be null", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Static Factory Method Tests")
    class StaticFactoryMethodTests {
        @Test
        @DisplayName("generate() should create a new DentistId with random UUID")
        void generateShouldCreateNewDentistIdWithRandomUUID() {
            DentistId dentistId1 = DentistId.generate();
            DentistId dentistId2 = DentistId.generate();

            assertNotNull(dentistId1);
            assertNotNull(dentistId2);
            assertNotEquals(dentistId1.value(), dentistId2.value());
        }

        @Test
        @DisplayName("fromString() should create DentistId from valid UUID string")
        void fromStringShouldCreateDentistIdFromValidUUIDString() {
            String uuidString = "123e4567-e89b-12d3-a456-426614174000";
            DentistId dentistId = DentistId.fromString(uuidString);

            assertNotNull(dentistId);
            assertEquals(UUID.fromString(uuidString), dentistId.value());
        }

        @Test
        @DisplayName("fromString() should throw IllegalArgumentException for invalid UUID string")
        void fromStringShouldThrowIllegalArgumentExceptionForInvalidUUIDString() {
            String invalidUuidString = "invalid-uuid";

            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DentistId.fromString(invalidUuidString)
            );
            assertTrue(exception.getMessage().contains("Invalid DentistId format: " + invalidUuidString));
            assertNotNull(exception.getCause());
        }

        @Test
        @DisplayName("fromString() should throw IllegalArgumentException for null string")
        void fromStringShouldThrowIllegalArgumentExceptionForNullString() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> DentistId.fromString(null)
            );
            assertTrue(exception.getMessage().contains("Invalid DentistId format: null"));
        }
    }

    @Nested
    @DisplayName("Value Object Contract Tests")
    class ValueObjectContractTests {
        @Test
        @DisplayName("Should implement ValueObject interface")
        void shouldImplementValueObjectInterface() {
            DentistId dentistId = new DentistId(UUID.randomUUID());
            assertTrue(dentistId instanceof ValueObject);
        }

        @Test
        @DisplayName("Equals and hashCode should be based on UUID value")
        void equalsAndHashCodeShouldBeBasedOnUUIDValue() {
            UUID uuid = UUID.randomUUID();
            DentistId dentistId1 = new DentistId(uuid);
            DentistId dentistId2 = new DentistId(uuid);

            assertEquals(dentistId1, dentistId2);
            assertEquals(dentistId1.hashCode(), dentistId2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal to DentistId with different UUID")
        void shouldNotBeEqualToDentistIdWithDifferentUUID() {
            DentistId dentistId1 = new DentistId(UUID.randomUUID());
            DentistId dentistId2 = new DentistId(UUID.randomUUID());

            assertNotEquals(dentistId1, dentistId2);
        }

        @Test
        @DisplayName("toString() should return meaningful representation")
        void toStringShouldReturnMeaningfulRepresentation() {
            UUID uuid = UUID.randomUUID();
            DentistId dentistId = new DentistId(uuid);
            String stringRepresentation = dentistId.toString();

            assertNotNull(stringRepresentation);
            assertTrue(stringRepresentation.contains(uuid.toString()));
        }
    }
}