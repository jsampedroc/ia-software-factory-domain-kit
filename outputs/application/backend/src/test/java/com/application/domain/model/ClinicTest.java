package com.application.domain.model;

import com.application.domain.valueobject.ClinicId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ClinicTest {

    private final ClinicId sampleId = new ClinicId(UUID.randomUUID());
    private final String sampleCodigo = "CLI-001";
    private final String sampleNombre = "Clínica Central";
    private final String sampleDireccion = "Calle Principal 123";
    private final String sampleTelefono = "555-1234";
    private final String sampleEmail = "central@clinica.com";
    private final LocalTime sampleApertura = LocalTime.of(8, 0);
    private final LocalTime sampleCierre = LocalTime.of(18, 0);

    @Test
    void create_ShouldReturnActiveClinic() {
        Clinic clinic = Clinic.create(sampleId, sampleCodigo, sampleNombre, sampleDireccion,
                sampleTelefono, sampleEmail, sampleApertura, sampleCierre);

        assertNotNull(clinic);
        assertEquals(sampleId, clinic.getId());
        assertEquals(sampleCodigo, clinic.getCodigo());
        assertEquals(sampleNombre, clinic.getNombre());
        assertEquals(sampleDireccion, clinic.getDireccion());
        assertEquals(sampleTelefono, clinic.getTelefono());
        assertEquals(sampleEmail, clinic.getEmail());
        assertEquals(sampleApertura, clinic.getHorarioApertura());
        assertEquals(sampleCierre, clinic.getHorarioCierre());
        assertTrue(clinic.getActiva());
    }

    @Test
    void restore_ShouldReturnClinicWithGivenState() {
        Clinic clinic = Clinic.restore(sampleId, sampleCodigo, sampleNombre, sampleDireccion,
                sampleTelefono, sampleEmail, sampleApertura, sampleCierre, false);

        assertNotNull(clinic);
        assertEquals(sampleId, clinic.getId());
        assertEquals(sampleCodigo, clinic.getCodigo());
        assertEquals(sampleNombre, clinic.getNombre());
        assertEquals(sampleDireccion, clinic.getDireccion());
        assertEquals(sampleTelefono, clinic.getTelefono());
        assertEquals(sampleEmail, clinic.getEmail());
        assertEquals(sampleApertura, clinic.getHorarioApertura());
        assertEquals(sampleCierre, clinic.getHorarioCierre());
        assertFalse(clinic.getActiva());
    }

    @Test
    void updateInformation_ShouldUpdateFields() {
        Clinic clinic = Clinic.create(sampleId, sampleCodigo, sampleNombre, sampleDireccion,
                sampleTelefono, sampleEmail, sampleApertura, sampleCierre);

        String nuevoNombre = "Clínica Renovada";
        String nuevaDireccion = "Avenida Nueva 456";
        String nuevoTelefono = "555-5678";
        String nuevoEmail = "nueva@clinica.com";
        LocalTime nuevaApertura = LocalTime.of(9, 0);
        LocalTime nuevoCierre = LocalTime.of(19, 0);

        clinic.updateInformation(nuevoNombre, nuevaDireccion, nuevoTelefono, nuevoEmail,
                nuevaApertura, nuevoCierre);

        assertEquals(nuevoNombre, clinic.getNombre());
        assertEquals(nuevaDireccion, clinic.getDireccion());
        assertEquals(nuevoTelefono, clinic.getTelefono());
        assertEquals(nuevoEmail, clinic.getEmail());
        assertEquals(nuevaApertura, clinic.getHorarioApertura());
        assertEquals(nuevoCierre, clinic.getHorarioCierre());
        assertEquals(sampleCodigo, clinic.getCodigo());
        assertTrue(clinic.getActiva());
    }

    @Test
    void updateInformation_WithNullValues_ShouldThrowException() {
        Clinic clinic = Clinic.create(sampleId, sampleCodigo, sampleNombre, sampleDireccion,
                sampleTelefono, sampleEmail, sampleApertura, sampleCierre);

        assertThrows(NullPointerException.class, () ->
                clinic.updateInformation(null, sampleDireccion, sampleTelefono,
                        sampleEmail, sampleApertura, sampleCierre));

        assertThrows(NullPointerException.class, () ->
                clinic.updateInformation(sampleNombre, null, sampleTelefono,
                        sampleEmail, sampleApertura, sampleCierre));

        assertThrows(NullPointerException.class, () ->
                clinic.updateInformation(sampleNombre, sampleDireccion, null,
                        sampleEmail, sampleApertura, sampleCierre));

        assertThrows(NullPointerException.class, () ->
                clinic.updateInformation(sampleNombre, sampleDireccion, sampleTelefono,
                        null, sampleApertura, sampleCierre));

        assertThrows(NullPointerException.class, () ->
                clinic.updateInformation(sampleNombre, sampleDireccion, sampleTelefono,
                        sampleEmail, null, sampleCierre));

        assertThrows(NullPointerException.class, () ->
                clinic.updateInformation(sampleNombre, sampleDireccion, sampleTelefono,
                        sampleEmail, sampleApertura, null));
    }

    @Test
    void activate_ShouldSetActivaToTrue() {
        Clinic clinic = Clinic.restore(sampleId, sampleCodigo, sampleNombre, sampleDireccion,
                sampleTelefono, sampleEmail, sampleApertura, sampleCierre, false);

        clinic.activate();

        assertTrue(clinic.getActiva());
    }

    @Test
    void deactivate_ShouldSetActivaToFalse() {
        Clinic clinic = Clinic.create(sampleId, sampleCodigo, sampleNombre, sampleDireccion,
                sampleTelefono, sampleEmail, sampleApertura, sampleCierre);

        clinic.deactivate();

        assertFalse(clinic.getActiva());
    }

    @Test
    void isOperationalAt_WhenActiveAndWithinHours_ShouldReturnTrue() {
        Clinic clinic = Clinic.create(sampleId, sampleCodigo, sampleNombre, sampleDireccion,
                sampleTelefono, sampleEmail, sampleApertura, sampleCierre);

        assertTrue(clinic.isOperationalAt(LocalTime.of(10, 0)));
        assertTrue(clinic.isOperationalAt(sampleApertura));
        assertTrue(clinic.isOperationalAt(sampleCierre));
    }

    @Test
    void isOperationalAt_WhenInactive_ShouldReturnFalse() {
        Clinic clinic = Clinic.restore(sampleId, sampleCodigo, sampleNombre, sampleDireccion,
                sampleTelefono, sampleEmail, sampleApertura, sampleCierre, false);

        assertFalse(clinic.isOperationalAt(LocalTime.of(10, 0)));
    }

    @Test
    void isOperationalAt_WhenOutsideHours_ShouldReturnFalse() {
        Clinic clinic = Clinic.create(sampleId, sampleCodigo, sampleNombre, sampleDireccion,
                sampleTelefono, sampleEmail, sampleApertura, sampleCierre);

        assertFalse(clinic.isOperationalAt(LocalTime.of(7, 59)));
        assertFalse(clinic.isOperationalAt(LocalTime.of(18, 1)));
    }

    @Test
    void isOperationalAt_WithNullTime_ShouldThrowException() {
        Clinic clinic = Clinic.create(sampleId, sampleCodigo, sampleNombre, sampleDireccion,
                sampleTelefono, sampleEmail, sampleApertura, sampleCierre);

        assertThrows(NullPointerException.class, () -> clinic.isOperationalAt(null));
    }

    @Test
    void validate_WithInvalidData_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                Clinic.create(sampleId, null, sampleNombre, sampleDireccion,
                        sampleTelefono, sampleEmail, sampleApertura, sampleCierre));

        assertThrows(IllegalArgumentException.class, () ->
                Clinic.create(sampleId, "", sampleNombre, sampleDireccion,
                        sampleTelefono, sampleEmail, sampleApertura, sampleCierre));

        assertThrows(IllegalArgumentException.class, () ->
                Clinic.create(sampleId, sampleCodigo, null, sampleDireccion,
                        sampleTelefono, sampleEmail, sampleApertura, sampleCierre));

        assertThrows(IllegalArgumentException.class, () ->
                Clinic.create(sampleId, sampleCodigo, "", sampleDireccion,
                        sampleTelefono, sampleEmail, sampleApertura, sampleCierre));

        assertThrows(IllegalArgumentException.class, () ->
                Clinic.create(sampleId, sampleCodigo, sampleNombre, null,
                        sampleTelefono, sampleEmail, sampleApertura, sampleCierre));

        assertThrows(IllegalArgumentException.class, () ->
                Clinic.create(sampleId, sampleCodigo, sampleNombre, "",
                        sampleTelefono, sampleEmail, sampleApertura, sampleCierre));

        assertThrows(IllegalArgumentException.class, () ->
                Clinic.create(sampleId, sampleCodigo, sampleNombre, sampleDireccion,
                        null, sampleEmail, sampleApertura, sampleCierre));

        assertThrows(IllegalArgumentException.class, () ->
                Clinic.create(sampleId, sampleCodigo, sampleNombre, sampleDireccion,
                        "", sampleEmail, sampleApertura, sampleCierre));

        assertThrows(IllegalArgumentException.class, () ->
                Clinic.create(sampleId, sampleCodigo, sampleNombre, sampleDireccion,
                        sampleTelefono, null, sampleApertura, sampleCierre));

        assertThrows(IllegalArgumentException.class, () ->
                Clinic.create(sampleId, sampleCodigo, sampleNombre, sampleDireccion,
                        sampleTelefono, "", sampleApertura, sampleCierre));

        assertThrows(IllegalArgumentException.class, () ->
                Clinic.create(sampleId, sampleCodigo, sampleNombre, sampleDireccion,
                        sampleTelefono, sampleEmail, null, sampleCierre));

        assertThrows(IllegalArgumentException.class, () ->
                Clinic.create(sampleId, sampleCodigo, sampleNombre, sampleDireccion,
                        sampleTelefono, sampleEmail, sampleApertura, null));

        LocalTime cierreInvalido = LocalTime.of(8, 0);
        assertThrows(IllegalArgumentException.class, () ->
                Clinic.create(sampleId, sampleCodigo, sampleNombre, sampleDireccion,
                        sampleTelefono, sampleEmail, cierreInvalido, cierreInvalido));

        LocalTime cierreAntes = LocalTime.of(7, 0);
        assertThrows(IllegalArgumentException.class, () ->
                Clinic.create(sampleId, sampleCodigo, sampleNombre, sampleDireccion,
                        sampleTelefono, sampleEmail, sampleApertura, cierreAntes));
    }

    @Test
    void validate_WithNullActivaInRestore_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () ->
                Clinic.restore(sampleId, sampleCodigo, sampleNombre, sampleDireccion,
                        sampleTelefono, sampleEmail, sampleApertura, sampleCierre, null));
    }
}