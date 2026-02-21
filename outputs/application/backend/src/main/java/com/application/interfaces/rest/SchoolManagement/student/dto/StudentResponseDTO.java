package com.application.interfaces.rest.SchoolManagement.student.dto;

import com.application.domain.SchoolManagement.student.domain.Student;
import com.application.domain.SchoolManagement.valueobject.StudentId;
import com.application.domain.SchoolManagement.valueobject.LegalGuardianId;
import com.application.domain.SchoolManagement.valueobject.ClassroomId;
import lombok.Data;
import java.time.LocalDate;

@Data
public class StudentResponseDTO {
    private StudentId id;
    private LegalGuardianId legalGuardianId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String identificationNumber;
    private LocalDate enrollmentDate;
    private Boolean active;
    private ClassroomId currentClassroomId;

    public static StudentResponseDTO fromDomain(Student student) {
        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(student.getId());
        dto.setLegalGuardianId(student.getLegalGuardianId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setDateOfBirth(student.getDateOfBirth());
        dto.setIdentificationNumber(student.getIdentificationNumber());
        dto.setEnrollmentDate(student.getEnrollmentDate());
        dto.setActive(student.getActive());
        dto.setCurrentClassroomId(student.getCurrentClassroomId());
        return dto;
    }
}