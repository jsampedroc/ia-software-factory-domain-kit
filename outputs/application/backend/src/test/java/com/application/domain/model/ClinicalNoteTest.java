package com.application.domain.model;

import com.application.domain.valueobject.ClinicalNoteId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class ClinicalNoteTest {

    private final UUID SAMPLE_DENTIST_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private final LocalDate SAMPLE_DATE = LocalDate.of(2024, 1, 15);
    private final LocalDateTime SAMPLE_CREATED_AT = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
    private final String SAMPLE_CONTENT = "Patient presented with moderate caries on tooth #19. Recommended composite filling.";

    @Nested
    @DisplayName("Creation and Instantiation")
    class CreationTests {

        @Test
        @DisplayName("Should successfully create a ClinicalNote with all required fields")
        void shouldCreateClinicalNoteSuccessfully() {
            ClinicalNoteId noteId = new ClinicalNoteId(UUID.randomUUID());

            ClinicalNote note = new ClinicalNote(noteId, SAMPLE_DENTIST_ID, SAMPLE_CONTENT, SAMPLE_DATE, SAMPLE_CREATED_AT);

            assertThat(note).isNotNull();
            assertThat(note.getNoteId()).isEqualTo(noteId);
            assertThat(note.getDentistId()).isEqualTo(SAMPLE_DENTIST_ID);
            assertThat(note.getContent()).isEqualTo(SAMPLE_CONTENT);
            assertThat(note.getDate()).isEqualTo(SAMPLE_DATE);
            assertThat(note.getCreatedAt()).isEqualTo(SAMPLE_CREATED_AT);
        }

        @Test
        @DisplayName("Should throw NullPointerException when ClinicalNoteId is null")
        void shouldThrowExceptionWhenIdIsNull() {
            assertThatThrownBy(() ->
                    new ClinicalNote(null, SAMPLE_DENTIST_ID, SAMPLE_CONTENT, SAMPLE_DATE, SAMPLE_CREATED_AT)
            ).isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should throw NullPointerException when dentistId is null")
        void shouldThrowExceptionWhenDentistIdIsNull() {
            ClinicalNoteId noteId = new ClinicalNoteId(UUID.randomUUID());

            assertThatThrownBy(() ->
                    new ClinicalNote(noteId, null, SAMPLE_CONTENT, SAMPLE_DATE, SAMPLE_CREATED_AT)
            ).isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should throw NullPointerException when content is null")
        void shouldThrowExceptionWhenContentIsNull() {
            ClinicalNoteId noteId = new ClinicalNoteId(UUID.randomUUID());

            assertThatThrownBy(() ->
                    new ClinicalNote(noteId, SAMPLE_DENTIST_ID, null, SAMPLE_DATE, SAMPLE_CREATED_AT)
            ).isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should throw NullPointerException when date is null")
        void shouldThrowExceptionWhenDateIsNull() {
            ClinicalNoteId noteId = new ClinicalNoteId(UUID.randomUUID());

            assertThatThrownBy(() ->
                    new ClinicalNote(noteId, SAMPLE_DENTIST_ID, SAMPLE_CONTENT, null, SAMPLE_CREATED_AT)
            ).isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should throw NullPointerException when createdAt is null")
        void shouldThrowExceptionWhenCreatedAtIsNull() {
            ClinicalNoteId noteId = new ClinicalNoteId(UUID.randomUUID());

            assertThatThrownBy(() ->
                    new ClinicalNote(noteId, SAMPLE_DENTIST_ID, SAMPLE_CONTENT, SAMPLE_DATE, null)
            ).isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should accept empty content string")
        void shouldAcceptEmptyContent() {
            ClinicalNoteId noteId = new ClinicalNoteId(UUID.randomUUID());
            String emptyContent = "";

            ClinicalNote note = new ClinicalNote(noteId, SAMPLE_DENTIST_ID, emptyContent, SAMPLE_DATE, SAMPLE_CREATED_AT);

            assertThat(note.getContent()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Entity Identity and Equality")
    class IdentityTests {

        @Test
        @DisplayName("Two ClinicalNotes with the same ID should be equal")
        void shouldBeEqualWhenIdsAreSame() {
            ClinicalNoteId sharedId = new ClinicalNoteId(UUID.randomUUID());
            UUID differentDentistId = UUID.fromString("223e4567-e89b-12d3-a456-426614174001");
            LocalDate differentDate = LocalDate.of(2024, 2, 1);

            ClinicalNote note1 = new ClinicalNote(sharedId, SAMPLE_DENTIST_ID, SAMPLE_CONTENT, SAMPLE_DATE, SAMPLE_CREATED_AT);
            ClinicalNote note2 = new ClinicalNote(sharedId, differentDentistId, "Different content", differentDate, SAMPLE_CREATED_AT.plusHours(1));

            assertThat(note1).isEqualTo(note2);
            assertThat(note1.hashCode()).isEqualTo(note2.hashCode());
        }

        @Test
        @DisplayName("Two ClinicalNotes with different IDs should not be equal")
        void shouldNotBeEqualWhenIdsAreDifferent() {
            ClinicalNoteId id1 = new ClinicalNoteId(UUID.randomUUID());
            ClinicalNoteId id2 = new ClinicalNoteId(UUID.randomUUID());

            ClinicalNote note1 = new ClinicalNote(id1, SAMPLE_DENTIST_ID, SAMPLE_CONTENT, SAMPLE_DATE, SAMPLE_CREATED_AT);
            ClinicalNote note2 = new ClinicalNote(id2, SAMPLE_DENTIST_ID, SAMPLE_CONTENT, SAMPLE_DATE, SAMPLE_CREATED_AT);

            assertThat(note1).isNotEqualTo(note2);
        }

        @Test
        @DisplayName("getNoteId() should return the same as getId()")
        void getNoteIdShouldReturnId() {
            ClinicalNoteId noteId = new ClinicalNoteId(UUID.randomUUID());
            ClinicalNote note = new ClinicalNote(noteId, SAMPLE_DENTIST_ID, SAMPLE_CONTENT, SAMPLE_DATE, SAMPLE_CREATED_AT);

            assertThat(note.getNoteId()).isSameAs(note.getId());
        }
    }

    @Nested
    @DisplayName("Value Object Properties")
    class ValueObjectTests {

        @Test
        @DisplayName("All fields should be immutable (final)")
        void fieldsShouldBeImmutable() throws NoSuchFieldException {
            assertThat(ClinicalNote.class.getDeclaredField("dentistId").getModifiers()).isFinal();
            assertThat(ClinicalNote.class.getDeclaredField("content").getModifiers()).isFinal();
            assertThat(ClinicalNote.class.getDeclaredField("date").getModifiers()).isFinal();
            assertThat(ClinicalNote.class.getDeclaredField("createdAt").getModifiers()).isFinal();
        }

        @Test
        @DisplayName("toString() should include all relevant fields")
        void toStringShouldIncludeRelevantFields() {
            ClinicalNoteId noteId = new ClinicalNoteId(UUID.fromString("333e4567-e89b-12d3-a456-426614174002"));
            ClinicalNote note = new ClinicalNote(noteId, SAMPLE_DENTIST_ID, SAMPLE_CONTENT, SAMPLE_DATE, SAMPLE_CREATED_AT);

            String toStringResult = note.toString();

            assertThat(toStringResult).contains(noteId.toString());
            assertThat(toStringResult).contains(SAMPLE_DENTIST_ID.toString());
            assertThat(toStringResult).contains(SAMPLE_CONTENT);
            assertThat(toStringResult).contains(SAMPLE_DATE.toString());
            assertThat(toStringResult).contains(SAMPLE_CREATED_AT.toString());
        }
    }

    @Nested
    @DisplayName("Business Rule Validation")
    class BusinessRuleTests {

        @Test
        @DisplayName("Should allow clinical notes with future dates (for planned follow-ups)")
        void shouldAllowFutureDates() {
            ClinicalNoteId noteId = new ClinicalNoteId(UUID.randomUUID());
            LocalDate futureDate = LocalDate.now().plusDays(7);
            LocalDateTime futureCreatedAt = LocalDateTime.now().plusMinutes(5);

            ClinicalNote note = new ClinicalNote(noteId, SAMPLE_DENTIST_ID, "Planned follow-up in one week.", futureDate, futureCreatedAt);

            assertThat(note.getDate()).isEqualTo(futureDate);
        }

        @Test
        @DisplayName("Should allow clinical notes with past dates (for retrospective documentation)")
        void shouldAllowPastDates() {
            ClinicalNoteId noteId = new ClinicalNoteId(UUID.randomUUID());
            LocalDate pastDate = LocalDate.now().minusDays(30);
            LocalDateTime pastCreatedAt = LocalDateTime.now().minusHours(2);

            ClinicalNote note = new ClinicalNote(noteId, SAMPLE_DENTIST_ID, "Retrospective note for previous visit.", pastDate, pastCreatedAt);

            assertThat(note.getDate()).isEqualTo(pastDate);
        }

        @Test
        @DisplayName("Should allow createdAt timestamp to be different from note date")
        void shouldAllowDifferentCreatedAtAndDate() {
            ClinicalNoteId noteId = new ClinicalNoteId(UUID.randomUUID());
            LocalDate noteDate = LocalDate.of(2024, 1, 10);
            LocalDateTime createdAt = LocalDateTime.of(2024, 1, 11, 9, 0);

            ClinicalNote note = new ClinicalNote(noteId, SAMPLE_DENTIST_ID, "Note documented the day after appointment.", noteDate, createdAt);

            assertThat(note.getDate()).isEqualTo(noteDate);
            assertThat(note.getCreatedAt()).isEqualTo(createdAt);
            assertThat(note.getCreatedAt().toLocalDate()).isNotEqualTo(note.getDate());
        }
    }
}