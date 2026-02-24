package com.application.domain.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SpecialtyIdTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {
        @Test
        @DisplayName("Should create SpecialtyId with valid UUID")
        void shouldCreateSpecialtyIdWithValidUUID() {
            UUID uuid = UUID.randomUUID();
            SpecialtyId specialtyId = new SpecialtyId(uuid);
            assertEquals(uuid, specialtyId.value());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is null")
        void shouldThrowIllegalArgumentExceptionWhenValueIsNull() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new SpecialtyId(null)
            );
            assertEquals("SpecialtyId value cannot be null", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Static Factory Method Tests")
    class StaticFactoryMethodTests {
        @Test
        @DisplayName("generate() should create SpecialtyId with random UUID")
        void generateShouldCreateSpecialtyIdWithRandomUUID() {
            SpecialtyId specialtyId1 = SpecialtyId.generate();
            SpecialtyId specialtyId2 = SpecialtyId.generate();
            assertNotNull(specialtyId1.value());
            assertNotNull(specialtyId2.value());
            assertNotEquals(specialtyId1.value(), specialtyId2.value());
        }

        @Test
        @DisplayName("fromString() should create SpecialtyId from valid UUID string")
        void fromStringShouldCreateSpecialtyIdFromValidUUIDString() {
            String uuidString = "123e4567-e89b-12d3-a456-426614174000";
            SpecialtyId specialtyId = SpecialtyId.fromString(uuidString);
            assertEquals(UUID.fromString(uuidString), specialtyId.value());
        }

        @Test
        @DisplayName("fromString() should throw IllegalArgumentException for invalid UUID string")
        void fromStringShouldThrowIllegalArgumentExceptionForInvalidUUIDString() {
            String invalidUuidString = "invalid-uuid";
            assertThrows(
                IllegalArgumentException.class,
                () -> SpecialtyId.fromString(invalidUuidString)
            );
        }
    }

    @Nested
    @DisplayName("Value Object Contract Tests")
    class ValueObjectContractTests {
        @Test
        @DisplayName("Should implement ValueObject interface")
        void shouldImplementValueObjectInterface() {
            SpecialtyId specialtyId = new SpecialtyId(UUID.randomUUID());
            assertTrue(specialtyId instanceof ValueObject);
        }

        @Test
        @DisplayName("Equals and hashCode should be based on value")
        void equalsAndHashCodeShouldBeBasedOnValue() {
            UUID uuid = UUID.randomUUID();
            SpecialtyId specialtyId1 = new SpecialtyId(uuid);
            SpecialtyId specialtyId2 = new SpecialtyId(uuid);
            SpecialtyId specialtyId3 = new SpecialtyId(UUID.randomUUID());

            assertEquals(specialtyId1, specialtyId2);
            assertNotEquals(specialtyId1, specialtyId3);
            assertEquals(specialtyId1.hashCode(), specialtyId2.hashCode());
            assertNotEquals(specialtyId1.hashCode(), specialtyId3.hashCode());
        }

        @Test
        @DisplayName("toString should include UUID value")
        void toStringShouldIncludeUUIDValue() {
            UUID uuid = UUID.randomUUID();
            SpecialtyId specialtyId = new SpecialtyId(uuid);
            String toStringResult = specialtyId.toString();
            assertTrue(toStringResult.contains(uuid.toString()));
        }
    }
}