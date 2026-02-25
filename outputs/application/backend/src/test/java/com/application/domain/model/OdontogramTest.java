package com.application.domain.model;

import com.application.domain.exception.DomainException;
import com.application.domain.valueobject.OdontogramId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OdontogramTest {

    private OdontogramId odontogramId;
    private Odontogram odontogram;

    @Mock
    private TreatmentRecord treatmentRecord;

    @BeforeEach
    void setUp() {
        odontogramId = new OdontogramId(UUID.randomUUID());
        odontogram = Odontogram.createInitial(odontogramId);
    }

    @Test
    @DisplayName("Create initial Odontogram should succeed with valid ID")
    void createInitial_ValidId_Success() {
        Odontogram initial = Odontogram.createInitial(odontogramId);

        assertThat(initial).isNotNull();
        assertThat(initial.getId()).isEqualTo(odontogramId);
        assertThat(initial.getToothData()).isEmpty();
        assertThat(initial.getLastUpdated()).isNotNull();
    }

    @Test
    @DisplayName("Create initial Odontogram with null ID should throw DomainException")
    void createInitial_NullId_ThrowsException() {
        assertThatThrownBy(() -> Odontogram.createInitial(null))
                .isInstanceOf(DomainException.class)
                .hasMessage("Odontogram ID cannot be null");
    }

    @Nested
    @DisplayName("Update Tooth Condition Tests")
    class UpdateToothConditionTests {

        private ToothCondition validCondition;

        @BeforeEach
        void setUp() {
            validCondition = ToothCondition.builder()
                    .toothNumber(15)
                    .condition(ToothStatus.CARIES)
                    .notes("Initial caries")
                    .lastTreated(LocalDate.now())
                    .build();
        }

        @Test
        @DisplayName("Update tooth condition with valid data should succeed")
        void updateToothCondition_ValidData_Success() {
            LocalDateTime beforeUpdate = LocalDateTime.now().minusSeconds(1);

            odontogram.updateToothCondition(validCondition, treatmentRecord);

            assertThat(odontogram.getToothCondition(15)).isEqualTo(validCondition);
            assertThat(odontogram.getToothData()).hasSize(1);
            assertThat(odontogram.getLastUpdated()).isAfter(beforeUpdate);
        }

        @Test
        @DisplayName("Update tooth condition with null condition should throw DomainException")
        void updateToothCondition_NullCondition_ThrowsException() {
            assertThatThrownBy(() -> odontogram.updateToothCondition(null, treatmentRecord))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Tooth condition cannot be null");
        }

        @Test
        @DisplayName("Update tooth condition with null treatment record should throw DomainException")
        void updateToothCondition_NullTreatmentRecord_ThrowsException() {
            assertThatThrownBy(() -> odontogram.updateToothCondition(validCondition, null))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Odontogram updates must be traceable to a specific TreatmentRecord");
        }

        @Test
        @DisplayName("Update tooth condition with tooth number less than 0 should throw DomainException")
        void updateToothCondition_ToothNumberNegative_ThrowsException() {
            ToothCondition invalidCondition = validCondition.toBuilder().toothNumber(-1).build();

            assertThatThrownBy(() -> odontogram.updateToothCondition(invalidCondition, treatmentRecord))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Tooth number must be between 0 (general) and 32");
        }

        @Test
        @DisplayName("Update tooth condition with tooth number greater than 32 should throw DomainException")
        void updateToothCondition_ToothNumberTooHigh_ThrowsException() {
            ToothCondition invalidCondition = validCondition.toBuilder().toothNumber(33).build();

            assertThatThrownBy(() -> odontogram.updateToothCondition(invalidCondition, treatmentRecord))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Tooth number must be between 0 (general) and 32");
        }

        @Test
        @DisplayName("Update tooth condition with tooth number 0 (general) should succeed")
        void updateToothCondition_GeneralToothNumber_Success() {
            ToothCondition generalCondition = validCondition.toBuilder().toothNumber(0).build();

            odontogram.updateToothCondition(generalCondition, treatmentRecord);

            assertThat(odontogram.getToothCondition(0)).isEqualTo(generalCondition);
        }

        @Test
        @DisplayName("Update existing tooth condition should overwrite previous data")
        void updateToothCondition_ExistingTooth_Overwrites() {
            odontogram.updateToothCondition(validCondition, treatmentRecord);

            ToothCondition updatedCondition = validCondition.toBuilder()
                    .condition(ToothStatus.FILLED)
                    .notes("Caries filled")
                    .build();

            odontogram.updateToothCondition(updatedCondition, treatmentRecord);

            assertThat(odontogram.getToothCondition(15)).isEqualTo(updatedCondition);
            assertThat(odontogram.getToothData()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Update Multiple Conditions Tests")
    class UpdateMultipleConditionsTests {

        private Set<ToothCondition> validConditions;

        @BeforeEach
        void setUp() {
            validConditions = new HashSet<>();
            validConditions.add(ToothCondition.builder()
                    .toothNumber(15)
                    .condition(ToothStatus.CARIES)
                    .notes("Caries on 15")
                    .lastTreated(LocalDate.now())
                    .build());
            validConditions.add(ToothCondition.builder()
                    .toothNumber(16)
                    .condition(ToothStatus.HEALTHY)
                    .notes("Healthy")
                    .lastTreated(LocalDate.now())
                    .build());
        }

        @Test
        @DisplayName("Update multiple conditions with valid data should succeed")
        void updateMultipleConditions_ValidData_Success() {
            odontogram.updateMultipleConditions(validConditions, treatmentRecord);

            assertThat(odontogram.getToothData()).hasSize(2);
            assertThat(odontogram.getToothCondition(15)).isNotNull();
            assertThat(odontogram.getToothCondition(16)).isNotNull();
        }

        @Test
        @DisplayName("Update multiple conditions with null set should throw DomainException")
        void updateMultipleConditions_NullSet_ThrowsException() {
            assertThatThrownBy(() -> odontogram.updateMultipleConditions(null, treatmentRecord))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("At least one tooth condition must be provided");
        }

        @Test
        @DisplayName("Update multiple conditions with empty set should throw DomainException")
        void updateMultipleConditions_EmptySet_ThrowsException() {
            assertThatThrownBy(() -> odontogram.updateMultipleConditions(Collections.emptySet(), treatmentRecord))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("At least one tooth condition must be provided");
        }

        @Test
        @DisplayName("Update multiple conditions with null treatment record should throw DomainException")
        void updateMultipleConditions_NullTreatmentRecord_ThrowsException() {
            assertThatThrownBy(() -> odontogram.updateMultipleConditions(validConditions, null))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Odontogram updates must be traceable to a specific TreatmentRecord");
        }

        @Test
        @DisplayName("Update multiple conditions with one invalid tooth number should throw DomainException")
        void updateMultipleConditions_OneInvalidTooth_ThrowsException() {
            ToothCondition invalidCondition = ToothCondition.builder()
                    .toothNumber(50)
                    .condition(ToothStatus.CARIES)
                    .notes("Invalid")
                    .lastTreated(LocalDate.now())
                    .build();
            validConditions.add(invalidCondition);

            assertThatThrownBy(() -> odontogram.updateMultipleConditions(validConditions, treatmentRecord))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Tooth number must be between 0 (general) and 32");
        }
    }

    @Nested
    @DisplayName("Query Methods Tests")
    class QueryMethodsTests {

        @BeforeEach
        void setUp() {
            ToothCondition condition1 = ToothCondition.builder()
                    .toothNumber(15)
                    .condition(ToothStatus.CARIES)
                    .notes("Test caries")
                    .lastTreated(LocalDate.now())
                    .build();
            ToothCondition condition2 = ToothCondition.builder()
                    .toothNumber(16)
                    .condition(ToothStatus.FILLED)
                    .notes("Amalgam filling")
                    .lastTreated(LocalDate.now())
                    .build();

            odontogram.updateToothCondition(condition1, treatmentRecord);
            odontogram.updateToothCondition(condition2, treatmentRecord);
        }

        @Test
        @DisplayName("Get tooth condition for existing tooth should return condition")
        void getToothCondition_ExistingTooth_ReturnsCondition() {
            ToothCondition result = odontogram.getToothCondition(15);

            assertThat(result).isNotNull();
            assertThat(result.getToothNumber()).isEqualTo(15);
            assertThat(result.getCondition()).isEqualTo(ToothStatus.CARIES);
        }

        @Test
        @DisplayName("Get tooth condition for non-existing tooth should return null")
        void getToothCondition_NonExistingTooth_ReturnsNull() {
            ToothCondition result = odontogram.getToothCondition(99);

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Get tooth data should return unmodifiable map")
        void getToothData_ReturnsUnmodifiableMap() {
            Map<Integer, ToothCondition> toothData = odontogram.getToothData();

            assertThat(toothData).hasSize(2);
            assertThatThrownBy(() -> toothData.put(17, ToothCondition.builder()
                    .toothNumber(17)
                    .condition(ToothStatus.HEALTHY)
                    .notes("Test")
                    .lastTreated(LocalDate.now())
                    .build()))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("Has tooth for existing tooth should return true")
        void hasTooth_ExistingTooth_ReturnsTrue() {
            assertThat(odontogram.hasTooth(15)).isTrue();
            assertThat(odontogram.hasTooth(16)).isTrue();
        }

        @Test
        @DisplayName("Has tooth for non-existing tooth should return false")
        void hasTooth_NonExistingTooth_ReturnsFalse() {
            assertThat(odontogram.hasTooth(99)).isFalse();
            assertThat(odontogram.hasTooth(0)).isFalse();
        }
    }

    @Test
    @DisplayName("Odontogram toString should include class name and ID")
    void toString_ContainsClassNameAndId() {
        String toString = odontogram.toString();

        assertThat(toString).contains("Odontogram");
        assertThat(toString).contains(odontogramId.toString());
    }

    @Test
    @DisplayName("Odontogram builder should create instance with provided values")
    void builder_CreatesInstanceWithValues() {
        Map<Integer, ToothCondition> initialData = new HashMap<>();
        ToothCondition condition = ToothCondition.builder()
                .toothNumber(1)
                .condition(ToothStatus.HEALTHY)
                .notes("Builder test")
                .lastTreated(LocalDate.now())
                .build();
        initialData.put(1, condition);

        LocalDateTime fixedTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        Odontogram built = Odontogram.builder()
                .id(odontogramId)
                .toothData(initialData)
                .lastUpdated(fixedTime)
                .build();

        assertThat(built.getId()).isEqualTo(odontogramId);
        assertThat(built.getToothData()).hasSize(1);
        assertThat(built.getToothCondition(1)).isEqualTo(condition);
        assertThat(built.getLastUpdated()).isEqualTo(fixedTime);
    }

    @Test
    @DisplayName("Odontogram toBuilder should create mutable copy")
    void toBuilder_CreatesMutableCopy() {
        ToothCondition originalCondition = ToothCondition.builder()
                .toothNumber(15)
                .condition(ToothStatus.CARIES)
                .notes("Original")
                .lastTreated(LocalDate.now())
                .build();
        odontogram.updateToothCondition(originalCondition, treatmentRecord);

        Odontogram copy = odontogram.toBuilder().build();

        assertThat(copy.getId()).isEqualTo(odontogram.getId());
        assertThat(copy.getToothData()).hasSize(1);
        assertThat(copy.getToothCondition(15)).isEqualTo(originalCondition);
    }
}