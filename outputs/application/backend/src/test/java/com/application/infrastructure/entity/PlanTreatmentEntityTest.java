package com.application.infrastructure.entity;

import com.application.domain.valueobject.TreatmentId;
import com.application.domain.valueobject.TreatmentPlanId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
class PlanTreatmentEntityTest {

    @Test
    void shouldCreatePlanTreatmentIdCorrectly() {
        // Given
        TreatmentPlanId treatmentPlanId = new TreatmentPlanId(UUID.randomUUID());
        TreatmentId treatmentId = new TreatmentId(UUID.randomUUID());

        // When
        PlanTreatmentEntity.PlanTreatmentId id = new PlanTreatmentEntity.PlanTreatmentId(treatmentPlanId, treatmentId);

        // Then
        assertAll(
                () -> assertThat(id.getTreatmentPlanId()).isEqualTo(treatmentPlanId),
                () -> assertThat(id.getTreatmentId()).isEqualTo(treatmentId)
        );
    }

    @Test
    void shouldCreatePlanTreatmentEntityWithAllFields() {
        // Given
        TreatmentPlanId treatmentPlanId = new TreatmentPlanId(UUID.randomUUID());
        TreatmentId treatmentId = new TreatmentId(UUID.randomUUID());
        PlanTreatmentEntity.PlanTreatmentId id = new PlanTreatmentEntity.PlanTreatmentId(treatmentPlanId, treatmentId);

        TreatmentPlanEntity treatmentPlan = new TreatmentPlanEntity();
        TreatmentEntity treatment = new TreatmentEntity();

        Integer orden = 1;
        Integer sesionEstimada = 3;
        BigDecimal costoAplicado = new BigDecimal("150.50");
        String notas = "Notas importantes del tratamiento";
        LocalDateTime fechaAgregado = LocalDateTime.now();
        LocalDateTime fechaCompletado = fechaAgregado.plusDays(7);

        // When - Using reflection to set fields due to protected constructor
        PlanTreatmentEntity entity = new PlanTreatmentEntity();
        setField(entity, "id", id);
        setField(entity, "treatmentPlan", treatmentPlan);
        setField(entity, "treatment", treatment);
        setField(entity, "orden", orden);
        setField(entity, "sesionEstimada", sesionEstimada);
        setField(entity, "costoAplicado", costoAplicado);
        setField(entity, "notas", notas);
        setField(entity, "fechaAgregado", fechaAgregado);
        setField(entity, "fechaCompletado", fechaCompletado);

        // Then
        assertAll(
                () -> assertThat(entity.getId()).isEqualTo(id),
                () -> assertThat(entity.getTreatmentPlan()).isEqualTo(treatmentPlan),
                () -> assertThat(entity.getTreatment()).isEqualTo(treatment),
                () -> assertThat(entity.getOrden()).isEqualTo(orden),
                () -> assertThat(entity.getSesionEstimada()).isEqualTo(sesionEstimada),
                () -> assertThat(entity.getCostoAplicado()).isEqualTo(costoAplicado),
                () -> assertThat(entity.getNotas()).isEqualTo(notas),
                () -> assertThat(entity.getFechaAgregado()).isEqualTo(fechaAgregado),
                () -> assertThat(entity.getFechaCompletado()).isEqualTo(fechaCompletado)
        );
    }

    @Test
    void shouldHandleNullOptionalFields() {
        // Given
        TreatmentPlanId treatmentPlanId = new TreatmentPlanId(UUID.randomUUID());
        TreatmentId treatmentId = new TreatmentId(UUID.randomUUID());
        PlanTreatmentEntity.PlanTreatmentId id = new PlanTreatmentEntity.PlanTreatmentId(treatmentPlanId, treatmentId);

        TreatmentPlanEntity treatmentPlan = new TreatmentPlanEntity();
        TreatmentEntity treatment = new TreatmentEntity();

        Integer orden = 2;
        LocalDateTime fechaAgregado = LocalDateTime.now();

        // When
        PlanTreatmentEntity entity = new PlanTreatmentEntity();
        setField(entity, "id", id);
        setField(entity, "treatmentPlan", treatmentPlan);
        setField(entity, "treatment", treatment);
        setField(entity, "orden", orden);
        setField(entity, "fechaAgregado", fechaAgregado);

        // Then
        assertAll(
                () -> assertThat(entity.getId()).isEqualTo(id),
                () -> assertThat(entity.getTreatmentPlan()).isEqualTo(treatmentPlan),
                () -> assertThat(entity.getTreatment()).isEqualTo(treatment),
                () -> assertThat(entity.getOrden()).isEqualTo(orden),
                () -> assertThat(entity.getSesionEstimada()).isNull(),
                () -> assertThat(entity.getCostoAplicado()).isNull(),
                () -> assertThat(entity.getNotas()).isNull(),
                () -> assertThat(entity.getFechaAgregado()).isEqualTo(fechaAgregado),
                () -> assertThat(entity.getFechaCompletado()).isNull()
        );
    }

    @Test
    void shouldHaveProtectedNoArgsConstructor() {
        // When - This should compile and work due to @NoArgsConstructor(access = PROTECTED)
        PlanTreatmentEntity entity = new PlanTreatmentEntity();
        PlanTreatmentEntity.PlanTreatmentId id = new PlanTreatmentEntity.PlanTreatmentId();

        // Then - Verify the objects are created (no exception thrown)
        assertThat(entity).isNotNull();
        assertThat(id).isNotNull();
    }

    @Test
    void shouldHaveCorrectTableName() {
        // Given
        PlanTreatmentEntity entity = new PlanTreatmentEntity();

        // When - Check via reflection if needed, but mainly verify class annotations
        Table tableAnnotation = entity.getClass().getAnnotation(Table.class);

        // Then
        assertThat(tableAnnotation).isNotNull();
        assertThat(tableAnnotation.name()).isEqualTo("plan_treatments");
    }

    @Test
    void shouldHaveEmbeddedIdAnnotation() throws NoSuchFieldException {
        // Given
        java.lang.reflect.Field idField = PlanTreatmentEntity.class.getDeclaredField("id");

        // When
        EmbeddedId embeddedIdAnnotation = idField.getAnnotation(EmbeddedId.class);

        // Then
        assertThat(embeddedIdAnnotation).isNotNull();
    }

    @Test
    void shouldHaveManyToOneRelationships() throws NoSuchFieldException {
        // Given
        java.lang.reflect.Field treatmentPlanField = PlanTreatmentEntity.class.getDeclaredField("treatmentPlan");
        java.lang.reflect.Field treatmentField = PlanTreatmentEntity.class.getDeclaredField("treatment");

        // When
        ManyToOne treatmentPlanAnnotation = treatmentPlanField.getAnnotation(ManyToOne.class);
        ManyToOne treatmentAnnotation = treatmentField.getAnnotation(ManyToOne.class);

        // Then
        assertAll(
                () -> assertThat(treatmentPlanAnnotation).isNotNull(),
                () -> assertThat(treatmentPlanAnnotation.fetch()).isEqualTo(FetchType.LAZY),
                () -> assertThat(treatmentAnnotation).isNotNull(),
                () -> assertThat(treatmentAnnotation.fetch()).isEqualTo(FetchType.LAZY)
        );
    }

    @Test
    void shouldHaveMapsIdAnnotations() throws NoSuchFieldException {
        // Given
        java.lang.reflect.Field treatmentPlanField = PlanTreatmentEntity.class.getDeclaredField("treatmentPlan");
        java.lang.reflect.Field treatmentField = PlanTreatmentEntity.class.getDeclaredField("treatment");

        // When
        MapsId treatmentPlanAnnotation = treatmentPlanField.getAnnotation(MapsId.class);
        MapsId treatmentAnnotation = treatmentField.getAnnotation(MapsId.class);

        // Then
        assertAll(
                () -> assertThat(treatmentPlanAnnotation).isNotNull(),
                () -> assertThat(treatmentPlanAnnotation.value()).isEqualTo("treatmentPlanId"),
                () -> assertThat(treatmentAnnotation).isNotNull(),
                () -> assertThat(treatmentAnnotation.value()).isEqualTo("treatmentId")
        );
    }

    @Test
    void shouldHaveColumnAnnotations() throws NoSuchFieldException {
        // Given
        java.lang.reflect.Field ordenField = PlanTreatmentEntity.class.getDeclaredField("orden");
        java.lang.reflect.Field costoAplicadoField = PlanTreatmentEntity.class.getDeclaredField("costoAplicado");

        // When
        Column ordenAnnotation = ordenField.getAnnotation(Column.class);
        Column costoAplicadoAnnotation = costoAplicadoField.getAnnotation(Column.class);

        // Then
        assertAll(
                () -> assertThat(ordenAnnotation).isNotNull(),
                () -> assertThat(ordenAnnotation.nullable()).isFalse(),
                () -> assertThat(costoAplicadoAnnotation).isNotNull(),
                () -> assertThat(costoAplicadoAnnotation.precision()).isEqualTo(10),
                () -> assertThat(costoAplicadoAnnotation.scale()).isEqualTo(2)
        );
    }

    @Test
    void shouldHaveEmbeddableIdClass() {
        // Given
        Class<?> idClass = PlanTreatmentEntity.PlanTreatmentId.class;

        // When
        Embeddable embeddableAnnotation = idClass.getAnnotation(Embeddable.class);
        Getter getterAnnotation = idClass.getAnnotation(Getter.class);

        // Then
        assertAll(
                () -> assertThat(embeddableAnnotation).isNotNull(),
                () -> assertThat(getterAnnotation).isNotNull(),
                () -> assertThat(java.io.Serializable.class.isAssignableFrom(idClass)).isTrue()
        );
    }

    // Helper method to set private fields using reflection
    private void setField(Object object, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set field: " + fieldName, e);
        }
    }
}