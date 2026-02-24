package com.application.domain.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InvoiceIdTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {
        @Test
        @DisplayName("Should create InvoiceId with valid UUID")
        void shouldCreateInvoiceIdWithValidUUID() {
            UUID uuid = UUID.randomUUID();
            InvoiceId invoiceId = new InvoiceId(uuid);
            assertNotNull(invoiceId);
            assertEquals(uuid, invoiceId.value());
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when value is null")
        void shouldThrowIllegalArgumentExceptionWhenValueIsNull() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new InvoiceId(null)
            );
            assertEquals("InvoiceId value cannot be null", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Static Factory Method Tests")
    class StaticFactoryMethodTests {
        @Test
        @DisplayName("generate() should create InvoiceId with random UUID")
        void generateShouldCreateInvoiceIdWithRandomUUID() {
            InvoiceId invoiceId = InvoiceId.generate();
            assertNotNull(invoiceId);
            assertNotNull(invoiceId.value());
        }

        @Test
        @DisplayName("fromString() should create InvoiceId from valid UUID string")
        void fromStringShouldCreateInvoiceIdFromValidUUIDString() {
            String uuidString = "123e4567-e89b-12d3-a456-426614174000";
            InvoiceId invoiceId = InvoiceId.fromString(uuidString);
            assertNotNull(invoiceId);
            assertEquals(UUID.fromString(uuidString), invoiceId.value());
        }

        @Test
        @DisplayName("fromString() should throw IllegalArgumentException for invalid UUID string")
        void fromStringShouldThrowIllegalArgumentExceptionForInvalidUUIDString() {
            String invalidUuidString = "invalid-uuid";
            assertThrows(
                IllegalArgumentException.class,
                () -> InvoiceId.fromString(invalidUuidString)
            );
        }
    }

    @Nested
    @DisplayName("Value Object Contract Tests")
    class ValueObjectContractTests {
        @Test
        @DisplayName("Should implement ValueObject interface")
        void shouldImplementValueObjectInterface() {
            InvoiceId invoiceId = new InvoiceId(UUID.randomUUID());
            assertTrue(invoiceId instanceof ValueObject);
        }

        @Test
        @DisplayName("Equals should return true for same UUID value")
        void equalsShouldReturnTrueForSameUUIDValue() {
            UUID uuid = UUID.randomUUID();
            InvoiceId invoiceId1 = new InvoiceId(uuid);
            InvoiceId invoiceId2 = new InvoiceId(uuid);
            assertEquals(invoiceId1, invoiceId2);
        }

        @Test
        @DisplayName("Equals should return false for different UUID values")
        void equalsShouldReturnFalseForDifferentUUIDValues() {
            InvoiceId invoiceId1 = new InvoiceId(UUID.randomUUID());
            InvoiceId invoiceId2 = new InvoiceId(UUID.randomUUID());
            assertNotEquals(invoiceId1, invoiceId2);
        }

        @Test
        @DisplayName("HashCode should be equal for same UUID value")
        void hashCodeShouldBeEqualForSameUUIDValue() {
            UUID uuid = UUID.randomUUID();
            InvoiceId invoiceId1 = new InvoiceId(uuid);
            InvoiceId invoiceId2 = new InvoiceId(uuid);
            assertEquals(invoiceId1.hashCode(), invoiceId2.hashCode());
        }

        @Test
        @DisplayName("ToString should contain UUID value")
        void toStringShouldContainUUIDValue() {
            UUID uuid = UUID.randomUUID();
            InvoiceId invoiceId = new InvoiceId(uuid);
            String toStringResult = invoiceId.toString();
            assertTrue(toStringResult.contains(uuid.toString()));
        }
    }
}