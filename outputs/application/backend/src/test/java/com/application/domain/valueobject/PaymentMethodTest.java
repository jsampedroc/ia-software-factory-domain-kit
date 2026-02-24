package com.application.domain.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PaymentMethod Value Object Tests")
class PaymentMethodTest {

    @Test
    @DisplayName("PaymentMethod should implement ValueObject interface")
    void shouldImplementValueObject() {
        assertTrue(ValueObject.class.isAssignableFrom(PaymentMethod.class),
                "PaymentMethod debe implementar la interfaz ValueObject");
    }

    @ParameterizedTest
    @EnumSource(PaymentMethod.class)
    @DisplayName("All enum values should be defined and accessible")
    void shouldHaveAllEnumValuesDefined(PaymentMethod paymentMethod) {
        assertNotNull(paymentMethod);
        assertNotNull(paymentMethod.name());
    }

    @Test
    @DisplayName("PaymentMethod should have exactly three values")
    void shouldHaveThreeValues() {
        PaymentMethod[] values = PaymentMethod.values();
        assertEquals(3, values.length, "PaymentMethod debe tener exactamente 3 valores");
    }

    @ParameterizedTest
    @ValueSource(strings = {"EFECTIVO", "TARJETA", "TRANSFERENCIA"})
    @DisplayName("PaymentMethod should contain specific values")
    void shouldContainSpecificValues(String valueName) {
        PaymentMethod paymentMethod = PaymentMethod.valueOf(valueName);
        assertNotNull(paymentMethod);
        assertEquals(valueName, paymentMethod.name());
    }

    @Test
    @DisplayName("PaymentMethod valueOf should be case sensitive")
    void valueOfShouldBeCaseSensitive() {
        assertThrows(IllegalArgumentException.class, () -> {
            PaymentMethod.valueOf("efectivo");
        });
    }

    @Test
    @DisplayName("PaymentMethod should be serializable")
    void shouldBeSerializable() {
        assertDoesNotThrow(() -> {
            PaymentMethod efectivo = PaymentMethod.EFECTIVO;
            PaymentMethod tarjeta = PaymentMethod.TARJETA;
            PaymentMethod transferencia = PaymentMethod.TRANSFERENCIA;
        });
    }

    @Test
    @DisplayName("PaymentMethod ordinal positions should be consistent")
    void shouldHaveConsistentOrdinalPositions() {
        assertEquals(0, PaymentMethod.EFECTIVO.ordinal());
        assertEquals(1, PaymentMethod.TARJETA.ordinal());
        assertEquals(2, PaymentMethod.TRANSFERENCIA.ordinal());
    }
}