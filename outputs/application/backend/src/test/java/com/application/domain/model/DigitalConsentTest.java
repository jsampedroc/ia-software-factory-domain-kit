package com.application.domain.model;

import com.application.domain.valueobject.ConsentId;
import com.application.domain.enums.ConsentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("DigitalConsent Entity Unit Tests")
class DigitalConsentTest {

    private final ConsentId TEST_CONSENT_ID = new ConsentId(UUID.randomUUID());
    private final ConsentType TEST_CONSENT_TYPE = ConsentType.TREATMENT;
    private final String TEST_VERSION = "v2.1";
    private final String TEST_CONTENT = "I consent to the proposed dental treatment.";
    private final String TEST_GIVEN_BY = "John Doe";
    private final LocalDateTime TEST_GIVEN_AT = LocalDateTime.now().minusDays(1);

    @Test
    @DisplayName("Should create DigitalConsent with valid parameters")
    void shouldCreateDigitalConsent() {
        // When
        DigitalConsent consent = new DigitalConsent(
                TEST_CONSENT_ID,
                TEST_CONSENT_TYPE,
                TEST_VERSION,
                TEST_CONTENT,
                TEST_GIVEN_BY,
                TEST_GIVEN_AT
        );

        // Then
        assertThat(consent).isNotNull();
        assertThat(consent.getId()).isEqualTo(TEST_CONSENT_ID);
        assertThat(consent.getConsentType()).isEqualTo(TEST_CONSENT_TYPE);
        assertThat(consent.getVersion()).isEqualTo(TEST_VERSION);
        assertThat(consent.getContent()).isEqualTo(TEST_CONTENT);
        assertThat(consent.getGivenBy()).isEqualTo(TEST_GIVEN_BY);
        assertThat(consent.getGivenAt()).isEqualTo(TEST_GIVEN_AT);
        assertThat(consent.isRevoked()).isFalse();
        assertThat(consent.getRevokedAt()).isNull();
        assertThat(consent.isActive()).isTrue();
    }

    @Test
    @DisplayName("Should throw exception when consent type is null")
    void shouldThrowExceptionWhenConsentTypeIsNull() {
        assertThatThrownBy(() -> new DigitalConsent(
                TEST_CONSENT_ID,
                null,
                TEST_VERSION,
                TEST_CONTENT,
                TEST_GIVEN_BY,
                TEST_GIVEN_AT
        ))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Consent type cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when version is null")
    void shouldThrowExceptionWhenVersionIsNull() {
        assertThatThrownBy(() -> new DigitalConsent(
                TEST_CONSENT_ID,
                TEST_CONSENT_TYPE,
                null,
                TEST_CONTENT,
                TEST_GIVEN_BY,
                TEST_GIVEN_AT
        ))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Version cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when content is null")
    void shouldThrowExceptionWhenContentIsNull() {
        assertThatThrownBy(() -> new DigitalConsent(
                TEST_CONSENT_ID,
                TEST_CONSENT_TYPE,
                TEST_VERSION,
                null,
                TEST_GIVEN_BY,
                TEST_GIVEN_AT
        ))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Content cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when givenBy is null")
    void shouldThrowExceptionWhenGivenByIsNull() {
        assertThatThrownBy(() -> new DigitalConsent(
                TEST_CONSENT_ID,
                TEST_CONSENT_TYPE,
                TEST_VERSION,
                TEST_CONTENT,
                null,
                TEST_GIVEN_AT
        ))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Given by cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when givenAt is null")
    void shouldThrowExceptionWhenGivenAtIsNull() {
        assertThatThrownBy(() -> new DigitalConsent(
                TEST_CONSENT_ID,
                TEST_CONSENT_TYPE,
                TEST_VERSION,
                TEST_CONTENT,
                TEST_GIVEN_BY,
                null
        ))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Given at cannot be null");
    }

    @Nested
    @DisplayName("Revocation Tests")
    class RevocationTests {

        @Test
        @DisplayName("Should revoke consent successfully")
        void shouldRevokeConsent() {
            // Given
            DigitalConsent consent = new DigitalConsent(
                    TEST_CONSENT_ID,
                    TEST_CONSENT_TYPE,
                    TEST_VERSION,
                    TEST_CONTENT,
                    TEST_GIVEN_BY,
                    TEST_GIVEN_AT
            );

            // When
            consent.revoke();

            // Then
            assertThat(consent.isRevoked()).isTrue();
            assertThat(consent.getRevokedAt()).isNotNull();
            assertThat(consent.isActive()).isFalse();
            assertThat(consent.getRevokedAt()).isAfter(TEST_GIVEN_AT);
        }

        @Test
        @DisplayName("Should not revoke already revoked consent")
        void shouldNotRevokeAlreadyRevokedConsent() {
            // Given
            DigitalConsent consent = new DigitalConsent(
                    TEST_CONSENT_ID,
                    TEST_CONSENT_TYPE,
                    TEST_VERSION,
                    TEST_CONTENT,
                    TEST_GIVEN_BY,
                    TEST_GIVEN_AT
            );
            consent.revoke();
            LocalDateTime firstRevocationTime = consent.getRevokedAt();

            // When
            consent.revoke();

            // Then
            assertThat(consent.isRevoked()).isTrue();
            assertThat(consent.getRevokedAt()).isEqualTo(firstRevocationTime);
        }

        @Test
        @DisplayName("Should return correct active status")
        void shouldReturnCorrectActiveStatus() {
            // Given
            DigitalConsent activeConsent = new DigitalConsent(
                    TEST_CONSENT_ID,
                    TEST_CONSENT_TYPE,
                    TEST_VERSION,
                    TEST_CONTENT,
                    TEST_GIVEN_BY,
                    TEST_GIVEN_AT
            );
            DigitalConsent revokedConsent = new DigitalConsent(
                    new ConsentId(UUID.randomUUID()),
                    TEST_CONSENT_TYPE,
                    TEST_VERSION,
                    TEST_CONTENT,
                    TEST_GIVEN_BY,
                    TEST_GIVEN_AT
            );
            revokedConsent.revoke();

            // Then
            assertThat(activeConsent.isActive()).isTrue();
            assertThat(revokedConsent.isActive()).isFalse();
        }
    }

    @Nested
    @DisplayName("Equality and HashCode Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal when IDs are equal")
        void shouldBeEqualWhenIdsAreEqual() {
            // Given
            ConsentId sameId = new ConsentId(UUID.randomUUID());
            DigitalConsent consent1 = new DigitalConsent(
                    sameId,
                    TEST_CONSENT_TYPE,
                    TEST_VERSION,
                    TEST_CONTENT,
                    TEST_GIVEN_BY,
                    TEST_GIVEN_AT
            );
            DigitalConsent consent2 = new DigitalConsent(
                    sameId,
                    ConsentType.DATA_USAGE,
                    "v1.0",
                    "Different content",
                    "Jane Doe",
                    LocalDateTime.now()
            );

            // Then
            assertThat(consent1).isEqualTo(consent2);
            assertThat(consent1.hashCode()).isEqualTo(consent2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when IDs are different")
        void shouldNotBeEqualWhenIdsAreDifferent() {
            // Given
            DigitalConsent consent1 = new DigitalConsent(
                    new ConsentId(UUID.randomUUID()),
                    TEST_CONSENT_TYPE,
                    TEST_VERSION,
                    TEST_CONTENT,
                    TEST_GIVEN_BY,
                    TEST_GIVEN_AT
            );
            DigitalConsent consent2 = new DigitalConsent(
                    new ConsentId(UUID.randomUUID()),
                    TEST_CONSENT_TYPE,
                    TEST_VERSION,
                    TEST_CONTENT,
                    TEST_GIVEN_BY,
                    TEST_GIVEN_AT
            );

            // Then
            assertThat(consent1).isNotEqualTo(consent2);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            // Given
            DigitalConsent consent = new DigitalConsent(
                    TEST_CONSENT_ID,
                    TEST_CONSENT_TYPE,
                    TEST_VERSION,
                    TEST_CONTENT,
                    TEST_GIVEN_BY,
                    TEST_GIVEN_AT
            );

            // Then
            assertThat(consent).isNotEqualTo(null);
        }

        @Test
        @DisplayName("Should not be equal to different class")
        void shouldNotBeEqualToDifferentClass() {
            // Given
            DigitalConsent consent = new DigitalConsent(
                    TEST_CONSENT_ID,
                    TEST_CONSENT_TYPE,
                    TEST_VERSION,
                    TEST_CONTENT,
                    TEST_GIVEN_BY,
                    TEST_GIVEN_AT
            );

            // Then
            assertThat(consent).isNotEqualTo("Not a consent");
        }

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            // Given
            DigitalConsent consent = new DigitalConsent(
                    TEST_CONSENT_ID,
                    TEST_CONSENT_TYPE,
                    TEST_VERSION,
                    TEST_CONTENT,
                    TEST_GIVEN_BY,
                    TEST_GIVEN_AT
            );

            // Then
            assertThat(consent).isEqualTo(consent);
        }
    }

    @Nested
    @DisplayName("Business Rule Validation Tests")
    class BusinessRuleTests {

        @Test
        @DisplayName("Should support all consent types")
        void shouldSupportAllConsentTypes() {
            for (ConsentType type : ConsentType.values()) {
                DigitalConsent consent = new DigitalConsent(
                        new ConsentId(UUID.randomUUID()),
                        type,
                        TEST_VERSION,
                        TEST_CONTENT,
                        TEST_GIVEN_BY,
                        TEST_GIVEN_AT
                );

                assertThat(consent.getConsentType()).isEqualTo(type);
            }
        }

        @Test
        @DisplayName("Should maintain immutability of final fields")
        void shouldMaintainImmutability() {
            DigitalConsent consent = new DigitalConsent(
                    TEST_CONSENT_ID,
                    TEST_CONSENT_TYPE,
                    TEST_VERSION,
                    TEST_CONTENT,
                    TEST_GIVEN_BY,
                    TEST_GIVEN_AT
            );

            assertThat(consent.getConsentType()).isEqualTo(TEST_CONSENT_TYPE);
            assertThat(consent.getVersion()).isEqualTo(TEST_VERSION);
            assertThat(consent.getContent()).isEqualTo(TEST_CONTENT);
            assertThat(consent.getGivenBy()).isEqualTo(TEST_GIVEN_BY);
            assertThat(consent.getGivenAt()).isEqualTo(TEST_GIVEN_AT);
        }

        @Test
        @DisplayName("Should have proper toString representation")
        void shouldHaveProperToString() {
            DigitalConsent consent = new DigitalConsent(
                    TEST_CONSENT_ID,
                    TEST_CONSENT_TYPE,
                    TEST_VERSION,
                    TEST_CONTENT,
                    TEST_GIVEN_BY,
                    TEST_GIVEN_AT
            );

            String toString = consent.toString();
            assertThat(toString).contains(TEST_CONSENT_ID.toString());
            assertThat(toString).contains(TEST_CONSENT_TYPE.name());
            assertThat(toString).contains(TEST_VERSION);
        }
    }
}