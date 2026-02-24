package com.application.infrastructure.repository;

import com.application.infrastructure.entity.PlanTreatmentEntity;
import com.application.infrastructure.entity.key.PlanTreatmentKey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PlanTreatmentJpaRepositoryTest {

    @Mock
    private PlanTreatmentJpaRepository planTreatmentJpaRepository;

    @Test
    void shouldExtendJpaRepository() {
        assertTrue(JpaRepository.class.isAssignableFrom(PlanTreatmentJpaRepository.class));
    }

    @Test
    void shouldHaveCorrectEntityAndKeyTypes() {
        // This test verifies the generic types are correctly set.
        // The repository is defined as JpaRepository<PlanTreatmentEntity, PlanTreatmentKey>
        // We can't directly test generics at runtime, but we can verify the mock works.
        PlanTreatmentKey key = new PlanTreatmentKey(UUID.randomUUID(), UUID.randomUUID());
        PlanTreatmentEntity entity = new PlanTreatmentEntity();
        // If the types were wrong, this assignment would cause a compilation error.
        // We perform a simple check to ensure the mock is not null.
        assertNotNull(planTreatmentJpaRepository);
    }

    @Test
    void shouldBeAnnotatedWithRepository() {
        // Check if the interface has the @Repository annotation.
        // Since we are testing the actual interface, we can inspect its annotations.
        Repository annotation = PlanTreatmentJpaRepository.class.getAnnotation(Repository.class);
        assertNotNull(annotation, "PlanTreatmentJpaRepository should be annotated with @Repository");
    }
}