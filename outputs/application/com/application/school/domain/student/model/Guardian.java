package com.application.school.domain.student.model;

import com.application.school.domain.shared.valueobject.PersonalName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Guardian {
    private GuardianId guardianId;
    private PersonalName name;
    private String email;
    private String phoneNumber;
    private String relationship;
}