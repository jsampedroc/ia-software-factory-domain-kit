package com.application.domain.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PatientIdTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {
        @Test
        @DisplayName("Should create PatientId with valid UUID")
        void shouldCreatePatientIdWithValidUUID() {
            UUID uuid = UUID.randomUUID();
            PatientId patientId = new PatientId(uuid);
            assertNotNull(patientId);
            assertEquals(uuid, patientId.value());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when UUID is null")
        void shouldThrowIllegalArgumentExceptionWhenUUIDIsNull() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new PatientId(null)
            );
            assertEquals("PatientId value cannot be null", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Static Factory Method Tests")
    class StaticFactoryMethodTests {
        @Test
        @DisplayName("generate() should create PatientId with random UUID")
        void generateShouldCreatePatientIdWithRandomUUID() {
            PatientId patientId = PatientId.generate();
            assertNotNull(patientId);
            assertNotNull(patientId.value());
        }

        @Test
        @DisplayName("fromString() should create PatientId from valid UUID string")
        void fromStringShouldCreatePatientIdFromValidUUIDString() {
            String uuidString = "123e4567-e89b-12d3-a456-426614174000";
            PatientId patientId = PatientId.fromString(uuidString);
            assertNotNull(patientId);
            assertEquals(UUID.fromString(uuidString), patientId.value());
        }

        @Test
        @DisplayName("fromString() should throw IllegalArgumentException for invalid UUID string")
        void fromStringShouldThrowIllegalArgumentExceptionForInvalidUUIDString() {
            String invalidUuidString = "invalid-uuid";
            assertThrows(
                IllegalArgumentException.class,
                () -> PatientId.fromString(invalidUuidString)
            );
        }
    }

    @Nested
    @DisplayName("Value Object Contract Tests")
    class ValueObjectContractTests {
        @Test
        @DisplayName("Should implement ValueObject interface")
        void shouldImplementValueObjectInterface() {
            PatientId patientId = new PatientId(UUID.randomUUID());
            assertTrue(patientId instanceof ValueObject);
        }

        @Test
        @DisplayName("Equals and hashCode should be based on value")
        void equalsAndHashCodeShouldBeBasedOnValue() {
            UUID uuid = UUID.randomUUID();
            PatientId patientId1 = new PatientId(uuid);
            PatientId patientId2 = new PatientId(uuid);
            PatientId patientId3 = new PatientId(UUID.randomUUID());

            assertEquals(patientId1, patientId2);
            assertNotEquals(patientId1, patientId3);
            assertEquals(patientId1.hashCode(), patientId2.hashCode());
            assertNotEquals(patientId1.hashCode(), patientId3.hashCode());
        }

        @Test
        @DisplayName("Should have correct toString representation")
        void shouldHaveCorrectToStringRepresentation() {
            UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
            PatientId patientId = new PatientId(uuid);
            String expected = "PatientId[value=" + uuid + "]";
            assertEquals(expected, patientId.toString());
        }
    }
}