package com.application.domain.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TreatmentPlanIdTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {
        @Test
        @DisplayName("Should create TreatmentPlanId with valid UUID")
        void shouldCreateTreatmentPlanIdWithValidUUID() {
            UUID uuid = UUID.randomUUID();
            TreatmentPlanId treatmentPlanId = new TreatmentPlanId(uuid);
            assertNotNull(treatmentPlanId);
            assertEquals(uuid, treatmentPlanId.value());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is null")
        void shouldThrowIllegalArgumentExceptionWhenValueIsNull() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new TreatmentPlanId(null)
            );
            assertEquals("TreatmentPlanId value cannot be null", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Static Factory Method Tests")
    class StaticFactoryMethodTests {
        @Test
        @DisplayName("generate() should create a TreatmentPlanId with a non-null UUID")
        void generateShouldCreateTreatmentPlanIdWithNonNullUUID() {
            TreatmentPlanId treatmentPlanId = TreatmentPlanId.generate();
            assertNotNull(treatmentPlanId);
            assertNotNull(treatmentPlanId.value());
        }

        @Test
        @DisplayName("generate() should create unique IDs")
        void generateShouldCreateUniqueIds() {
            TreatmentPlanId id1 = TreatmentPlanId.generate();
            TreatmentPlanId id2 = TreatmentPlanId.generate();
            assertNotEquals(id1, id2);
            assertNotEquals(id1.value(), id2.value());
        }

        @Test
        @DisplayName("fromString() should create TreatmentPlanId from valid UUID string")
        void fromStringShouldCreateTreatmentPlanIdFromValidUUIDString() {
            String uuidString = "123e4567-e89b-12d3-a456-426614174000";
            TreatmentPlanId treatmentPlanId = TreatmentPlanId.fromString(uuidString);
            assertNotNull(treatmentPlanId);
            assertEquals(UUID.fromString(uuidString), treatmentPlanId.value());
        }

        @Test
        @DisplayName("fromString() should throw IllegalArgumentException for invalid UUID string")
        void fromStringShouldThrowIllegalArgumentExceptionForInvalidUUIDString() {
            String invalidUuidString = "invalid-uuid";
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> TreatmentPlanId.fromString(invalidUuidString)
            );
            assertTrue(exception.getMessage().contains("Invalid TreatmentPlanId format: " + invalidUuidString));
        }

        @Test
        @DisplayName("fromString() should throw IllegalArgumentException for null string")
        void fromStringShouldThrowIllegalArgumentExceptionForNullString() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> TreatmentPlanId.fromString(null)
            );
            assertTrue(exception.getMessage().contains("Invalid TreatmentPlanId format: null"));
        }
    }

    @Nested
    @DisplayName("Value Object Contract Tests")
    class ValueObjectContractTests {
        @Test
        @DisplayName("Should implement ValueObject interface")
        void shouldImplementValueObjectInterface() {
            TreatmentPlanId treatmentPlanId = TreatmentPlanId.generate();
            assertTrue(treatmentPlanId instanceof ValueObject);
        }

        @Test
        @DisplayName("Equals should return true for same value")
        void equalsShouldReturnTrueForSameValue() {
            UUID uuid = UUID.randomUUID();
            TreatmentPlanId id1 = new TreatmentPlanId(uuid);
            TreatmentPlanId id2 = new TreatmentPlanId(uuid);
            assertEquals(id1, id2);
            assertEquals(id1.hashCode(), id2.hashCode());
        }

        @Test
        @DisplayName("Equals should return false for different value")
        void equalsShouldReturnFalseForDifferentValue() {
            TreatmentPlanId id1 = TreatmentPlanId.generate();
            TreatmentPlanId id2 = TreatmentPlanId.generate();
            assertNotEquals(id1, id2);
        }

        @Test
        @DisplayName("toString should return string representation")
        void toStringShouldReturnStringRepresentation() {
            UUID uuid = UUID.randomUUID();
            TreatmentPlanId treatmentPlanId = new TreatmentPlanId(uuid);
            String stringRepresentation = treatmentPlanId.toString();
            assertTrue(stringRepresentation.contains(uuid.toString()));
            assertTrue(stringRepresentation.contains("TreatmentPlanId"));
        }
    }
}