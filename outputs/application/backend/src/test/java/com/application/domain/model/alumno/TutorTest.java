package com.application.domain.model.alumno;

import com.application.domain.shared.Entity;
import com.application.domain.valueobject.alumno.DocumentoIdentidad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TutorTest {

    @Mock
    private DocumentoIdentidad mockDocumentoIdentidad;

    private Tutor tutor;

    @BeforeEach
    void setUp() {
        tutor = Tutor.crear(
                "Juan",
                "Pérez",
                mockDocumentoIdentidad,
                "+34123456789",
                "juan.perez@email.com",
                "Padre"
        );
    }

    @Test
    void crear_ConDatosValidos_RetornaInstancia() {
        assertNotNull(tutor);
        assertEquals("Juan", tutor.getNombre());
        assertEquals("Pérez", tutor.getApellidos());
        assertEquals(mockDocumentoIdentidad, tutor.getDocumentoIdentidad());
        assertEquals("+34123456789", tutor.getTelefono());
        assertEquals("juan.perez@email.com", tutor.getEmail());
        assertEquals("Padre", tutor.getRelacionConAlumno());
    }

    @Test
    void crear_ConSoloTelefono_RetornaInstancia() {
        Tutor tutorSoloTelefono = Tutor.crear(
                "Ana",
                "García",
                mockDocumentoIdentidad,
                "+34987654321",
                null,
                "Madre"
        );

        assertNotNull(tutorSoloTelefono);
        assertEquals("+34987654321", tutorSoloTelefono.getTelefono());
        assertNull(tutorSoloTelefono.getEmail());
    }

    @Test
    void crear_ConSoloEmail_RetornaInstancia() {
        Tutor tutorSoloEmail = Tutor.crear(
                "Luis",
                "López",
                mockDocumentoIdentidad,
                null,
                "luis.lopez@email.com",
                "Tutor Legal"
        );

        assertNotNull(tutorSoloEmail);
        assertNull(tutorSoloEmail.getTelefono());
        assertEquals("luis.lopez@email.com", tutorSoloEmail.getEmail());
    }

    @Test
    void crear_SinMetodoContacto_LanzaExcepcion() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                Tutor.crear(
                        "Pedro",
                        "Sánchez",
                        mockDocumentoIdentidad,
                        null,
                        null,
                        "Padre"
                )
        );
        assertTrue(exception.getMessage().contains("al menos un método de contacto"));
    }

    @Test
    void crear_ConDocumentoIdentidadNulo_LanzaExcepcion() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                Tutor.crear(
                        "Pedro",
                        "Sánchez",
                        null,
                        "+34111111111",
                        null,
                        "Padre"
                )
        );
        assertTrue(exception.getMessage().contains("DocumentoIdentidad"));
    }

    @Test
    void actualizarContacto_ConDatosValidos_ActualizaCampos() {
        tutor.actualizarContacto("+34999999999", "nuevo.email@email.com", "Abuelo");

        assertEquals("+34999999999", tutor.getTelefono());
        assertEquals("nuevo.email@email.com", tutor.getEmail());
        assertEquals("Abuelo", tutor.getRelacionConAlumno());
    }

    @Test
    void actualizarContacto_ConAmbosContactosNulos_LanzaExcepcion() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                tutor.actualizarContacto(null, null, "Padre")
        );
        assertTrue(exception.getMessage().contains("al menos un método de contacto"));
    }

    @Test
    void actualizarContacto_ConSoloTelefonoValido_Actualiza() {
        tutor.actualizarContacto("+34888888888", null, "Tío");

        assertEquals("+34888888888", tutor.getTelefono());
        assertNull(tutor.getEmail());
        assertEquals("Tío", tutor.getRelacionConAlumno());
    }

    @Test
    void actualizarContacto_ConSoloEmailValido_Actualiza() {
        tutor.actualizarContacto(null, "solo.email@email.com", "Hermano");

        assertNull(tutor.getTelefono());
        assertEquals("solo.email@email.com", tutor.getEmail());
        assertEquals("Hermano", tutor.getRelacionConAlumno());
    }

    @Test
    void equals_ConMismoDocumentoIdentidad_RetornaTrue() {
        when(mockDocumentoIdentidad.equals(mockDocumentoIdentidad)).thenReturn(true);
        Tutor otroTutor = Tutor.crear(
                "Nombre",
                "Diferente",
                mockDocumentoIdentidad,
                "tel",
                "email",
                "rel"
        );

        assertTrue(tutor.equals(otroTutor));
    }

    @Test
    void equals_ConDocumentoIdentidadDiferente_RetornaFalse() {
        DocumentoIdentidad otroDocumento = new DocumentoIdentidad("NIE", "X1234567X", "ES");
        when(mockDocumentoIdentidad.equals(otroDocumento)).thenReturn(false);
        Tutor otroTutor = Tutor.crear(
                "Juan",
                "Pérez",
                otroDocumento,
                "+34123456789",
                "juan.perez@email.com",
                "Padre"
        );

        assertFalse(tutor.equals(otroTutor));
    }

    @Test
    void hashCode_UsaDocumentoIdentidad() {
        int expectedHash = tutor.getDocumentoIdentidad().hashCode();
        assertEquals(expectedHash, tutor.hashCode());
    }

    @Test
    void extiendeEntity() {
        assertTrue(Entity.class.isAssignableFrom(Tutor.class));
    }

    @Test
    void tieneConstructorProtegidoSinArgs() {
        assertDoesNotThrow(() -> {
            Tutor.class.getDeclaredConstructor().newInstance();
        });
    }
}