package com.application.domain.valueobject.facturacion;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DineroTest {

    @Nested
    class Creacion {
        @Test
        void deberiaCrearDineroConCantidadPositivaYDivisaValida() {
            Dinero dinero = new Dinero(100.50, "EUR");

            assertThat(dinero.cantidad()).isEqualTo(100.50);
            assertThat(dinero.divisa()).isEqualTo("EUR");
        }

        @Test
        void deberiaCrearDineroConCantidadCero() {
            Dinero dinero = new Dinero(0.0, "USD");

            assertThat(dinero.cantidad()).isEqualTo(0.0);
            assertThat(dinero.divisa()).isEqualTo("USD");
        }

        @ParameterizedTest
        @ValueSource(doubles = {-0.01, -1.0, -100.0})
        void deberiaLanzarExcepcionConCantidadNegativa(double cantidadNegativa) {
            assertThatThrownBy(() -> new Dinero(cantidadNegativa, "EUR"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("cantidad");
        }

        @ParameterizedTest
        @NullAndEmptySource
        void deberiaLanzarExcepcionConDivisaNulaOVacia(String divisaInvalida) {
            assertThatThrownBy(() -> new Dinero(100.0, divisaInvalida))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("divisa");
        }

        @Test
        void deberiaLanzarExcepcionConDivisaNoValida() {
            assertThatThrownBy(() -> new Dinero(100.0, "EURO"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("divisa");
        }
    }

    @Nested
    class IgualdadYHashCode {
        @Test
        void dosInstanciasConMismaCantidadYDivisaDeberianSerIguales() {
            Dinero dinero1 = new Dinero(50.75, "GBP");
            Dinero dinero2 = new Dinero(50.75, "GBP");

            assertThat(dinero1).isEqualTo(dinero2);
            assertThat(dinero1.hashCode()).isEqualTo(dinero2.hashCode());
        }

        @Test
        void dosInstanciasConDiferenteCantidadNoDeberianSerIguales() {
            Dinero dinero1 = new Dinero(50.75, "GBP");
            Dinero dinero2 = new Dinero(100.75, "GBP");

            assertThat(dinero1).isNotEqualTo(dinero2);
        }

        @Test
        void dosInstanciasConDiferenteDivisaNoDeberianSerIguales() {
            Dinero dinero1 = new Dinero(50.75, "GBP");
            Dinero dinero2 = new Dinero(50.75, "USD");

            assertThat(dinero1).isNotEqualTo(dinero2);
        }

        @Test
        void noDeberiaSerIgualANull() {
            Dinero dinero = new Dinero(10.0, "EUR");

            assertThat(dinero).isNotEqualTo(null);
        }

        @Test
        void noDeberiaSerIgualAOtroTipoDeObjeto() {
            Dinero dinero = new Dinero(10.0, "EUR");

            assertThat(dinero).isNotEqualTo("10.0 EUR");
        }
    }

    @Nested
    class Operaciones {
        @Test
        void sumarDeberiaRetornarNuevoDineroConSumaDeCantidades() {
            Dinero dinero1 = new Dinero(100.50, "EUR");
            Dinero dinero2 = new Dinero(50.25, "EUR");

            Dinero resultado = dinero1.sumar(dinero2);

            assertThat(resultado.cantidad()).isEqualTo(150.75);
            assertThat(resultado.divisa()).isEqualTo("EUR");
        }

        @Test
        void sumarDeberiaLanzarExcepcionSiDivisasSonDiferentes() {
            Dinero dinero1 = new Dinero(100.50, "EUR");
            Dinero dinero2 = new Dinero(50.25, "USD");

            assertThatThrownBy(() -> dinero1.sumar(dinero2))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("divisa");
        }

        @Test
        void restarDeberiaRetornarNuevoDineroConRestaDeCantidades() {
            Dinero dinero1 = new Dinero(100.50, "EUR");
            Dinero dinero2 = new Dinero(50.25, "EUR");

            Dinero resultado = dinero1.restar(dinero2);

            assertThat(resultado.cantidad()).isEqualTo(50.25);
            assertThat(resultado.divisa()).isEqualTo("EUR");
        }

        @Test
        void restarDeberiaLanzarExcepcionSiResultadoEsNegativo() {
            Dinero dinero1 = new Dinero(50.25, "EUR");
            Dinero dinero2 = new Dinero(100.50, "EUR");

            assertThatThrownBy(() -> dinero1.restar(dinero2))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("negativa");
        }

        @Test
        void restarDeberiaLanzarExcepcionSiDivisasSonDiferentes() {
            Dinero dinero1 = new Dinero(100.50, "EUR");
            Dinero dinero2 = new Dinero(50.25, "USD");

            assertThatThrownBy(() -> dinero1.restar(dinero2))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("divisa");
        }

        @Test
        void multiplicarPorEscalarDeberiaRetornarNuevoDineroConCantidadMultiplicada() {
            Dinero dinero = new Dinero(100.0, "JPY");

            Dinero resultado = dinero.multiplicar(2.5);

            assertThat(resultado.cantidad()).isEqualTo(250.0);
            assertThat(resultado.divisa()).isEqualTo("JPY");
        }

        @Test
        void multiplicarPorEscalarNegativoDeberiaLanzarExcepcion() {
            Dinero dinero = new Dinero(100.0, "JPY");

            assertThatThrownBy(() -> dinero.multiplicar(-1.0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("negativo");
        }

        @Test
        void multiplicarPorCeroDeberiaRetornarCero() {
            Dinero dinero = new Dinero(100.0, "JPY");

            Dinero resultado = dinero.multiplicar(0.0);

            assertThat(resultado.cantidad()).isEqualTo(0.0);
            assertThat(resultado.divisa()).isEqualTo("JPY");
        }

        @Test
        void esMayorQueDeberiaRetornarTrueSiCantidadEsMayor() {
            Dinero dinero1 = new Dinero(150.0, "EUR");
            Dinero dinero2 = new Dinero(100.0, "EUR");

            assertThat(dinero1.esMayorQue(dinero2)).isTrue();
        }

        @Test
        void esMayorQueDeberiaRetornarFalseSiCantidadEsMenor() {
            Dinero dinero1 = new Dinero(50.0, "EUR");
            Dinero dinero2 = new Dinero(100.0, "EUR");

            assertThat(dinero1.esMayorQue(dinero2)).isFalse();
        }

        @Test
        void esMayorQueDeberiaLanzarExcepcionSiDivisasSonDiferentes() {
            Dinero dinero1 = new Dinero(150.0, "EUR");
            Dinero dinero2 = new Dinero(100.0, "USD");

            assertThatThrownBy(() -> dinero1.esMayorQue(dinero2))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("divisa");
        }

        @Test
        void esMayorOIgualQueDeberiaRetornarTrueSiCantidadEsMayor() {
            Dinero dinero1 = new Dinero(150.0, "EUR");
            Dinero dinero2 = new Dinero(100.0, "EUR");

            assertThat(dinero1.esMayorOIgualQue(dinero2)).isTrue();
        }

        @Test
        void esMayorOIgualQueDeberiaRetornarTrueSiCantidadEsIgual() {
            Dinero dinero1 = new Dinero(100.0, "EUR");
            Dinero dinero2 = new Dinero(100.0, "EUR");

            assertThat(dinero1.esMayorOIgualQue(dinero2)).isTrue();
        }

        @Test
        void esMayorOIgualQueDeberiaRetornarFalseSiCantidadEsMenor() {
            Dinero dinero1 = new Dinero(50.0, "EUR");
            Dinero dinero2 = new Dinero(100.0, "EUR");

            assertThat(dinero1.esMayorOIgualQue(dinero2)).isFalse();
        }
    }

    @Nested
    class RepresentacionComoString {
        @Test
        void toStringDeberiaIncluirCantidadYDivisa() {
            Dinero dinero = new Dinero(123.45, "EUR");

            String representacion = dinero.toString();

            assertThat(representacion).contains("123.45");
            assertThat(representacion).contains("EUR");
        }
    }
}