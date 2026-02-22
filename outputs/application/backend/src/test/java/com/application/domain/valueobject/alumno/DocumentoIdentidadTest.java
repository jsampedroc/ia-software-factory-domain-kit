package com.application.domain.valueobject.alumno;

import com.application.domain.shared.ValueObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DocumentoIdentidadTest {

    @Test
    void crearDocumentoIdentidad_ConParametrosValidos_DeberiaCrearInstancia() {
        DocumentoIdentidad.Tipo tipo = DocumentoIdentidad.Tipo.DNI;
        String numero = "12345678A";
        String paisEmision = "ES";

        DocumentoIdentidad doc = new DocumentoIdentidad(tipo, numero, paisEmision);

        assertNotNull(doc);
        assertEquals(tipo, doc.tipo());
        assertEquals(numero, doc.numero());
        assertEquals(paisEmision, doc.paisEmision());
    }

    @Test
    void crearDocumentoIdentidad_ConNumeroNulo_DeberiaLanzarExcepcion() {
        DocumentoIdentidad.Tipo tipo = DocumentoIdentidad.Tipo.PASAPORTE;
        String paisEmision = "US";

        assertThrows(NullPointerException.class, () -> new DocumentoIdentidad(tipo, null, paisEmision));
    }

    @Test
    void crearDocumentoIdentidad_ConPaisEmisionNulo_DeberiaLanzarExcepcion() {
        DocumentoIdentidad.Tipo tipo = DocumentoIdentidad.Tipo.NIE;
        String numero = "X1234567A";

        assertThrows(NullPointerException.class, () -> new DocumentoIdentidad(tipo, numero, null));
    }

    @Test
    void crearDocumentoIdentidad_ConTipoNulo_DeberiaLanzarExcepcion() {
        String numero = "98765432B";
        String paisEmision = "ES";

        assertThrows(NullPointerException.class, () -> new DocumentoIdentidad(null, numero, paisEmision));
    }

    @Test
    void crearDocumentoIdentidad_ConNumeroVacio_DeberiaLanzarExcepcion() {
        DocumentoIdentidad.Tipo tipo = DocumentoIdentidad.Tipo.DNI;
        String paisEmision = "ES";

        assertThrows(IllegalArgumentException.class, () -> new DocumentoIdentidad(tipo, "", paisEmision));
        assertThrows(IllegalArgumentException.class, () -> new DocumentoIdentidad(tipo, "   ", paisEmision));
    }

    @Test
    void crearDocumentoIdentidad_ConPaisEmisionVacio_DeberiaLanzarExcepcion() {
        DocumentoIdentidad.Tipo tipo = DocumentoIdentidad.Tipo.DNI;
        String numero = "12345678Z";

        assertThrows(IllegalArgumentException.class, () -> new DocumentoIdentidad(tipo, numero, ""));
        assertThrows(IllegalArgumentException.class, () -> new DocumentoIdentidad(tipo, numero, "  "));
    }

    @Test
    void equals_ConMismosValores_DeberiaSerIgual() {
        DocumentoIdentidad doc1 = new DocumentoIdentidad(DocumentoIdentidad.Tipo.DNI, "12345678A", "ES");
        DocumentoIdentidad doc2 = new DocumentoIdentidad(DocumentoIdentidad.Tipo.DNI, "12345678A", "ES");

        assertEquals(doc1, doc2);
        assertEquals(doc1.hashCode(), doc2.hashCode());
    }

    @Test
    void equals_ConValoresDiferentes_DeberiaSerDiferente() {
        DocumentoIdentidad doc1 = new DocumentoIdentidad(DocumentoIdentidad.Tipo.DNI, "12345678A", "ES");
        DocumentoIdentidad doc2 = new DocumentoIdentidad(DocumentoIdentidad.Tipo.NIE, "X1234567A", "ES");
        DocumentoIdentidad doc3 = new DocumentoIdentidad(DocumentoIdentidad.Tipo.DNI, "87654321B", "ES");
        DocumentoIdentidad doc4 = new DocumentoIdentidad(DocumentoIdentidad.Tipo.DNI, "12345678A", "PT");

        assertNotEquals(doc1, doc2);
        assertNotEquals(doc1, doc3);
        assertNotEquals(doc1, doc4);
    }

    @Test
    void equals_ConMismoObjeto_DeberiaSerIgual() {
        DocumentoIdentidad doc = new DocumentoIdentidad(DocumentoIdentidad.Tipo.PASAPORTE, "PA123456", "US");
        assertEquals(doc, doc);
    }

    @Test
    void equals_ConObjetoNulo_DeberiaSerDiferente() {
        DocumentoIdentidad doc = new DocumentoIdentidad(DocumentoIdentidad.Tipo.DNI, "12345678Z", "ES");
        assertNotEquals(null, doc);
    }

    @Test
    void equals_ConClaseDiferente_DeberiaSerDiferente() {
        DocumentoIdentidad doc = new DocumentoIdentidad(DocumentoIdentidad.Tipo.DNI, "12345678Z", "ES");
        String otroObjeto = "12345678Z";
        assertNotEquals(doc, otroObjeto);
    }

    @Test
    void implementaValueObject_DeberiaSerVerdadero() {
        DocumentoIdentidad doc = new DocumentoIdentidad(DocumentoIdentidad.Tipo.DNI, "12345678Z", "ES");
        assertTrue(doc instanceof ValueObject);
    }

    @Test
    void valoresDeTipo_DeberianEstarDefinidos() {
        assertNotNull(DocumentoIdentidad.Tipo.DNI);
        assertNotNull(DocumentoIdentidad.Tipo.NIE);
        assertNotNull(DocumentoIdentidad.Tipo.PASAPORTE);
        assertEquals(3, DocumentoIdentidad.Tipo.values().length);
    }
}