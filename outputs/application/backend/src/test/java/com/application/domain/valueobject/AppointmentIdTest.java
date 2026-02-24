package com.application.domain.valueobject;

import com.application.domain.exception.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AppointmentId Value Object Tests")
class AppointmentIdTest {

    @Test
    @DisplayName("Should create AppointmentId with valid UUID")
    void shouldCreateAppointmentIdWithValidUUID() {
        // Given
        UUID expectedUuid = UUID.randomUUID();

        // When
        AppointmentId appointmentId = new AppointmentId(expectedUuid);

        // Then
        assertNotNull(appointmentId);
        assertEquals(expectedUuid, appointmentId.value());
    }

    @Test
    @DisplayName("Should throw DomainException when UUID is null")
    void shouldThrowDomainExceptionWhenUUIDIsNull() {
        // Given
        UUID nullUuid = null;

        // When & Then
        DomainException exception = assertThrows(DomainException.class,
                () -> new AppointmentId(nullUuid));
        assertEquals("El ID de la cita no puede ser nulo", exception.getMessage());
    }

    @Nested
    @DisplayName("Generate Method Tests")
    class GenerateMethodTests {

        @Test
        @DisplayName("Should generate a new AppointmentId with random UUID")
        void shouldGenerateNewAppointmentIdWithRandomUUID() {
            // When
            AppointmentId generatedId = AppointmentId.generate();

            // Then
            assertNotNull(generatedId);
            assertNotNull(generatedId.value());
        }

        @Test
        @DisplayName("Generated IDs should be unique")
        void generatedIdsShouldBeUnique() {
            // When
            AppointmentId id1 = AppointmentId.generate();
            AppointmentId id2 = AppointmentId.generate();

            // Then
            assertNotEquals(id1.value(), id2.value());
        }
    }

    @Nested
    @DisplayName("FromString Method Tests")
    class FromStringMethodTests {

        @Test
        @DisplayName("Should create AppointmentId from valid UUID string")
        void shouldCreateAppointmentIdFromValidUUIDString() {
            // Given
            String validUuidString = "123e4567-e89b-12d3-a456-426614174000";

            // When
            AppointmentId appointmentId = AppointmentId.fromString(validUuidString);

            // Then
            assertNotNull(appointmentId);
            assertEquals(UUID.fromString(validUuidString), appointmentId.value());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"invalid-uuid", "123", "123e4567-e89b-12d3-a456-42661417400"})
        @DisplayName("Should throw DomainException when string is invalid")
        void shouldThrowDomainExceptionWhenStringIsInvalid(String invalidUuidString) {
            // When & Then
            DomainException exception = assertThrows(DomainException.class,
                    () -> AppointmentId.fromString(invalidUuidString));
            assertTrue(exception.getMessage().contains("ID de cita inválido"));
        }
    }

    @Test
    @DisplayName("Should return correct string representation")
    void shouldReturnCorrectStringRepresentation() {
        // Given
        UUID uuid = UUID.randomUUID();
        AppointmentId appointmentId = new AppointmentId(uuid);

        // When
        String stringRepresentation = appointmentId.toString();

        // Then
        assertEquals(uuid.toString(), stringRepresentation);
    }

    @Test
    @DisplayName("Should implement ValueObject interface")
    void shouldImplementValueObjectInterface() {
        // Given
        AppointmentId appointmentId = AppointmentId.generate();

        // Then
        assertTrue(appointmentId instanceof ValueObject);
    }

    @Test
    @DisplayName("Should have proper equals and hashCode implementation")
    void shouldHaveProperEqualsAndHashCodeImplementation() {
        // Given
        UUID uuid = UUID.randomUUID();
        AppointmentId id1 = new AppointmentId(uuid);
        AppointmentId id2 = new AppointmentId(uuid);
        AppointmentId differentId = AppointmentId.generate();

        // Then
        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1, differentId);
        assertNotEquals(id1.hashCode(), differentId.hashCode());
    }
}