package com.application.infrastructure.entity;

import com.application.domain.valueobject.PatientId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PatientEntityTest {

    @Test
    void shouldCreatePatientEntityWithAllAttributes() {
        // Given
        UUID uuid = UUID.randomUUID();
        PatientId patientId = PatientId.of(uuid);
        String dni = "12345678A";
        String nombre = "Juan";
        String apellido = "Pérez";
        LocalDate fechaNacimiento = LocalDate.of(1990, 5, 15);
        String telefono = "+34123456789";
        String email = "juan.perez@example.com";
        String direccion = "Calle Falsa 123, Madrid";
        LocalDateTime fechaRegistro = LocalDateTime.now();
        Boolean activo = true;

        // When
        PatientEntity patient = new PatientEntity();
        patient.setPatientId(patientId);
        // Usamos reflexión para simular la inyección de valores en un test de entidad.
        // En un escenario real, estos valores se establecerían a través de un constructor o builder.
        // Para este test, asumimos que los setters están disponibles o usamos reflexión.
        // Dado que la entidad tiene @Getter y @NoArgsConstructor(access = PROTECTED),
        // y no hay métodos de fábrica visibles en el código proporcionado,
        // probamos los getters y el mapeo del ID.
        // Vamos a usar Java Reflection para establecer los campos privados para la prueba.
        try {
            var idField = PatientEntity.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(patient, uuid);

            var dniField = PatientEntity.class.getDeclaredField("dni");
            dniField.setAccessible(true);
            dniField.set(patient, dni);

            var nombreField = PatientEntity.class.getDeclaredField("nombre");
            nombreField.setAccessible(true);
            nombreField.set(patient, nombre);

            var apellidoField = PatientEntity.class.getDeclaredField("apellido");
            apellidoField.setAccessible(true);
            apellidoField.set(patient, apellido);

            var fechaNacimientoField = PatientEntity.class.getDeclaredField("fechaNacimiento");
            fechaNacimientoField.setAccessible(true);
            fechaNacimientoField.set(patient, fechaNacimiento);

            var telefonoField = PatientEntity.class.getDeclaredField("telefono");
            telefonoField.setAccessible(true);
            telefonoField.set(patient, telefono);

            var emailField = PatientEntity.class.getDeclaredField("email");
            emailField.setAccessible(true);
            emailField.set(patient, email);

            var direccionField = PatientEntity.class.getDeclaredField("direccion");
            direccionField.setAccessible(true);
            direccionField.set(patient, direccion);

            var fechaRegistroField = PatientEntity.class.getDeclaredField("fechaRegistro");
            fechaRegistroField.setAccessible(true);
            fechaRegistroField.set(patient, fechaRegistro);

            var activoField = PatientEntity.class.getDeclaredField("activo");
            activoField.setAccessible(true);
            activoField.set(patient, activo);

        } catch (Exception e) {
            throw new RuntimeException("Error setting field via reflection", e);
        }

        // Then
        assertThat(patient.getPatientId()).isEqualTo(patientId);
        assertThat(patient.getDni()).isEqualTo(dni);
        assertThat(patient.getNombre()).isEqualTo(nombre);
        assertThat(patient.getApellido()).isEqualTo(apellido);
        assertThat(patient.getFechaNacimiento()).isEqualTo(fechaNacimiento);
        assertThat(patient.getTelefono()).isEqualTo(telefono);
        assertThat(patient.getEmail()).isEqualTo(email);
        assertThat(patient.getDireccion()).isEqualTo(direccion);
        assertThat(patient.getFechaRegistro()).isEqualTo(fechaRegistro);
        assertThat(patient.getActivo()).isEqualTo(activo);
        // Las colecciones deben estar inicializadas
        assertThat(patient.getAppointments()).isNotNull().isEmpty();
        assertThat(patient.getInvoices()).isNotNull().isEmpty();
        assertThat(patient.getTreatmentPlans()).isNotNull().isEmpty();
        // MedicalHistory es lazy y puede ser null inicialmente
        assertThat(patient.getMedicalHistory()).isNull();
    }

    @Test
    void getPatientId_shouldReturnPatientIdObject() {
        // Given
        UUID uuid = UUID.randomUUID();
        PatientId expectedPatientId = PatientId.of(uuid);
        PatientEntity patient = new PatientEntity();
        patient.setPatientId(expectedPatientId);

        // When
        PatientId actualPatientId = patient.getPatientId();

        // Then
        assertThat(actualPatientId).isEqualTo(expectedPatientId);
        assertThat(actualPatientId.getValue()).isEqualTo(uuid);
    }

    @Test
    void setPatientId_withNull_shouldSetIdToNull() {
        // Given
        PatientEntity patient = new PatientEntity();
        UUID initialUuid = UUID.randomUUID();
        patient.setPatientId(PatientId.of(initialUuid));
        assertThat(patient.getPatientId()).isNotNull();

        // When
        patient.setPatientId(null);

        // Then
        // Verificamos a través del getter que el ID es null
        // Como getPatientId() devuelve PatientId.of(id), y id es null, puede lanzar una excepción.
        // Dependiendo de la implementación de PatientId.of, podría lanzar NullPointerException.
        // Para este test, asumimos que setPatientId(null) establece el campo id a null.
        try {
            var idField = PatientEntity.class.getDeclaredField("id");
            idField.setAccessible(true);
            UUID idValue = (UUID) idField.get(patient);
            assertThat(idValue).isNull();
        } catch (Exception e) {
            throw new RuntimeException("Error accessing field via reflection", e);
        }
    }

    @Test
    void collections_shouldBeInitializedAsEmpty() {
        // Given
        PatientEntity patient = new PatientEntity();

        // Then
        assertThat(patient.getAppointments()).isNotNull().isEmpty();
        assertThat(patient.getInvoices()).isNotNull().isEmpty();
        assertThat(patient.getTreatmentPlans()).isNotNull().isEmpty();
    }
}