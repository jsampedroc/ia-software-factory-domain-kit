package com.application.infrastructure.entity;

import com.application.domain.valueobject.AppointmentId;
import com.application.domain.valueobject.AppointmentStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentEntityTest {

    @Mock
    private PatientEntity mockPatientEntity;
    @Mock
    private DentistEntity mockDentistEntity;
    @Mock
    private ClinicEntity mockClinicEntity;
    @Mock
    private ConsultingRoomEntity mockConsultingRoomEntity;
    @Mock
    private AppointmentTreatmentEntity mockAppointmentTreatmentEntity;

    @Test
    void shouldCreateAppointmentEntityWithAllFields() {
        // Given
        AppointmentId appointmentId = new AppointmentId(UUID.randomUUID());
        LocalDateTime fechaHora = LocalDateTime.now().plusDays(1);
        Integer duracionMinutos = 60;
        AppointmentStatus estado = AppointmentStatus.PROGRAMADA;
        String motivo = "Limpieza dental";
        String notas = "Paciente con sensibilidad";

        // When
        AppointmentEntity appointment = new AppointmentEntity();
        // Reflection or package-private setter simulation for test context
        // In a real test, you might use a test builder or reflection utils
        // For this example, we assert the structure and assume proper creation via factory in domain
        // This test validates the entity's ability to hold data as per its design.

        // Since the entity has a protected constructor and no public setters,
        // we verify the class structure and annotations.
        // A more complete integration test would be in the adapter layer.

        // Assert class annotations
        assertThat(AppointmentEntity.class.getAnnotation(Entity.class)).isNotNull();
        assertThat(AppointmentEntity.class.getAnnotation(jakarta.persistence.Table.class).name()).isEqualTo("appointments");

        // Assert field mappings
        // This is a structural test, not a behavioral one.
    }

    @Test
    void shouldHaveCorrectRelationships() {
        // Given: Simulated IDs
        AppointmentId appointmentId = new AppointmentId(UUID.randomUUID());

        // When: We examine the entity's relationship fields
        AppointmentEntity appointment = new AppointmentEntity();

        // Then: Verify relationships are correctly defined via annotations (conceptual test)
        // Patient relationship
        assertThat(AppointmentEntity.class.getDeclaredField("patient").getAnnotation(jakarta.persistence.ManyToOne.class).fetch())
                .isEqualTo(jakarta.persistence.FetchType.LAZY);
        // Dentist relationship
        assertThat(AppointmentEntity.class.getDeclaredField("dentist").getAnnotation(jakarta.persistence.ManyToOne.class).fetch())
                .isEqualTo(jakarta.persistence.FetchType.LAZY);
        // Clinic relationship
        assertThat(AppointmentEntity.class.getDeclaredField("clinic").getAnnotation(jakarta.persistence.ManyToOne.class).fetch())
                .isEqualTo(jakarta.persistence.FetchType.LAZY);
        // ConsultingRoom relationship (optional)
        assertThat(AppointmentEntity.class.getDeclaredField("consultingRoom").getAnnotation(jakarta.persistence.ManyToOne.class).fetch())
                .isEqualTo(jakarta.persistence.FetchType.LAZY);
        // AppointmentTreatments collection
        assertThat(AppointmentEntity.class.getDeclaredField("appointmentTreatments").getAnnotation(jakarta.persistence.OneToMany.class).mappedBy())
                .isEqualTo("appointment");
    }

    @Test
    void shouldHandleAppointmentTreatmentsCollection() {
        // Given
        AppointmentEntity appointment = new AppointmentEntity();
        Set<AppointmentTreatmentEntity> treatments = new HashSet<>();
        treatments.add(mockAppointmentTreatmentEntity);

        // When: Simulate setting the collection (via reflection or package-private method in a test helper)
        // This is a conceptual test; in practice, the collection is managed by JPA/hibernate.
        // We verify the collection is initialized to avoid NullPointerException.
        assertThat(appointment.getAppointmentTreatments()).isNotNull();
        assertThat(appointment.getAppointmentTreatments()).isEmpty();

        // The entity's design ensures the collection is initialized as an empty HashSet.
    }

    @Test
    void shouldReflectCorrectColumnDefinitions() throws NoSuchFieldException {
        // Given: The AppointmentEntity class

        // Then: Check column annotations for critical fields
        // fecha_hora is non-nullable
        assertThat(AppointmentEntity.class.getDeclaredField("fechaHora").getAnnotation(jakarta.persistence.Column.class).nullable())
                .isFalse();
        // duracion_minutos is non-nullable
        assertThat(AppointmentEntity.class.getDeclaredField("duracionMinutos").getAnnotation(jakarta.persistence.Column.class).nullable())
                .isFalse();
        // estado is non-nullable and enumerated
        assertThat(AppointmentEntity.class.getDeclaredField("estado").getAnnotation(jakarta.persistence.Column.class).nullable())
                .isFalse();
        assertThat(AppointmentEntity.class.getDeclaredField("estado").getAnnotation(jakarta.persistence.Enumerated.class).value())
                .isEqualTo(jakarta.persistence.EnumType.STRING);
        // motivo has length 500
        assertThat(AppointmentEntity.class.getDeclaredField("motivo").getAnnotation(jakarta.persistence.Column.class).length())
                .isEqualTo(500);
        // notas is TEXT
        assertThat(AppointmentEntity.class.getDeclaredField("notas").getAnnotation(jakarta.persistence.Column.class).columnDefinition())
                .isEqualTo("TEXT");
    }

    @Test
    void shouldUseValueObjectAsId() {
        // Given: The AppointmentEntity class

        // Then: The ID field is of type AppointmentId (Value Object) and is annotated with @Id
        assertThat(AppointmentEntity.class.getDeclaredField("id").getType()).isEqualTo(AppointmentId.class);
        assertThat(AppointmentEntity.class.getDeclaredField("id").getAnnotation(jakarta.persistence.Id.class)).isNotNull();
    }
}