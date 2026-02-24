package com.application.domain.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConsultingRoomIdTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {
        @Test
        @DisplayName("Should create ConsultingRoomId with valid UUID")
        void shouldCreateConsultingRoomIdWithValidUUID() {
            UUID uuid = UUID.randomUUID();
            ConsultingRoomId consultingRoomId = new ConsultingRoomId(uuid);
            assertNotNull(consultingRoomId);
            assertEquals(uuid, consultingRoomId.value());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when UUID is null")
        void shouldThrowIllegalArgumentExceptionWhenUUIDIsNull() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ConsultingRoomId(null)
            );
            assertEquals("ConsultingRoomId value cannot be null", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Static Factory Method Tests")
    class StaticFactoryMethodTests {
        @Test
        @DisplayName("generate() should create a new ConsultingRoomId with random UUID")
        void generateShouldCreateNewConsultingRoomIdWithRandomUUID() {
            ConsultingRoomId consultingRoomId = ConsultingRoomId.generate();
            assertNotNull(consultingRoomId);
            assertNotNull(consultingRoomId.value());
        }

        @Test
        @DisplayName("fromString() should create ConsultingRoomId from valid UUID string")
        void fromStringShouldCreateConsultingRoomIdFromValidUUIDString() {
            String uuidString = "123e4567-e89b-12d3-a456-426614174000";
            ConsultingRoomId consultingRoomId = ConsultingRoomId.fromString(uuidString);
            assertNotNull(consultingRoomId);
            assertEquals(UUID.fromString(uuidString), consultingRoomId.value());
        }

        @Test
        @DisplayName("fromString() should throw IllegalArgumentException for invalid UUID string")
        void fromStringShouldThrowIllegalArgumentExceptionForInvalidUUIDString() {
            String invalidUuidString = "invalid-uuid";
            assertThrows(
                IllegalArgumentException.class,
                () -> ConsultingRoomId.fromString(invalidUuidString)
            );
        }
    }

    @Nested
    @DisplayName("Value Object Contract Tests")
    class ValueObjectContractTests {
        @Test
        @DisplayName("Should implement ValueObject interface")
        void shouldImplementValueObjectInterface() {
            ConsultingRoomId consultingRoomId = ConsultingRoomId.generate();
            assertTrue(consultingRoomId instanceof ValueObject);
        }

        @Test
        @DisplayName("Equals and hashCode should be based on UUID value")
        void equalsAndHashCodeShouldBeBasedOnUUIDValue() {
            UUID uuid1 = UUID.randomUUID();
            UUID uuid2 = UUID.randomUUID();

            ConsultingRoomId id1 = new ConsultingRoomId(uuid1);
            ConsultingRoomId id2 = new ConsultingRoomId(uuid1);
            ConsultingRoomId id3 = new ConsultingRoomId(uuid2);

            assertEquals(id1, id2);
            assertNotEquals(id1, id3);
            assertEquals(id1.hashCode(), id2.hashCode());
            assertNotEquals(id1.hashCode(), id3.hashCode());
        }

        @Test
        @DisplayName("toString() should return meaningful representation")
        void toStringShouldReturnMeaningfulRepresentation() {
            UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
            ConsultingRoomId consultingRoomId = new ConsultingRoomId(uuid);
            String stringRepresentation = consultingRoomId.toString();
            assertTrue(stringRepresentation.contains("ConsultingRoomId"));
            assertTrue(stringRepresentation.contains(uuid.toString()));
        }
    }
}