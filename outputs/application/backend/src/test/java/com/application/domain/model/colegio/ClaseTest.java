package com.application.domain.model.colegio;

import com.application.domain.model.colegio.clase.ClaseId;
import com.application.domain.shared.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class ClaseTest {

    private Clase clase;
    private final String nombre = "1ºA Primaria";
    private final String nivelEducativo = "PRIMARIA";
    private final String anoAcademico = "2024-2025";
    private final int capacidadMaxima = 25;

    @BeforeEach
    void setUp() {
        clase = Clase.crear(nombre, nivelEducativo, anoAcademico, capacidadMaxima);
    }

    @Test
    void crear_ConDatosValidos_CreaClaseConEstadoCorrecto() {
        assertThat(clase).isNotNull();
        assertThat(clase.getId()).isNotNull();
        assertThat(clase.getNombre()).isEqualTo(nombre);
        assertThat(clase.getNivelEducativo()).isEqualTo(nivelEducativo);
        assertThat(clase.getAnoAcademico()).isEqualTo(anoAcademico);
        assertThat(clase.getCapacidadMaxima()).isEqualTo(capacidadMaxima);
    }

    @Test
    void crear_ConNombreNulo_LanzaExcepcion() {
        assertThatThrownBy(() -> Clase.crear(null, nivelEducativo, anoAcademico, capacidadMaxima))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("nombre");
    }

    @Test
    void crear_ConNombreVacio_LanzaExcepcion() {
        assertThatThrownBy(() -> Clase.crear("", nivelEducativo, anoAcademico, capacidadMaxima))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nombre");
    }

    @Test
    void crear_ConNivelEducativoNulo_LanzaExcepcion() {
        assertThatThrownBy(() -> Clase.crear(nombre, null, anoAcademico, capacidadMaxima))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("nivelEducativo");
    }

    @Test
    void crear_ConAnoAcademicoNulo_LanzaExcepcion() {
        assertThatThrownBy(() -> Clase.crear(nombre, nivelEducativo, null, capacidadMaxima))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("anoAcademico");
    }

    @Test
    void crear_ConAnoAcademicoFormatoInvalido_LanzaExcepcion() {
        assertThatThrownBy(() -> Clase.crear(nombre, nivelEducativo, "2024/2025", capacidadMaxima))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("anoAcademico");
    }

    @Test
    void crear_ConCapacidadMaximaCero_LanzaExcepcion() {
        assertThatThrownBy(() -> Clase.crear(nombre, nivelEducativo, anoAcademico, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("capacidadMaxima");
    }

    @Test
    void crear_ConCapacidadMaximaNegativa_LanzaExcepcion() {
        assertThatThrownBy(() -> Clase.crear(nombre, nivelEducativo, anoAcademico, -5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("capacidadMaxima");
    }

    @Test
    void cambiarNombre_ConNuevoNombreValido_ActualizaNombre() {
        String nuevoNombre = "1ºB Primaria";
        clase.cambiarNombre(nuevoNombre);
        assertThat(clase.getNombre()).isEqualTo(nuevoNombre);
    }

    @Test
    void cambiarNombre_ConNombreNulo_LanzaExcepcion() {
        assertThatThrownBy(() -> clase.cambiarNombre(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("nombre");
    }

    @Test
    void cambiarNombre_ConNombreVacio_LanzaExcepcion() {
        assertThatThrownBy(() -> clase.cambiarNombre(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nombre");
    }

    @Test
    void cambiarCapacidadMaxima_ConNuevaCapacidadValida_ActualizaCapacidad() {
        int nuevaCapacidad = 30;
        clase.cambiarCapacidadMaxima(nuevaCapacidad);
        assertThat(clase.getCapacidadMaxima()).isEqualTo(nuevaCapacidad);
    }

    @Test
    void cambiarCapacidadMaxima_ConCapacidadCero_LanzaExcepcion() {
        assertThatThrownBy(() -> clase.cambiarCapacidadMaxima(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("capacidadMaxima");
    }

    @Test
    void cambiarCapacidadMaxima_ConCapacidadNegativa_LanzaExcepcion() {
        assertThatThrownBy(() -> clase.cambiarCapacidadMaxima(-10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("capacidadMaxima");
    }

    @Test
    void equals_ConMismoId_DevuelveTrue() {
        ClaseId id = clase.getId();
        Clase otraClase = new Clase() {
            @Override
            public ClaseId getId() {
                return id;
            }
        };
        assertThat(clase.equals(otraClase)).isTrue();
    }

    @Test
    void equals_ConIdDiferente_DevuelveFalse() {
        Clase otraClase = Clase.crear("Otra Clase", "SECUNDARIA", "2024-2025", 20);
        assertThat(clase.equals(otraClase)).isFalse();
    }

    @Test
    void hashCode_ConMismoId_DevuelveMismoHashCode() {
        ClaseId id = clase.getId();
        int expectedHashCode = id.hashCode();
        assertThat(clase.hashCode()).isEqualTo(expectedHashCode);
    }

    @Test
    void testClaseIdRecord() {
        ClaseId id1 = new ClaseId(java.util.UUID.randomUUID());
        ClaseId id2 = new ClaseId(id1.value());
        assertThat(id1).isEqualTo(id2);
        assertThat(id1.value()).isEqualTo(id2.value());
    }

    @Test
    void testClaseIdValueObject() {
        ClaseId id = new ClaseId(java.util.UUID.randomUUID());
        assertThat(id).isInstanceOf(com.application.domain.shared.ValueObject.class);
    }

    @Test
    void testClaseExtendsEntity() {
        assertThat(clase).isInstanceOf(Entity.class);
    }
}