package com.application.domain.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TreatmentIdTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {
        @Test
        @DisplayName("Should create TreatmentId with valid UUID")
        void shouldCreateTreatmentIdWithValidUUID() {
            // Given
            UUID expectedUuid = UUID.randomUUID();

            // When
            TreatmentId treatmentId = new TreatmentId(expectedUuid);

            // Then
            assertNotNull(treatmentId);
            assertEquals(expectedUuid, treatmentId.value());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when UUID is null")
        void shouldThrowIllegalArgumentExceptionWhenUUIDIsNull() {
            // Given
            UUID nullUuid = null;

            // When & Then
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new TreatmentId(nullUuid)
            );
            assertEquals("TreatmentId value cannot be null", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Static Factory Method Tests")
    class StaticFactoryMethodTests {
        @Test
        @DisplayName("generate() should create TreatmentId with random UUID")
        void generateShouldCreateTreatmentIdWithRandomUUID() {
            // When
            TreatmentId treatmentId = TreatmentId.generate();

            // Then
            assertNotNull(treatmentId);
            assertNotNull(treatmentId.value());
        }

        @Test
        @DisplayName("fromString() should create TreatmentId from valid UUID string")
        void fromStringShouldCreateTreatmentIdFromValidUUIDString() {
            // Given
            String uuidString = "123e4567-e89b-12d3-a456-426614174000";
            UUID expectedUuid = UUID.fromString(uuidString);

            // When
            TreatmentId treatmentId = TreatmentId.fromString(uuidString);

            // Then
            assertNotNull(treatmentId);
            assertEquals(expectedUuid, treatmentId.value());
        }

        @Test
        @DisplayName("fromString() should throw IllegalArgumentException for invalid UUID string")
        void fromStringShouldThrowIllegalArgumentExceptionForInvalidUUIDString() {
            // Given
            String invalidUuidString = "invalid-uuid-string";

            // When & Then
            assertThrows(
                IllegalArgumentException.class,
                () -> TreatmentId.fromString(invalidUuidString)
            );
        }
    }

    @Nested
    @DisplayName("Value Object Contract Tests")
    class ValueObjectContractTests {
        @Test
        @DisplayName("Should implement ValueObject interface")
        void shouldImplementValueObjectInterface() {
            // Given
            TreatmentId treatmentId = TreatmentId.generate();

            // Then
            assertTrue(treatmentId instanceof ValueObject);
        }

        @Test
        @DisplayName("Equals and hashCode should be based on UUID value")
        void equalsAndHashCodeShouldBeBasedOnUUIDValue() {
            // Given
            UUID uuid1 = UUID.randomUUID();
            UUID uuid2 = UUID.randomUUID();

            TreatmentId treatmentId1 = new TreatmentId(uuid1);
            TreatmentId treatmentId2 = new TreatmentId(uuid1);
            TreatmentId treatmentId3 = new TreatmentId(uuid2);

            // Then
            assertEquals(treatmentId1, treatmentId2);
            assertNotEquals(treatmentId1, treatmentId3);
            assertEquals(treatmentId1.hashCode(), treatmentId2.hashCode());
            assertNotEquals(treatmentId1.hashCode(), treatmentId3.hashCode());
        }

        @Test
        @DisplayName("Should not be equal to null or different type")
        void shouldNotBeEqualToNullOrDifferentType() {
            // Given
            TreatmentId treatmentId = TreatmentId.generate();

            // Then
            assertNotEquals(null, treatmentId);
            assertNotEquals("some string", treatmentId);
        }

        @Test
        @DisplayName("toString() should return meaningful representation")
        void toStringShouldReturnMeaningfulRepresentation() {
            // Given
            UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
            TreatmentId treatmentId = new TreatmentId(uuid);

            // When
            String result = treatmentId.toString();

            // Then
            assertNotNull(result);
            assertTrue(result.contains("TreatmentId"));
            assertTrue(result.contains(uuid.toString()));
        }
    }

    @Nested
    @DisplayName("Record Behavior Tests")
    class RecordBehaviorTests {
        @Test
        @DisplayName("Should have immutable value")
        void shouldHaveImmutableValue() {
            // Given
            UUID originalUuid = UUID.randomUUID();
            TreatmentId treatmentId = new TreatmentId(originalUuid);

            // When
            UUID retrievedValue = treatmentId.value();

            // Then
            assertEquals(originalUuid, retrievedValue);
            assertSame(originalUuid, retrievedValue);
        }

        @Test
        @DisplayName("Should support pattern matching with instanceof")
        void shouldSupportPatternMatchingWithInstanceof() {
            // Given
            TreatmentId treatmentId = TreatmentId.generate();

            // When
            if (treatmentId instanceof TreatmentId(UUID uuid)) {
                // Then
                assertNotNull(uuid);
                assertEquals(treatmentId.value(), uuid);
            } else {
                fail("Pattern matching should succeed");
            }
        }
    }
}