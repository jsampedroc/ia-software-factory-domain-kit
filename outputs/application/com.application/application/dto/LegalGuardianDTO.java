package com.application.application.dto;

import com.application.domain.enums.RelationshipType;
import com.application.domain.valueobject.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LegalGuardianDTO {
    private String guardianId;
    private String firstName;
    private String lastName;
    private RelationshipType relationship;
    private String email;
    private String primaryPhone;
    private String secondaryPhone;
    private Address address;
    private Boolean isPrimary;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}