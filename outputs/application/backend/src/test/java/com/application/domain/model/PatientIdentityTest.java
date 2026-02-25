package com.application.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("PatientIdentity Value Object Unit Tests")
class PatientIdentityTest {

    private static final LocalDate VALID_PAST_DATE = LocalDate.now().minusYears(30);
    private static final String VALID_EMAIL = "john.doe@example.com";
    private static final String VALID_NATIONAL_ID = "ID123456";
    private static final String VALID_PHONE = "+1234567890";
    private static final String VALID_ADDRESS = "123 Main St, City, Country";

    @Test
    @DisplayName("Should create PatientIdentity with valid data")
    void shouldCreatePatientIdentityWithValidData() {
        // Given
        String firstName = "John";
        String lastName = "Doe";

        // When
        PatientIdentity identity = new PatientIdentity(
                firstName,
                lastName,
                VALID_PAST_DATE,
                VALID_NATIONAL_ID,
                VALID_EMAIL,
                VALID_PHONE,
                VALID_ADDRESS
        );

        // Then
        assertThat(identity).isNotNull();
        assertThat(identity.firstName()).isEqualTo(firstName);
        assertThat(identity.lastName()).isEqualTo(lastName);
        assertThat(identity.dateOfBirth()).isEqualTo(VALID_PAST_DATE);
        assertThat(identity.nationalId()).isEqualTo(VALID_NATIONAL_ID);
        assertThat(identity.email()).isEqualTo(VALID_EMAIL);
        assertThat(identity.phoneNumber()).isEqualTo(VALID_PHONE);
        assertThat(identity.address()).isEqualTo(VALID_ADDRESS);
    }

    @Nested
    @DisplayName("Constructor Validation Tests")
    class ConstructorValidationTests {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("Should reject blank or null first name")
        void shouldRejectInvalidFirstName(String invalidFirstName) {
            assertThatThrownBy(() -> new PatientIdentity(
                    invalidFirstName,
                    "Doe",
                    VALID_PAST_DATE,
                    VALID_NATIONAL_ID,
                    VALID_EMAIL,
                    VALID_PHONE,
                    VALID_ADDRESS
            ))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("First name");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("Should reject blank or null last name")
        void shouldRejectInvalidLastName(String invalidLastName) {
            assertThatThrownBy(() -> new PatientIdentity(
                    "John",
                    invalidLastName,
                    VALID_PAST_DATE,
                    VALID_NATIONAL_ID,
                    VALID_EMAIL,
                    VALID_PHONE,
                    VALID_ADDRESS
            ))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Last name");
        }

        @Test
        @DisplayName("Should reject null date of birth")
        void shouldRejectNullDateOfBirth() {
            assertThatThrownBy(() -> new PatientIdentity(
                    "John",
                    "Doe",
                    null,
                    VALID_NATIONAL_ID,
                    VALID_EMAIL,
                    VALID_PHONE,
                    VALID_ADDRESS
            ))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("Date of birth cannot be null");
        }

        @Test
        @DisplayName("Should reject future date of birth")
        void shouldRejectFutureDateOfBirth() {
            LocalDate futureDate = LocalDate.now().plusDays(1);

            assertThatThrownBy(() -> new PatientIdentity(
                    "John",
                    "Doe",
                    futureDate,
                    VALID_NATIONAL_ID,
                    VALID_EMAIL,
                    VALID_PHONE,
                    VALID_ADDRESS
            ))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Date of birth cannot be in the future");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("Should reject blank or null national ID")
        void shouldRejectInvalidNationalId(String invalidNationalId) {
            assertThatThrownBy(() -> new PatientIdentity(
                    "John",
                    "Doe",
                    VALID_PAST_DATE,
                    invalidNationalId,
                    VALID_EMAIL,
                    VALID_PHONE,
                    VALID_ADDRESS
            ))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("National ID");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("Should reject blank or null email")
        void shouldRejectInvalidEmail(String invalidEmail) {
            assertThatThrownBy(() -> new PatientIdentity(
                    "John",
                    "Doe",
                    VALID_PAST_DATE,
                    VALID_NATIONAL_ID,
                    invalidEmail,
                    VALID_PHONE,
                    VALID_ADDRESS
            ))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Email");
        }

        @ParameterizedTest
        @ValueSource(strings = {"invalid", "missing.at.com", "@nodomain", "spaces @test.com"})
        @DisplayName("Should reject email without @ symbol")
        void shouldRejectEmailWithoutAtSymbol(String invalidEmail) {
            assertThatThrownBy(() -> new PatientIdentity(
                    "John",
                    "Doe",
                    VALID_PAST_DATE,
                    VALID_NATIONAL_ID,
                    invalidEmail,
                    VALID_PHONE,
                    VALID_ADDRESS
            ))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Email must be valid");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("Should reject blank or null phone number")
        void shouldRejectInvalidPhoneNumber(String invalidPhone) {
            assertThatThrownBy(() -> new PatientIdentity(
                    "John",
                    "Doe",
                    VALID_PAST_DATE,
                    VALID_NATIONAL_ID,
                    VALID_EMAIL,
                    invalidPhone,
                    VALID_ADDRESS
            ))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Phone number");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("Should reject blank or null address")
        void shouldRejectInvalidAddress(String invalidAddress) {
            assertThatThrownBy(() -> new PatientIdentity(
                    "John",
                    "Doe",
                    VALID_PAST_DATE,
                    VALID_NATIONAL_ID,
                    VALID_EMAIL,
                    VALID_PHONE,
                    invalidAddress
            ))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Address");
        }
    }

    @Nested
    @DisplayName("Business Method Tests")
    class BusinessMethodTests {

        @Test
        @DisplayName("getFullName should return concatenated first and last name")
        void getFullNameShouldReturnConcatenatedName() {
            // Given
            PatientIdentity identity = new PatientIdentity(
                    "John",
                    "Doe",
                    VALID_PAST_DATE,
                    VALID_NATIONAL_ID,
                    VALID_EMAIL,
                    VALID_PHONE,
                    VALID_ADDRESS
            );

            // When
            String fullName = identity.getFullName();

            // Then
            assertThat(fullName).isEqualTo("John Doe");
        }

        @ParameterizedTest
        @CsvSource({
                "2000-01-01, true",  // Adult (24 years old if today is 2024)
                "2010-01-01, false", // Child (14 years old)
                "2006-01-01, true"   // Exactly 18 today (if today is 2024-01-01)
        })
        @DisplayName("isAdult should correctly determine adult status based on date of birth")
        void isAdultShouldCorrectlyDetermineAdultStatus(LocalDate dateOfBirth, boolean expectedAdult) {
            // Given
            PatientIdentity identity = new PatientIdentity(
                    "John",
                    "Doe",
                    dateOfBirth,
                    VALID_NATIONAL_ID,
                    VALID_EMAIL,
                    VALID_PHONE,
                    VALID_ADDRESS
            );

            // When
            boolean isAdult = identity.isAdult();

            // Then
            assertThat(isAdult).isEqualTo(expectedAdult);
        }

        @Test
        @DisplayName("isAdult should return false for birthdate exactly 18 years ago today")
        void isAdultShouldReturnFalseForExactly18YearsAgoToday() {
            // Given
            LocalDate exactly18YearsAgo = LocalDate.now().minusYears(18);
            PatientIdentity identity = new PatientIdentity(
                    "John",
                    "Doe",
                    exactly18YearsAgo,
                    VALID_NATIONAL_ID,
                    VALID_EMAIL,
                    VALID_PHONE,
                    VALID_ADDRESS
            );

            // When
            boolean isAdult = identity.isAdult();

            // Then
            assertThat(isAdult).isFalse(); // Because plusYears(18) is exactly today, not before
        }
    }

    @Nested
    @DisplayName("Value Object Contract Tests")
    class ValueObjectContractTests {

        @Test
        @DisplayName("Should implement ValueObject interface")
        void shouldImplementValueObjectInterface() {
            // Given
            PatientIdentity identity = new PatientIdentity(
                    "John",
                    "Doe",
                    VALID_PAST_DATE,
                    VALID_NATIONAL_ID,
                    VALID_EMAIL,
                    VALID_PHONE,
                    VALID_ADDRESS
            );

            // Then
            assertThat(identity).isInstanceOf(ValueObject.class);
        }

        @Test
        @DisplayName("Equals and hashCode should be based on all fields")
        void equalsAndHashCodeShouldBeBasedOnAllFields() {
            // Given
            PatientIdentity identity1 = new PatientIdentity(
                    "John",
                    "Doe",
                    VALID_PAST_DATE,
                    VALID_NATIONAL_ID,
                    VALID_EMAIL,
                    VALID_PHONE,
                    VALID_ADDRESS
            );

            PatientIdentity identity2 = new PatientIdentity(
                    "John",
                    "Doe",
                    VALID_PAST_DATE,
                    VALID_NATIONAL_ID,
                    VALID_EMAIL,
                    VALID_PHONE,
                    VALID_ADDRESS
            );

            // Then
            assertThat(identity1).isEqualTo(identity2);
            assertThat(identity1.hashCode()).isEqualTo(identity2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when any field differs")
        void shouldNotBeEqualWhenAnyFieldDiffers() {
            // Given
            PatientIdentity baseIdentity = new PatientIdentity(
                    "John",
                    "Doe",
                    VALID_PAST_DATE,
                    VALID_NATIONAL_ID,
                    VALID_EMAIL,
                    VALID_PHONE,
                    VALID_ADDRESS
            );

            PatientIdentity differentFirstName = new PatientIdentity(
                    "Jane",
                    "Doe",
                    VALID_PAST_DATE,
                    VALID_NATIONAL_ID,
                    VALID_EMAIL,
                    VALID_PHONE,
                    VALID_ADDRESS
            );

            // Then
            assertThat(baseIdentity).isNotEqualTo(differentFirstName);
        }

        @Test
        @DisplayName("toString should include all fields")
        void toStringShouldIncludeAllFields() {
            // Given
            PatientIdentity identity = new PatientIdentity(
                    "John",
                    "Doe",
                    VALID_PAST_DATE,
                    VALID_NATIONAL_ID,
                    VALID_EMAIL,
                    VALID_PHONE,
                    VALID_ADDRESS
            );

            // When
            String toString = identity.toString();

            // Then
            assertThat(toString).contains("John");
            assertThat(toString).contains("Doe");
            assertThat(toString).contains(VALID_PAST_DATE.toString());
            assertThat(toString).contains(VALID_NATIONAL_ID);
            assertThat(toString).contains(VALID_EMAIL);
            assertThat(toString).contains(VALID_PHONE);
            assertThat(toString).contains(VALID_ADDRESS);
        }
    }

    @Test
    @DisplayName("Should accept valid email formats")
    void shouldAcceptValidEmailFormats() {
        String[] validEmails = {
                "test@example.com",
                "test.name@example.co.uk",
                "test+tag@example.org",
                "123@example.com",
                "test_name@example-domain.com"
        };

        for (String validEmail : validEmails) {
            assertThat(new PatientIdentity(
                    "John",
                    "Doe",
                    VALID_PAST_DATE,
                    VALID_NATIONAL_ID,
                    validEmail,
                    VALID_PHONE,
                    VALID_ADDRESS
            )).isNotNull();
        }
    }

    @Test
    @DisplayName("Should handle edge case: date of birth is today")
    void shouldHandleDateOfBirthIsToday() {
        LocalDate today = LocalDate.now();

        PatientIdentity identity = new PatientIdentity(
                "John",
                "Doe",
                today,
                VALID_NATIONAL_ID,
                VALID_EMAIL,
                VALID_PHONE,
                VALID_ADDRESS
        );

        assertThat(identity.dateOfBirth()).isEqualTo(today);
        assertThat(identity.isAdult()).isFalse();
    }
}