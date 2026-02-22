package com.application.domain.valueobject.alumno;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DireccionTest {

    @Test
    void crearDireccion_ConDatosValidos_DeberiaInstanciarse() {
        String linea1 = "Calle Principal 123";
        String linea2 = "Escalera B, 2ºA";
        String codigoPostal = "28001";
        String ciudad = "Madrid";
        String provincia = "Madrid";

        Direccion direccion = new Direccion(linea1, linea2, codigoPostal, ciudad, provincia);

        assertNotNull(direccion);
        assertEquals(linea1, direccion.linea1());
        assertEquals(linea2, direccion.linea2());
        assertEquals(codigoPostal, direccion.codigoPostal());
        assertEquals(ciudad, direccion.ciudad());
        assertEquals(provincia, direccion.provincia());
    }

    @Test
    void crearDireccion_ConLinea2Nula_DeberiaInstanciarse() {
        String linea1 = "Avenida Secundaria 456";
        String codigoPostal = "08001";
        String ciudad = "Barcelona";
        String provincia = "Barcelona";

        Direccion direccion = new Direccion(linea1, null, codigoPostal, ciudad, provincia);

        assertNotNull(direccion);
        assertEquals(linea1, direccion.linea1());
        assertNull(direccion.linea2());
        assertEquals(codigoPostal, direccion.codigoPostal());
        assertEquals(ciudad, direccion.ciudad());
        assertEquals(provincia, direccion.provincia());
    }

    @Test
    void crearDireccion_ConLinea1Nula_DeberiaLanzarExcepcion() {
        String codigoPostal = "41001";
        String ciudad = "Sevilla";
        String provincia = "Sevilla";

        assertThrows(NullPointerException.class,
                () -> new Direccion(null, "Linea 2", codigoPostal, ciudad, provincia));
    }

    @Test
    void crearDireccion_ConCodigoPostalNulo_DeberiaLanzarExcepcion() {
        String linea1 = "Plaza Mayor 1";

        assertThrows(NullPointerException.class,
                () -> new Direccion(linea1, null, null, "Ciudad", "Provincia"));
    }

    @Test
    void crearDireccion_ConCiudadNula_DeberiaLanzarExcepcion() {
        String linea1 = "Calle Sin Nombre 7";
        String codigoPostal = "50001";

        assertThrows(NullPointerException.class,
                () -> new Direccion(linea1, null, codigoPostal, null, "Provincia"));
    }

    @Test
    void crearDireccion_ConProvinciaNula_DeberiaLanzarExcepcion() {
        String linea1 = "Calle Sin Nombre 7";
        String codigoPostal = "50001";
        String ciudad = "Zaragoza";

        assertThrows(NullPointerException.class,
                () -> new Direccion(linea1, null, codigoPostal, ciudad, null));
    }

    @Test
    void equals_ConMismosValores_DeberiaSerIgual() {
        Direccion direccion1 = new Direccion("Calle A", "Piso 1", "28001", "Madrid", "Madrid");
        Direccion direccion2 = new Direccion("Calle A", "Piso 1", "28001", "Madrid", "Madrid");

        assertEquals(direccion1, direccion2);
        assertEquals(direccion1.hashCode(), direccion2.hashCode());
    }

    @Test
    void equals_ConValoresDiferentes_DeberiaSerDiferente() {
        Direccion direccion1 = new Direccion("Calle A", "Piso 1", "28001", "Madrid", "Madrid");
        Direccion direccion2 = new Direccion("Calle B", "Piso 1", "28001", "Madrid", "Madrid");

        assertNotEquals(direccion1, direccion2);
        assertNotEquals(direccion1.hashCode(), direccion2.hashCode());
    }

    @Test
    void equals_ConLinea2Diferente_DeberiaSerDiferente() {
        Direccion direccion1 = new Direccion("Calle A", "Piso 1", "28001", "Madrid", "Madrid");
        Direccion direccion2 = new Direccion("Calle A", "Piso 2", "28001", "Madrid", "Madrid");

        assertNotEquals(direccion1, direccion2);
        assertNotEquals(direccion1.hashCode(), direccion2.hashCode());
    }

    @Test
    void equals_ConLinea2NullEnUno_DeberiaSerDiferente() {
        Direccion direccion1 = new Direccion("Calle A", null, "28001", "Madrid", "Madrid");
        Direccion direccion2 = new Direccion("Calle A", "Piso 1", "28001", "Madrid", "Madrid");

        assertNotEquals(direccion1, direccion2);
        assertNotEquals(direccion1.hashCode(), direccion2.hashCode());
    }

    @Test
    void toString_DeberiaContenerValores() {
        Direccion direccion = new Direccion("Calle Test", "Bloque 3", "12345", "TestCity", "TestProv");

        String result = direccion.toString();

        assertTrue(result.contains("Calle Test"));
        assertTrue(result.contains("Bloque 3"));
        assertTrue(result.contains("12345"));
        assertTrue(result.contains("TestCity"));
        assertTrue(result.contains("TestProv"));
    }
}