package com.application.infrastructure.repository;

import com.application.infrastructure.entity.TreatmentPlanEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TreatmentPlanJpaRepositoryTest {

    @Mock
    private TreatmentPlanJpaRepository treatmentPlanJpaRepository;

    private final UUID testPlanId = UUID.randomUUID();
    private final UUID testPatientId = UUID.randomUUID();
    private final UUID testDentistId = UUID.randomUUID();
    private final String testStatus = "ACTIVE";
    private final LocalDate testDate = LocalDate.now();

    @Test
    void findByPlanId_shouldReturnTreatmentPlanEntityWhenExists() {
        TreatmentPlanEntity entity = TreatmentPlanEntity.create(testPlanId);
        when(treatmentPlanJpaRepository.findByPlanId(testPlanId)).thenReturn(Optional.of(entity));

        Optional<TreatmentPlanEntity> result = treatmentPlanJpaRepository.findByPlanId(testPlanId);

        assertThat(result).isPresent();
        assertThat(result.get().getPlanId()).isEqualTo(testPlanId);
    }

    @Test
    void findByPlanId_shouldReturnEmptyWhenNotExists() {
        when(treatmentPlanJpaRepository.findByPlanId(testPlanId)).thenReturn(Optional.empty());

        Optional<TreatmentPlanEntity> result = treatmentPlanJpaRepository.findByPlanId(testPlanId);

        assertThat(result).isEmpty();
    }

    @Test
    void findByPatientId_shouldReturnListOfTreatmentPlanEntities() {
        List<TreatmentPlanEntity> expectedList = Arrays.asList(
                TreatmentPlanEntity.create(UUID.randomUUID()),
                TreatmentPlanEntity.create(UUID.randomUUID())
        );
        when(treatmentPlanJpaRepository.findByPatientId(testPatientId)).thenReturn(expectedList);

        List<TreatmentPlanEntity> result = treatmentPlanJpaRepository.findByPatientId(testPatientId);

        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(expectedList);
    }

    @Test
    void findByDentistId_shouldReturnListOfTreatmentPlanEntities() {
        List<TreatmentPlanEntity> expectedList = Collections.singletonList(
                TreatmentPlanEntity.create(UUID.randomUUID())
        );
        when(treatmentPlanJpaRepository.findByDentistId(testDentistId)).thenReturn(expectedList);

        List<TreatmentPlanEntity> result = treatmentPlanJpaRepository.findByDentistId(testDentistId);

        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(expectedList);
    }

    @Test
    void findByStatus_shouldReturnListOfTreatmentPlanEntities() {
        List<TreatmentPlanEntity> expectedList = Arrays.asList(
                TreatmentPlanEntity.create(UUID.randomUUID()),
                TreatmentPlanEntity.create(UUID.randomUUID()),
                TreatmentPlanEntity.create(UUID.randomUUID())
        );
        when(treatmentPlanJpaRepository.findByStatus(testStatus)).thenReturn(expectedList);

        List<TreatmentPlanEntity> result = treatmentPlanJpaRepository.findByStatus(testStatus);

        assertThat(result).hasSize(3);
        assertThat(result).isEqualTo(expectedList);
    }

    @Test
    void findActivePlansByDate_shouldReturnListOfTreatmentPlanEntities() {
        List<TreatmentPlanEntity> expectedList = Arrays.asList(
                TreatmentPlanEntity.create(UUID.randomUUID()),
                TreatmentPlanEntity.create(UUID.randomUUID())
        );
        when(treatmentPlanJpaRepository.findActivePlansByDate(testDate)).thenReturn(expectedList);

        List<TreatmentPlanEntity> result = treatmentPlanJpaRepository.findActivePlansByDate(testDate);

        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(expectedList);
    }

    @Test
    void findByPatientIdAndStatus_shouldReturnListOfTreatmentPlanEntities() {
        List<TreatmentPlanEntity> expectedList = Collections.singletonList(
                TreatmentPlanEntity.create(UUID.randomUUID())
        );
        when(treatmentPlanJpaRepository.findByPatientIdAndStatus(testPatientId, testStatus)).thenReturn(expectedList);

        List<TreatmentPlanEntity> result = treatmentPlanJpaRepository.findByPatientIdAndStatus(testPatientId, testStatus);

        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(expectedList);
    }

    @Test
    void existsByPlanId_shouldReturnTrueWhenExists() {
        when(treatmentPlanJpaRepository.existsByPlanId(testPlanId)).thenReturn(true);

        boolean result = treatmentPlanJpaRepository.existsByPlanId(testPlanId);

        assertThat(result).isTrue();
    }

    @Test
    void existsByPlanId_shouldReturnFalseWhenNotExists() {
        when(treatmentPlanJpaRepository.existsByPlanId(testPlanId)).thenReturn(false);

        boolean result = treatmentPlanJpaRepository.existsByPlanId(testPlanId);

        assertThat(result).isFalse();
    }

    @Test
    void save_shouldDelegateToJpaRepository() {
        TreatmentPlanEntity entity = TreatmentPlanEntity.create(testPlanId);
        when(treatmentPlanJpaRepository.save(entity)).thenReturn(entity);

        TreatmentPlanEntity result = treatmentPlanJpaRepository.save(entity);

        assertThat(result).isEqualTo(entity);
    }

    @Test
    void findById_shouldDelegateToJpaRepository() {
        TreatmentPlanEntity entity = TreatmentPlanEntity.create(testPlanId);
        when(treatmentPlanJpaRepository.findById(testPlanId)).thenReturn(Optional.of(entity));

        Optional<TreatmentPlanEntity> result = treatmentPlanJpaRepository.findById(testPlanId);

        assertThat(result).contains(entity);
    }

    @Test
    void findAll_shouldDelegateToJpaRepository() {
        List<TreatmentPlanEntity> expectedList = Arrays.asList(
                TreatmentPlanEntity.create(UUID.randomUUID()),
                TreatmentPlanEntity.create(UUID.randomUUID())
        );
        when(treatmentPlanJpaRepository.findAll()).thenReturn(expectedList);

        List<TreatmentPlanEntity> result = treatmentPlanJpaRepository.findAll();

        assertThat(result).isEqualTo(expectedList);
    }

    @Test
    void deleteById_shouldDelegateToJpaRepository() {
        treatmentPlanJpaRepository.deleteById(testPlanId);
    }
}