package com.application.domain.model.studentmanagement;

import com.application.domain.enums.RelationshipType;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class Guardianship {
    LegalGuardianId guardianId;
    RelationshipType relationship;
    Boolean isPrimary;
    LocalDate assignedDate;
    String responsibilities;
}