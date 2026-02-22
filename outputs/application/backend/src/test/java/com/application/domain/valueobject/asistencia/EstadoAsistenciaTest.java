package com.application.domain.valueobject.asistencia;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.EnumSource;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class EstadoAsistenciaTest {

    @Nested
    class CreacionValida {
        @ParameterizedTest
        @EnumSource(EstadoAsistencia.Valor.class)
        void deberiaCrearseConValorValido(EstadoAsistencia.Valor valor) {
            EstadoAsistencia estado = new EstadoAsistencia(valor);
            assertThat(estado).isNotNull();
            assertThat(estado.valor()).isEqualTo(valor);
        }

        @Test
        void deberiaCrearseConValorDesdeStringValido() {
            EstadoAsistencia estado = new EstadoAsistencia("PRESENTE");
            assertThat(estado.valor()).isEqualTo(EstadoAsistencia.Valor.PRESENTE);
        }
    }

    @Nested
    class CreacionInvalida {
        @Test
        void deberiaFallarConValorNulo() {
            assertThrows(NullPointerException.class, () -> new EstadoAsistencia((EstadoAsistencia.Valor) null));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "  ", "\t", "\n", "INVALIDO", "ausente"})
        void deberiaFallarConStringInvalido(String valorInvalido) {
            assertThrows(IllegalArgumentException.class, () -> new EstadoAsistencia(valorInvalido));
        }
    }

    @Nested
    class MetodosDeValor {
        @Test
        void valorComoStringDeberiaDevolverNombre() {
            EstadoAsistencia estado = new EstadoAsistencia(EstadoAsistencia.Valor.JUSTIFICADO);
            assertThat(estado.valorComoString()).isEqualTo("JUSTIFICADO");
        }

        @Test
        void esAusenteNoJustificadaDeberiaDevolverTrueSoloParaAusente() {
            EstadoAsistencia ausente = new EstadoAsistencia(EstadoAsistencia.Valor.AUSENTE);
            assertThat(ausente.esAusenteNoJustificada()).isTrue();

            EstadoAsistencia presente = new EstadoAsistencia(EstadoAsistencia.Valor.PRESENTE);
            assertThat(presente.esAusenteNoJustificada()).isFalse();

            EstadoAsistencia justificado = new EstadoAsistencia(EstadoAsistencia.Valor.JUSTIFICADO);
            assertThat(justificado.esAusenteNoJustificada()).isFalse();

            EstadoAsistencia retraso = new EstadoAsistencia(EstadoAsistencia.Valor.RETRASO);
            assertThat(retraso.esAusenteNoJustificada()).isFalse();
        }
    }

    @Nested
    class IgualdadYHashCode {
        @Test
        void deberianSerIgualesSiMismoValor() {
            EstadoAsistencia estado1 = new EstadoAsistencia(EstadoAsistencia.Valor.PRESENTE);
            EstadoAsistencia estado2 = new EstadoAsistencia(EstadoAsistencia.Valor.PRESENTE);
            assertThat(estado1).isEqualTo(estado2);
            assertThat(estado1.hashCode()).isEqualTo(estado2.hashCode());
        }

        @Test
        void noDeberianSerIgualesSiValorDiferente() {
            EstadoAsistencia estado1 = new EstadoAsistencia(EstadoAsistencia.Valor.PRESENTE);
            EstadoAsistencia estado2 = new EstadoAsistencia(EstadoAsistencia.Valor.AUSENTE);
            assertThat(estado1).isNotEqualTo(estado2);
        }

        @Test
        void noDeberiaSerIgualANull() {
            EstadoAsistencia estado = new EstadoAsistencia(EstadoAsistencia.Valor.PRESENTE);
            assertThat(estado).isNotEqualTo(null);
        }

        @Test
        void noDeberiaSerIgualAOtroTipo() {
            EstadoAsistencia estado = new EstadoAsistencia(EstadoAsistencia.Valor.PRESENTE);
            assertThat(estado).isNotEqualTo("PRESENTE");
        }
    }

    @Nested
    class RepresentacionComoString {
        @Test
        void toStringDeberiaIncluirValor() {
            EstadoAsistencia estado = new EstadoAsistencia(EstadoAsistencia.Valor.RETRASO);
            String resultado = estado.toString();
            assertThat(resultado).contains("RETRASO");
            assertThat(resultado).contains("EstadoAsistencia");
        }
    }
}