package com.application.domain.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentStatusTest {

    @Test
    @DisplayName("AppointmentStatus debe implementar la interfaz ValueObject")
    void shouldImplementValueObject() {
        assertTrue(ValueObject.class.isAssignableFrom(AppointmentStatus.class),
                "AppointmentStatus debe implementar ValueObject");
    }

    @ParameterizedTest
    @EnumSource(AppointmentStatus.class)
    @DisplayName("Todos los valores del enum deben ser accesibles")
    void allEnumValuesShouldBeAccessible(AppointmentStatus status) {
        assertNotNull(status);
        assertNotNull(status.name());
        assertTrue(status.name().length() > 0);
    }

    @Test
    @DisplayName("El enum debe contener exactamente 5 valores definidos")
    void shouldHaveExactlyFiveValues() {
        AppointmentStatus[] values = AppointmentStatus.values();
        assertEquals(5, values.length, "Debe haber exactamente 5 estados de cita");

        // Verificar que los valores esperados existen
        assertTrue(containsValue(values, "PROGRAMADA"));
        assertTrue(containsValue(values, "CONFIRMADA"));
        assertTrue(containsValue(values, "EN_CURSO"));
        assertTrue(containsValue(values, "COMPLETADA"));
        assertTrue(containsValue(values, "CANCELADA"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"PROGRAMADA", "CONFIRMADA", "EN_CURSO", "COMPLETADA", "CANCELADA"})
    @DisplayName("Debe poder obtenerse un valor por su nombre")
    void shouldGetValueByName(String statusName) {
        AppointmentStatus status = AppointmentStatus.valueOf(statusName);
        assertNotNull(status);
        assertEquals(statusName, status.name());
    }

    @Test
    @DisplayName("valueOf debe lanzar IllegalArgumentException para nombre inválido")
    void valueOfShouldThrowExceptionForInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> {
            AppointmentStatus.valueOf("INEXISTENTE");
        });
    }

    @Test
    @DisplayName("Los valores deben estar en el orden correcto")
    void valuesShouldBeInCorrectOrder() {
        AppointmentStatus[] values = AppointmentStatus.values();
        
        assertEquals(AppointmentStatus.PROGRAMADA, values[0]);
        assertEquals(AppointmentStatus.CONFIRMADA, values[1]);
        assertEquals(AppointmentStatus.EN_CURSO, values[2]);
        assertEquals(AppointmentStatus.COMPLETADA, values[3]);
        assertEquals(AppointmentStatus.CANCELADA, values[4]);
    }

    @Test
    @DisplayName("Métodos de transición de estado - verificar estados válidos")
    void verifyStateTransitions() {
        // PROGRAMADA puede cambiar a CONFIRMADA o CANCELADA
        assertTrue(isValidTransition(AppointmentStatus.PROGRAMADA, AppointmentStatus.CONFIRMADA));
        assertTrue(isValidTransition(AppointmentStatus.PROGRAMADA, AppointmentStatus.CANCELADA));
        
        // CONFIRMADA puede cambiar a EN_CURSO o CANCELADA
        assertTrue(isValidTransition(AppointmentStatus.CONFIRMADA, AppointmentStatus.EN_CURSO));
        assertTrue(isValidTransition(AppointmentStatus.CONFIRMADA, AppointmentStatus.CANCELADA));
        
        // EN_CURSO puede cambiar a COMPLETADA o CANCELADA
        assertTrue(isValidTransition(AppointmentStatus.EN_CURSO, AppointmentStatus.COMPLETADA));
        assertTrue(isValidTransition(AppointmentStatus.EN_CURSO, AppointmentStatus.CANCELADA));
        
        // COMPLETADA y CANCELADA son estados finales
        assertFalse(isValidTransition(AppointmentStatus.COMPLETADA, AppointmentStatus.PROGRAMADA));
        assertFalse(isValidTransition(AppointmentStatus.CANCELADA, AppointmentStatus.PROGRAMADA));
    }

    @Test
    @DisplayName("Estados finales no deben permitir transiciones")
    void finalStatesShouldNotAllowTransitions() {
        assertFalse(isValidTransition(AppointmentStatus.COMPLETADA, AppointmentStatus.EN_CURSO));
        assertFalse(isValidTransition(AppointmentStatus.CANCELADA, AppointmentStatus.CONFIRMADA));
        assertFalse(isValidTransition(AppointmentStatus.COMPLETADA, AppointmentStatus.CANCELADA));
        assertFalse(isValidTransition(AppointmentStatus.CANCELADA, AppointmentStatus.COMPLETADA));
    }

    @Test
    @DisplayName("Verificar estados activos vs finalizados")
    void verifyActiveVsCompletedStates() {
        assertTrue(isActiveState(AppointmentStatus.PROGRAMADA));
        assertTrue(isActiveState(AppointmentStatus.CONFIRMADA));
        assertTrue(isActiveState(AppointmentStatus.EN_CURSO));
        assertFalse(isActiveState(AppointmentStatus.COMPLETADA));
        assertFalse(isActiveState(AppointmentStatus.CANCELADA));
    }

    @Test
    @DisplayName("Verificar estados cancelables")
    void verifyCancelableStates() {
        assertTrue(isCancelable(AppointmentStatus.PROGRAMADA));
        assertTrue(isCancelable(AppointmentStatus.CONFIRMADA));
        assertTrue(isCancelable(AppointmentStatus.EN_CURSO));
        assertFalse(isCancelable(AppointmentStatus.COMPLETADA));
        assertFalse(isCancelable(AppointmentStatus.CANCELADA));
    }

    @Test
    @DisplayName("toString debe devolver el nombre del enum")
    void toStringShouldReturnEnumName() {
        assertEquals("PROGRAMADA", AppointmentStatus.PROGRAMADA.toString());
        assertEquals("CANCELADA", AppointmentStatus.CANCELADA.toString());
    }

    @Test
    @DisplayName("Comparación de valores enum")
    void enumValueComparison() {
        assertNotEquals(AppointmentStatus.PROGRAMADA, AppointmentStatus.CONFIRMADA);
        assertEquals(AppointmentStatus.PROGRAMADA, AppointmentStatus.valueOf("PROGRAMADA"));
    }

    // Métodos auxiliares para verificar reglas de negocio
    private boolean isValidTransition(AppointmentStatus from, AppointmentStatus to) {
        // Reglas de transición basadas en el dominio
        if (from == AppointmentStatus.PROGRAMADA) {
            return to == AppointmentStatus.CONFIRMADA || to == AppointmentStatus.CANCELADA;
        }
        if (from == AppointmentStatus.CONFIRMADA) {
            return to == AppointmentStatus.EN_CURSO || to == AppointmentStatus.CANCELADA;
        }
        if (from == AppointmentStatus.EN_CURSO) {
            return to == AppointmentStatus.COMPLETADA || to == AppointmentStatus.CANCELADA;
        }
        // COMPLETADA y CANCELADA son estados finales
        return false;
    }

    private boolean isActiveState(AppointmentStatus status) {
        return status == AppointmentStatus.PROGRAMADA || 
               status == AppointmentStatus.CONFIRMADA || 
               status == AppointmentStatus.EN_CURSO;
    }

    private boolean isCancelable(AppointmentStatus status) {
        return status == AppointmentStatus.PROGRAMADA || 
               status == AppointmentStatus.CONFIRMADA || 
               status == AppointmentStatus.EN_CURSO;
    }

    private boolean containsValue(AppointmentStatus[] values, String valueName) {
        for (AppointmentStatus status : values) {
            if (status.name().equals(valueName)) {
                return true;
            }
        }
        return false;
    }
}