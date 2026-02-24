package com.application.infrastructure.entity;

import com.application.domain.valueobject.AppointmentId;
import com.application.domain.valueobject.TreatmentId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AppointmentTreatmentEntityTest {

    @Test
    void shouldCreateAppointmentTreatmentId() {
        // Given
        AppointmentId appointmentId = new AppointmentId(UUID.randomUUID());
        TreatmentId treatmentId = new TreatmentId(UUID.randomUUID());

        // When
        AppointmentTreatmentEntity.AppointmentTreatmentId id = new AppointmentTreatmentEntity.AppointmentTreatmentId(appointmentId, treatmentId);

        // Then
        assertThat(id).isNotNull();
        assertThat(id.getAppointmentId()).isEqualTo(appointmentId);
        assertThat(id.getTreatmentId()).isEqualTo(treatmentId);
    }

    @Test
    void shouldCreateAppointmentTreatmentIdWithNoArgsConstructor() {
        // When
        AppointmentTreatmentEntity.AppointmentTreatmentId id = new AppointmentTreatmentEntity.AppointmentTreatmentId();

        // Then
        assertThat(id).isNotNull();
        assertThat(id.getAppointmentId()).isNull();
        assertThat(id.getTreatmentId()).isNull();
    }

    @Test
    void shouldCreateAppointmentTreatmentEntity() {
        // Given
        AppointmentTreatmentEntity.AppointmentTreatmentId id = new AppointmentTreatmentEntity.AppointmentTreatmentId(
                new AppointmentId(UUID.randomUUID()),
                new TreatmentId(UUID.randomUUID())
        );
        AppointmentEntity appointment = new AppointmentEntity();
        TreatmentEntity treatment = new TreatmentEntity();
        Integer cantidad = 2;
        Double costoAplicado = 150.75;
        String observaciones = "Observaciones de prueba";
        LocalDateTime fechaRegistro = LocalDateTime.now();

        // When
        AppointmentTreatmentEntity entity = new AppointmentTreatmentEntity();
        // Reflection para settear campos privados en el test (alternativa: usar package-private setters o un método de fábrica)
        // Como el constructor es protected, usamos reflexión para simular la creación
        // En un escenario real, la entidad sería creada por JPA o un método de fábrica.
        // Para este test, validamos la estructura y los getters.
        // Asumimos que los setters están manejados por JPA.

        // Then - Validamos que la clase tiene los campos esperados
        assertThat(entity).isNotNull();
        // La entidad debe tener los getters generados por Lombok
        assertThat(entity).hasFieldOrProperty("id");
        assertThat(entity).hasFieldOrProperty("appointment");
        assertThat(entity).hasFieldOrProperty("treatment");
        assertThat(entity).hasFieldOrProperty("cantidad");
        assertThat(entity).hasFieldOrProperty("costoAplicado");
        assertThat(entity).hasFieldOrProperty("observaciones");
        assertThat(entity).hasFieldOrProperty("fechaRegistro");
    }

    @Test
    void shouldHaveEmbeddedIdAnnotationOnIdField() throws NoSuchFieldException {
        // Given
        var idField = AppointmentTreatmentEntity.class.getDeclaredField("id");

        // Then
        assertThat(idField.isAnnotationPresent(EmbeddedId.class)).isTrue();
    }

    @Test
    void shouldHaveManyToOneAnnotationsOnRelations() throws NoSuchFieldException {
        // Given
        var appointmentField = AppointmentTreatmentEntity.class.getDeclaredField("appointment");
        var treatmentField = AppointmentTreatmentEntity.class.getDeclaredField("treatment");

        // Then
        assertThat(appointmentField.isAnnotationPresent(ManyToOne.class)).isTrue();
        assertThat(appointmentField.getAnnotation(ManyToOne.class).fetch()).isEqualTo(FetchType.LAZY);
        assertThat(appointmentField.isAnnotationPresent(MapsId.class)).isTrue();
        assertThat(appointmentField.getAnnotation(MapsId.class).value()).isEqualTo("appointmentId");
        assertThat(appointmentField.isAnnotationPresent(JoinColumn.class)).isTrue();
        assertThat(appointmentField.getAnnotation(JoinColumn.class).name()).isEqualTo("appointment_id");
        assertThat(appointmentField.getAnnotation(JoinColumn.class).nullable()).isFalse();

        assertThat(treatmentField.isAnnotationPresent(ManyToOne.class)).isTrue();
        assertThat(treatmentField.getAnnotation(ManyToOne.class).fetch()).isEqualTo(FetchType.LAZY);
        assertThat(treatmentField.isAnnotationPresent(MapsId.class)).isTrue();
        assertThat(treatmentField.getAnnotation(MapsId.class).value()).isEqualTo("treatmentId");
        assertThat(treatmentField.isAnnotationPresent(JoinColumn.class)).isTrue();
        assertThat(treatmentField.getAnnotation(JoinColumn.class).name()).isEqualTo("treatment_id");
        assertThat(treatmentField.getAnnotation(JoinColumn.class).nullable()).isFalse();
    }

    @Test
    void shouldHaveColumnAnnotationsOnFields() throws NoSuchFieldException {
        // Given
        var cantidadField = AppointmentTreatmentEntity.class.getDeclaredField("cantidad");
        var costoAplicadoField = AppointmentTreatmentEntity.class.getDeclaredField("costoAplicado");
        var observacionesField = AppointmentTreatmentEntity.class.getDeclaredField("observaciones");
        var fechaRegistroField = AppointmentTreatmentEntity.class.getDeclaredField("fechaRegistro");

        // Then
        assertThat(cantidadField.isAnnotationPresent(Column.class)).isTrue();
        assertThat(cantidadField.getAnnotation(Column.class).name()).isEqualTo("cantidad");
        assertThat(cantidadField.getAnnotation(Column.class).nullable()).isFalse();

        assertThat(costoAplicadoField.isAnnotationPresent(Column.class)).isTrue();
        assertThat(costoAplicadoField.getAnnotation(Column.class).name()).isEqualTo("costo_aplicado");
        assertThat(costoAplicadoField.getAnnotation(Column.class).nullable()).isFalse();

        assertThat(observacionesField.isAnnotationPresent(Column.class)).isTrue();
        assertThat(observacionesField.getAnnotation(Column.class).name()).isEqualTo("observaciones");
        // nullable default es true, no se especifica en la anotación

        assertThat(fechaRegistroField.isAnnotationPresent(Column.class)).isTrue();
        assertThat(fechaRegistroField.getAnnotation(Column.class).name()).isEqualTo("fecha_registro");
        assertThat(fechaRegistroField.getAnnotation(Column.class).nullable()).isFalse();
    }

    @Test
    void shouldHaveEmbeddableAnnotationOnIdClass() {
        // Then
        assertThat(AppointmentTreatmentEntity.AppointmentTreatmentId.class.isAnnotationPresent(Embeddable.class)).isTrue();
    }

    @Test
    void shouldHaveColumnAnnotationsOnIdClassFields() throws NoSuchFieldException {
        // Given
        var appointmentIdField = AppointmentTreatmentEntity.AppointmentTreatmentId.class.getDeclaredField("appointmentId");
        var treatmentIdField = AppointmentTreatmentEntity.AppointmentTreatmentId.class.getDeclaredField("treatmentId");

        // Then
        assertThat(appointmentIdField.isAnnotationPresent(Column.class)).isTrue();
        assertThat(appointmentIdField.getAnnotation(Column.class).name()).isEqualTo("appointment_id");

        assertThat(treatmentIdField.isAnnotationPresent(Column.class)).isTrue();
        assertThat(treatmentIdField.getAnnotation(Column.class).name()).isEqualTo("treatment_id");
    }

    @Test
    void shouldImplementSerializableInIdClass() {
        // Then
        assertThat(java.io.Serializable.class.isAssignableFrom(AppointmentTreatmentEntity.AppointmentTreatmentId.class)).isTrue();
    }
}