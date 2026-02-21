package com.application.infrastructure.persistence.SchoolManagement.student.mapper;

import com.application.domain.SchoolManagement.student.domain.Student;
import com.application.domain.SchoolManagement.student.domain.LegalGuardian;
import com.application.domain.SchoolManagement.valueobject.StudentId;
import com.application.domain.SchoolManagement.valueobject.LegalGuardianId;
import com.application.domain.SchoolManagement.valueobject.ClassroomId;
import com.application.domain.shared.valueobject.PersonName;
import com.application.infrastructure.persistence.SchoolManagement.student.entity.StudentEntity;
import com.application.infrastructure.persistence.SchoolManagement.student.entity.LegalGuardianEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StudentEntityMapper {

    public Student toDomain(StudentEntity entity) {
        if (entity == null) {
            return null;
        }

        LegalGuardian legalGuardianDomain = null;
        if (entity.getLegalGuardian() != null) {
            legalGuardianDomain = toLegalGuardianDomain(entity.getLegalGuardian());
        }

        return Student.builder()
                .id(new StudentId(entity.getId()))
                .legalGuardianId(new LegalGuardianId(entity.getLegalGuardianId()))
                .name(new PersonName(entity.getFirstName(), entity.getLastName()))
                .dateOfBirth(entity.getDateOfBirth())
                .identificationNumber(entity.getIdentificationNumber())
                .enrollmentDate(entity.getEnrollmentDate())
                .active(entity.isActive())
                .currentClassroomId(entity.getCurrentClassroomId() != null ? new ClassroomId(entity.getCurrentClassroomId()) : null)
                .legalGuardian(legalGuardianDomain)
                .build();
    }

    public StudentEntity toEntity(Student domain) {
        if (domain == null) {
            return null;
        }

        StudentEntity entity = new StudentEntity();
        entity.setId(domain.getId() != null ? domain.getId().getValue() : UUID.randomUUID());
        entity.setLegalGuardianId(domain.getLegalGuardianId().getValue());
        entity.setFirstName(domain.getName().firstName());
        entity.setLastName(domain.getName().lastName());
        entity.setDateOfBirth(domain.getDateOfBirth());
        entity.setIdentificationNumber(domain.getIdentificationNumber());
        entity.setEnrollmentDate(domain.getEnrollmentDate());
        entity.setActive(domain.isActive());
        entity.setCurrentClassroomId(domain.getCurrentClassroomId() != null ? domain.getCurrentClassroomId().getValue() : null);

        if (domain.getLegalGuardian() != null) {
            entity.setLegalGuardian(toLegalGuardianEntity(domain.getLegalGuardian()));
        }

        return entity;
    }

    private LegalGuardian toLegalGuardianDomain(LegalGuardianEntity entity) {
        if (entity == null) {
            return null;
        }
        return LegalGuardian.builder()
                .id(new LegalGuardianId(entity.getId()))
                .name(new PersonName(entity.getFirstName(), entity.getLastName()))
                .email(entity.getEmail())
                .primaryPhone(entity.getPrimaryPhone())
                .secondaryPhone(entity.getSecondaryPhone())
                .address(entity.getAddress())
                .build();
    }

    private LegalGuardianEntity toLegalGuardianEntity(LegalGuardian domain) {
        if (domain == null) {
            return null;
        }
        LegalGuardianEntity entity = new LegalGuardianEntity();
        entity.setId(domain.getId() != null ? domain.getId().getValue() : UUID.randomUUID());
        entity.setFirstName(domain.getName().firstName());
        entity.setLastName(domain.getName().lastName());
        entity.setEmail(domain.getEmail());
        entity.setPrimaryPhone(domain.getPrimaryPhone());
        entity.setSecondaryPhone(domain.getSecondaryPhone());
        entity.setAddress(domain.getAddress());
        return entity;
    }
}