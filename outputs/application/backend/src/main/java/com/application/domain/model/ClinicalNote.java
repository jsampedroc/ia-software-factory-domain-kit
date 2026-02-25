package com.application.domain.model;

import com.application.domain.model.base.AggregateRoot;
import com.application.domain.model.valueobject.ClinicalNoteId;
import com.application.domain.model.valueobject.PatientId;
import com.application.domain.model.valueobject.DentistId;
import com.application.domain.model.valueobject.ClinicalNoteContent;
import com.application.domain.model.valueobject.ClinicalNoteDate;

import java.time.LocalDateTime;

public class ClinicalNote extends AggregateRoot<ClinicalNoteId> {
    private PatientId patientId;
    private DentistId dentistId;
    private ClinicalNoteContent content;
    private ClinicalNoteDate noteDate;

    public ClinicalNote(ClinicalNoteId id, PatientId patientId, DentistId dentistId, ClinicalNoteContent content, ClinicalNoteDate noteDate) {
        super(id);
        this.patientId = patientId;
        this.dentistId = dentistId;
        this.content = content;
        this.noteDate = noteDate;
    }

    public PatientId getPatientId() {
        return patientId;
    }

    public DentistId getDentistId() {
        return dentistId;
    }

    public ClinicalNoteContent getContent() {
        return content;
    }

    public ClinicalNoteDate getNoteDate() {
        return noteDate;
    }

    public void updateContent(ClinicalNoteContent newContent) {
        this.content = newContent;
        this.registerEvent(new ClinicalNoteUpdatedEvent(this.getId(), LocalDateTime.now()));
    }

    // Event class defined internally to avoid missing dependency
    public static class ClinicalNoteUpdatedEvent {
        private final ClinicalNoteId noteId;
        private final LocalDateTime updatedAt;

        public ClinicalNoteUpdatedEvent(ClinicalNoteId noteId, LocalDateTime updatedAt) {
            this.noteId = noteId;
            this.updatedAt = updatedAt;
        }

        public ClinicalNoteId getNoteId() {
            return noteId;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }
    }
}