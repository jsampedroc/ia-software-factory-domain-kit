package com.application.domain.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PlanStatus Value Object Test")
class PlanStatusTest {

    @Test
    @DisplayName("PlanStatus debe implementar la interfaz ValueObject")
    void shouldImplementValueObjectInterface() {
        assertTrue(ValueObject.class.isAssignableFrom(PlanStatus.class),
                "PlanStatus debe implementar ValueObject");
    }

    @ParameterizedTest
    @EnumSource(PlanStatus.class)
    @DisplayName("Todos los valores del enum deben estar definidos")
    void allEnumValuesShouldBeDefined(PlanStatus status) {
        assertNotNull(status, "El valor del enum no debe ser nulo");
        assertNotNull(status.name(), "El nombre del enum no debe ser nulo");
    }

    @Test
    @DisplayName("El enum debe contener exactamente 4 valores")
    void shouldHaveExactlyFourValues() {
        PlanStatus[] values = PlanStatus.values();
        assertEquals(4, values.length, "PlanStatus debe tener exactamente 4 valores");
    }

    @Test
    @DisplayName("Los valores del enum deben estar en el orden correcto")
    void shouldHaveCorrectEnumValues() {
        PlanStatus[] values = PlanStatus.values();
        
        assertEquals(PlanStatus.BORRADOR, values[0], "Primer valor debe ser BORRADOR");
        assertEquals(PlanStatus.ACTIVO, values[1], "Segundo valor debe ser ACTIVO");
        assertEquals(PlanStatus.COMPLETADO, values[2], "Tercer valor debe ser COMPLETADO");
        assertEquals(PlanStatus.CANCELADO, values[3], "Cuarto valor debe ser CANCELADO");
    }

    @ParameterizedTest
    @ValueSource(strings = {"BORRADOR", "ACTIVO", "COMPLETADO", "CANCELADO"})
    @DisplayName("valueOf debe retornar el enum correcto para cada nombre")
    void valueOfShouldReturnCorrectEnum(String enumName) {
        PlanStatus status = PlanStatus.valueOf(enumName);
        assertNotNull(status, "valueOf no debe retornar nulo para: " + enumName);
        assertEquals(enumName, status.name(), "El nombre debe coincidir");
    }

    @Test
    @DisplayName("valueOf debe lanzar IllegalArgumentException para nombre inválido")
    void valueOfShouldThrowExceptionForInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> {
            PlanStatus.valueOf("INVALIDO");
        }, "Debe lanzar IllegalArgumentException para nombre de enum inválido");
    }

    @Test
    @DisplayName("Los métodos ordinales deben ser consistentes")
    void ordinalShouldBeConsistent() {
        assertEquals(0, PlanStatus.BORRADOR.ordinal(), "BORRADOR debe tener ordinal 0");
        assertEquals(1, PlanStatus.ACTIVO.ordinal(), "ACTIVO debe tener ordinal 1");
        assertEquals(2, PlanStatus.COMPLETADO.ordinal(), "COMPLETADO debe tener ordinal 2");
        assertEquals(3, PlanStatus.CANCELADO.ordinal(), "CANCELADO debe tener ordinal 3");
    }

    @Test
    @DisplayName("Comparación entre valores del enum")
    void enumComparisonShouldWork() {
        assertTrue(PlanStatus.BORRADOR.compareTo(PlanStatus.ACTIVO) < 0,
                "BORRADOR debe ser menor que ACTIVO");
        assertTrue(PlanStatus.ACTIVO.compareTo(PlanStatus.COMPLETADO) < 0,
                "ACTIVO debe ser menor que COMPLETADO");
        assertTrue(PlanStatus.COMPLETADO.compareTo(PlanStatus.CANCELADO) < 0,
                "COMPLETADO debe ser menor que CANCELADO");
        assertEquals(0, PlanStatus.BORRADOR.compareTo(PlanStatus.BORRADOR),
                "Comparación consigo mismo debe ser 0");
    }

    @Test
    @DisplayName("toString debe retornar el nombre del enum")
    void toStringShouldReturnEnumName() {
        assertEquals("BORRADOR", PlanStatus.BORRADOR.toString());
        assertEquals("ACTIVO", PlanStatus.ACTIVO.toString());
        assertEquals("COMPLETADO", PlanStatus.COMPLETADO.toString());
        assertEquals("CANCELADO", PlanStatus.CANCELADO.toString());
    }

    @Test
    @DisplayName("Verificación de igualdad entre valores del enum")
    void enumEqualityShouldWork() {
        assertSame(PlanStatus.BORRADOR, PlanStatus.valueOf("BORRADOR"),
                "BORRADOR debe ser la misma instancia");
        assertNotSame(PlanStatus.BORRADOR, PlanStatus.ACTIVO,
                "BORRADOR y ACTIVO deben ser diferentes instancias");
    }
}