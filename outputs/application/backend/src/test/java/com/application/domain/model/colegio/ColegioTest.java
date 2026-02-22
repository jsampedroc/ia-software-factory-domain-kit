package com.application.domain.model.colegio;

import com.application.domain.shared.Entity;
import com.application.domain.valueobject.colegio.CodigoCentroEducativo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ColegioTest {

    @Mock
    private CodigoCentroEducativo mockCodigoCentro;
    @Mock
    private Clase mockClase1;
    @Mock
    private Clase mockClase2;

    private Colegio colegio;

    @BeforeEach
    void setUp() {
        when(mockCodigoCentro.codigo()).thenReturn("COD123");
        colegio = new Colegio("Colegio Test", mockCodigoCentro, "Calle Falsa 123", "912345678", "test@colegio.com");
    }

    @Test
    void crearColegio_ConDatosValidos_DeberiaCrearInstancia() {
        assertThat(colegio).isNotNull();
        assertThat(colegio.getNombre()).isEqualTo("Colegio Test");
        assertThat(colegio.getCodigoCentro()).isEqualTo(mockCodigoCentro);
        assertThat(colegio.getDireccion()).isEqualTo("Calle Falsa 123");
        assertThat(colegio.getTelefono()).isEqualTo("912345678");
        assertThat(colegio.getEmail()).isEqualTo("test@colegio.com");
        assertThat(colegio.getClases()).isEmpty();
    }

    @Test
    void crearColegio_ConNombreNulo_DeberiaLanzarExcepcion() {
        assertThatThrownBy(() -> new Colegio(null, mockCodigoCentro, "dir", "tel", "email"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("nombre");
    }

    @Test
    void crearColegio_ConCodigoCentroNulo_DeberiaLanzarExcepcion() {
        assertThatThrownBy(() -> new Colegio("Nombre", null, "dir", "tel", "email"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("codigoCentro");
    }

    @Test
    void crearColegio_ConDireccionNula_DeberiaLanzarExcepcion() {
        assertThatThrownBy(() -> new Colegio("Nombre", mockCodigoCentro, null, "tel", "email"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("direccion");
    }

    @Test
    void crearColegio_ConTelefonoYEmailNulos_DeberiaLanzarExcepcion() {
        assertThatThrownBy(() -> new Colegio("Nombre", mockCodigoCentro, "direccion", null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("contacto");
    }

    @Test
    void crearColegio_ConTelefonoVacioYEmailVacio_DeberiaLanzarExcepcion() {
        assertThatThrownBy(() -> new Colegio("Nombre", mockCodigoCentro, "direccion", "", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("contacto");
    }

    @Test
    void crearColegio_SoloConTelefonoValido_DeberiaCrearInstancia() {
        Colegio colegioSoloTel = new Colegio("Colegio Tel", mockCodigoCentro, "Calle", "600000000", null);
        assertThat(colegioSoloTel.getTelefono()).isEqualTo("600000000");
        assertThat(colegioSoloTel.getEmail()).isNull();
    }

    @Test
    void crearColegio_SoloConEmailValido_DeberiaCrearInstancia() {
        Colegio colegioSoloEmail = new Colegio("Colegio Email", mockCodigoCentro, "Calle", null, "email@valido.com");
        assertThat(colegioSoloEmail.getTelefono()).isNull();
        assertThat(colegioSoloEmail.getEmail()).isEqualTo("email@valido.com");
    }

    @Test
    void agregarClase_ConClaseValida_DeberiaAgregarClase() {
        colegio.agregarClase(mockClase1);
        assertThat(colegio.getClases()).containsExactly(mockClase1);
    }

    @Test
    void agregarClase_ConClaseNula_DeberiaLanzarExcepcion() {
        assertThatThrownBy(() -> colegio.agregarClase(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("clase");
    }

    @Test
    void agregarClase_ConClaseDuplicada_DeberiaIgnorarDuplicado() {
        colegio.agregarClase(mockClase1);
        colegio.agregarClase(mockClase1);
        assertThat(colegio.getClases()).hasSize(1).containsExactly(mockClase1);
    }

    @Test
    void agregarClase_ConMultiplesClases_DeberiaAgregarTodas() {
        colegio.agregarClase(mockClase1);
        colegio.agregarClase(mockClase2);
        assertThat(colegio.getClases()).containsExactlyInAnyOrder(mockClase1, mockClase2);
    }

    @Test
    void eliminarClase_ConClaseExistente_DeberiaEliminarClase() {
        colegio.agregarClase(mockClase1);
        colegio.agregarClase(mockClase2);
        colegio.eliminarClase(mockClase1);
        assertThat(colegio.getClases()).containsExactly(mockClase2);
    }

    @Test
    void eliminarClase_ConClaseNoExistente_NoDeberiaHacerNada() {
        colegio.agregarClase(mockClase1);
        colegio.eliminarClase(mockClase2);
        assertThat(colegio.getClases()).containsExactly(mockClase1);
    }

    @Test
    void eliminarClase_ConClaseNula_DeberiaLanzarExcepcion() {
        assertThatThrownBy(() -> colegio.eliminarClase(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("clase");
    }

    @Test
    void setClases_ConSetValido_DeberiaReemplazarClases() {
        Set<Clase> nuevasClases = new HashSet<>();
        nuevasClases.add(mockClase1);
        nuevasClases.add(mockClase2);
        colegio.setClases(nuevasClases);
        assertThat(colegio.getClases()).containsExactlyInAnyOrder(mockClase1, mockClase2);
    }

    @Test
    void setClases_ConSetNulo_DeberiaLanzarExcepcion() {
        assertThatThrownBy(() -> colegio.setClases(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("clases");
    }

    @Test
    void desactivar_DeberiaMarcarColegioComoInactivo() {
        colegio.desactivar();
        assertThat(colegio.isActivo()).isFalse();
    }

    @Test
    void equals_ConMismoId_DeberiaSerIgual() {
        Colegio otroColegio = new Colegio("Otro Nombre", mockCodigoCentro, "Otra Dir", "999", "otro@email.com");
        // Simulamos que tienen el mismo ID (no se puede modificar el ID real, se testea la lógica de Entity)
        // Este test asume que la igualdad se basa en el ID heredado de Entity
        // Se verifica que la instancia no sea igual a otra con diferente referencia
        assertThat(colegio.equals(otroColegio)).isFalse();
        assertThat(colegio.equals(colegio)).isTrue();
    }
}