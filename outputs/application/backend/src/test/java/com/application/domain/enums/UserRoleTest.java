package com.application.domain.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("unit")
@Tag("domain")
@Tag("enums")
@DisplayName("UserRole Enum Unit Tests")
class UserRoleTest {

    @Test
    @DisplayName("Should have exactly three defined roles")
    void shouldHaveExactlyThreeDefinedRoles() {
        UserRole[] roles = UserRole.values();
        assertThat(roles).hasSize(3);
        List<String> roleNames = Arrays.stream(roles).map(Enum::name).collect(Collectors.toList());
        assertThat(roleNames).containsExactlyInAnyOrder("RECEPTIONIST", "DENTIST", "ADMINISTRATOR");
    }

    @Nested
    @DisplayName("ValueOf Validation Tests")
    class ValueOfTests {

        @ParameterizedTest
        @EnumSource(UserRole.class)
        @DisplayName("Should correctly parse valid role names (case-sensitive)")
        void shouldCorrectlyParseValidRoleNames(UserRole expectedRole) {
            UserRole actualRole = UserRole.valueOf(expectedRole.name());
            assertThat(actualRole).isEqualTo(expectedRole);
        }

        @ParameterizedTest
        @ValueSource(strings = {"receptionist", "Dentist", "ADMIN", "doctor", ""})
        @DisplayName("Should throw IllegalArgumentException for invalid role names")
        void shouldThrowIllegalArgumentExceptionForInvalidRoleNames(String invalidName) {
            assertThatThrownBy(() -> UserRole.valueOf(invalidName))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining(invalidName);
        }

        @Test
        @DisplayName("Should throw NullPointerException for null input")
        void shouldThrowNullPointerExceptionForNullInput() {
            assertThatThrownBy(() -> UserRole.valueOf(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Business Logic and Permissions Tests")
    class BusinessLogicTests {

        @Test
        @DisplayName("Should identify clinical roles correctly")
        void shouldIdentifyClinicalRolesCorrectly() {
            assertThat(UserRole.DENTIST.isClinicalRole()).isTrue();
            assertThat(UserRole.RECEPTIONIST.isClinicalRole()).isFalse();
            assertThat(UserRole.ADMINISTRATOR.isClinicalRole()).isFalse();
        }

        @Test
        @DisplayName("Should identify administrative roles correctly")
        void shouldIdentifyAdministrativeRolesCorrectly() {
            assertThat(UserRole.ADMINISTRATOR.isAdministrativeRole()).isTrue();
            assertThat(UserRole.DENTIST.isAdministrativeRole()).isFalse();
            assertThat(UserRole.RECEPTIONIST.isAdministrativeRole()).isFalse();
        }

        @Test
        @DisplayName("Should identify front desk roles correctly")
        void shouldIdentifyFrontDeskRolesCorrectly() {
            assertThat(UserRole.RECEPTIONIST.isFrontDeskRole()).isTrue();
            assertThat(UserRole.DENTIST.isFrontDeskRole()).isFalse();
            assertThat(UserRole.ADMINISTRATOR.isFrontDeskRole()).isFalse();
        }

        @Test
        @DisplayName("Should have correct permission to modify EHR")
        void shouldHaveCorrectPermissionToModifyEHR() {
            assertThat(UserRole.DENTIST.canModifyEHR()).isTrue();
            assertThat(UserRole.RECEPTIONIST.canModifyEHR()).isFalse();
            assertThat(UserRole.ADMINISTRATOR.canModifyEHR()).isFalse();
        }

        @Test
        @DisplayName("Should have correct permission to schedule appointments")
        void shouldHaveCorrectPermissionToScheduleAppointments() {
            assertThat(UserRole.RECEPTIONIST.canScheduleAppointments()).isTrue();
            assertThat(UserRole.DENTIST.canScheduleAppointments()).isTrue();
            assertThat(UserRole.ADMINISTRATOR.canScheduleAppointments()).isTrue();
        }

        @Test
        @DisplayName("Should have correct permission to view financial data")
        void shouldHaveCorrectPermissionToViewFinancialData() {
            assertThat(UserRole.ADMINISTRATOR.canViewFinancialData()).isTrue();
            assertThat(UserRole.RECEPTIONIST.canViewFinancialData()).isFalse();
            assertThat(UserRole.DENTIST.canViewFinancialData()).isFalse();
        }

        @Test
        @DisplayName("Should have correct permission to approve high-cost treatments")
        void shouldHaveCorrectPermissionToApproveHighCostTreatments() {
            assertThat(UserRole.ADMINISTRATOR.canApproveHighCostTreatments()).isTrue();
            assertThat(UserRole.DENTIST.canApproveHighCostTreatments()).isFalse();
            assertThat(UserRole.RECEPTIONIST.canApproveHighCostTreatments()).isFalse();
        }
    }

    @Nested
    @DisplayName("Enum Properties and Consistency Tests")
    class EnumPropertiesTests {

        @Test
        @DisplayName("Should maintain ordinal consistency")
        void shouldMaintainOrdinalConsistency() {
            assertThat(UserRole.RECEPTIONIST.ordinal()).isEqualTo(0);
            assertThat(UserRole.DENTIST.ordinal()).isEqualTo(1);
            assertThat(UserRole.ADMINISTRATOR.ordinal()).isEqualTo(2);
        }

        @Test
        @DisplayName("Should return correct string representation")
        void shouldReturnCorrectStringRepresentation() {
            assertThat(UserRole.RECEPTIONIST.toString()).isEqualTo("RECEPTIONIST");
            assertThat(UserRole.DENTIST.toString()).isEqualTo("DENTIST");
            assertThat(UserRole.ADMINISTRATOR.toString()).isEqualTo("ADMINISTRATOR");
        }

        @Test
        @DisplayName("Should be comparable")
        void shouldBeComparable() {
            assertThat(UserRole.RECEPTIONIST.compareTo(UserRole.DENTIST)).isNegative();
            assertThat(UserRole.DENTIST.compareTo(UserRole.RECEPTIONIST)).isPositive();
            assertThat(UserRole.DENTIST.compareTo(UserRole.DENTIST)).isZero();
        }
    }

    @Nested
    @DisplayName("Integration with Domain Rules Tests")
    class DomainRulesIntegrationTests {

        @Test
        @DisplayName("Should enforce that only DENTIST can create clinical notes")
        void shouldEnforceThatOnlyDentistCanCreateClinicalNotes() {
            assertThat(UserRole.DENTIST.canCreateClinicalNotes()).isTrue();
            assertThat(UserRole.RECEPTIONIST.canCreateClinicalNotes()).isFalse();
            assertThat(UserRole.ADMINISTRATOR.canCreateClinicalNotes()).isFalse();
        }

        @Test
        @DisplayName("Should enforce that only DENTIST can modify odontograms")
        void shouldEnforceThatOnlyDentistCanModifyOdontograms() {
            assertThat(UserRole.DENTIST.canModifyOdontogram()).isTrue();
            assertThat(UserRole.RECEPTIONIST.canModifyOdontogram()).isFalse();
            assertThat(UserRole.ADMINISTRATOR.canModifyOdontogram()).isFalse();
        }

        @Test
        @DisplayName("Should enforce that RECEPTIONIST cannot access clinical notes")
        void shouldEnforceThatReceptionistCannotAccessClinicalNotes() {
            assertThat(UserRole.RECEPTIONIST.canAccessClinicalNotes()).isFalse();
            assertThat(UserRole.DENTIST.canAccessClinicalNotes()).isTrue();
            assertThat(UserRole.ADMINISTRATOR.canAccessClinicalNotes()).isTrue(); // Admin has read-only access
        }

        @Test
        @DisplayName("Should enforce that ADMINISTRATOR cannot modify clinical records")
        void shouldEnforceThatAdministratorCannotModifyClinicalRecords() {
            assertThat(UserRole.ADMINISTRATOR.canModifyClinicalRecords()).isFalse();
            assertThat(UserRole.DENTIST.canModifyClinicalRecords()).isTrue();
            assertThat(UserRole.RECEPTIONIST.canModifyClinicalRecords()).isFalse();
        }

        @Test
        @DisplayName("Should enforce access logging requirement for sensitive data")
        void shouldEnforceAccessLoggingRequirementForSensitiveData() {
            assertThat(UserRole.RECEPTIONIST.requiresAccessLoggingForSensitiveData()).isTrue();
            assertThat(UserRole.DENTIST.requiresAccessLoggingForSensitiveData()).isTrue();
            assertThat(UserRole.ADMINISTRATOR.requiresAccessLoggingForSensitiveData()).isTrue(); // All roles require logging
        }
    }
}