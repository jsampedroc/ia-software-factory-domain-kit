package com.application.domain.model.studentmanagement;

import com.application.domain.valueobject.Address;
import com.application.domain.enums.RelationshipType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LegalGuardian {
    private LegalGuardianId guardianId;
    private String firstName;
    private String lastName;
    private RelationshipType relationship;
    private String email;
    private String primaryPhone;
    private String secondaryPhone;
    private Address address;
}