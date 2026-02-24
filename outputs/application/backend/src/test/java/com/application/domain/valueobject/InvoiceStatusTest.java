package com.application.domain.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class InvoiceStatusTest {

    @Test
    @DisplayName("InvoiceStatus debe implementar la interfaz ValueObject")
    void shouldImplementValueObject() {
        assertTrue(ValueObject.class.isAssignableFrom(InvoiceStatus.class),
            "InvoiceStatus debe implementar ValueObject");
    }

    @Test
    @DisplayName("InvoiceStatus debe tener exactamente 4 valores definidos")
    void shouldHaveExactlyFourValues() {
        InvoiceStatus[] values = InvoiceStatus.values();
        assertEquals(4, values.length, "InvoiceStatus debe tener 4 valores");
    }

    @ParameterizedTest
    @EnumSource(InvoiceStatus.class)
    @DisplayName("Cada valor de InvoiceStatus debe ser no nulo")
    void eachEnumValueShouldBeNonNull(InvoiceStatus status) {
        assertNotNull(status, "El valor del enum no debe ser nulo");
    }

    @ParameterizedTest
    @ValueSource(strings = {"PENDIENTE", "PAGADA", "VENCIDA", "CANCELADA"})
    @DisplayName("InvoiceStatus debe contener los valores esperados")
    void shouldContainExpectedValues(String expectedName) {
        InvoiceStatus status = InvoiceStatus.valueOf(expectedName);
        assertEquals(expectedName, status.name(), "El nombre del enum debe coincidir");
    }

    @Test
    @DisplayName("InvoiceStatus.valueOf debe lanzar excepción para valor inválido")
    void valueOfShouldThrowExceptionForInvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> {
            InvoiceStatus.valueOf("INVALIDO");
        }, "Debe lanzar IllegalArgumentException para valor inválido");
    }

    @Test
    @DisplayName("InvoiceStatus.values() debe devolver array en orden de declaración")
    void valuesShouldReturnInDeclarationOrder() {
        InvoiceStatus[] values = InvoiceStatus.values();
        assertEquals(InvoiceStatus.PENDIENTE, values[0]);
        assertEquals(InvoiceStatus.PAGADA, values[1]);
        assertEquals(InvoiceStatus.VENCIDA, values[2]);
        assertEquals(InvoiceStatus.CANCELADA, values[3]);
    }

    @Test
    @DisplayName("InvoiceStatus puede ser usado en switch")
    void canBeUsedInSwitchStatement() {
        InvoiceStatus status = InvoiceStatus.PAGADA;
        String result = switch (status) {
            case PENDIENTE -> "pendiente";
            case PAGADA -> "pagada";
            case VENCIDA -> "vencida";
            case CANCELADA -> "cancelada";
        };
        assertEquals("pagada", result);
    }
}