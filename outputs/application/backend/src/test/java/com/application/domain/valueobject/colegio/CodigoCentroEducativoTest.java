package com.application.domain.valueobject.colegio;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import com.application.domain.shared.ValueObject;

import static org.junit.jupiter.api.Assertions.*;

class CodigoCentroEducativoTest {

    @Test
    void shouldCreateValidCodigoCentroEducativo() {
        String validCode = "28012345";
        CodigoCentroEducativo codigo = new CodigoCentroEducativo(validCode);
        assertNotNull(codigo);
        assertEquals(validCode, codigo.codigo());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    void shouldThrowExceptionWhenCodigoIsNullOrBlank(String invalidCode) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CodigoCentroEducativo(invalidCode)
        );
        assertTrue(exception.getMessage().contains("código"));
    }

    @Test
    void shouldImplementValueObject() {
        CodigoCentroEducativo codigo = new CodigoCentroEducativo("28012345");
        assertTrue(codigo instanceof ValueObject);
    }

    @Test
    void shouldBeEqualWhenSameCodigo() {
        CodigoCentroEducativo codigo1 = new CodigoCentroEducativo("28012345");
        CodigoCentroEducativo codigo2 = new CodigoCentroEducativo("28012345");
        assertEquals(codigo1, codigo2);
        assertEquals(codigo1.hashCode(), codigo2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentCodigo() {
        CodigoCentroEducativo codigo1 = new CodigoCentroEducativo("28012345");
        CodigoCentroEducativo codigo2 = new CodigoCentroEducativo("28067890");
        assertNotEquals(codigo1, codigo2);
        assertNotEquals(codigo1.hashCode(), codigo2.hashCode());
    }

    @Test
    void shouldReturnCorrectString() {
        String code = "28012345";
        CodigoCentroEducativo codigo = new CodigoCentroEducativo(code);
        assertEquals(code, codigo.toString());
    }
}