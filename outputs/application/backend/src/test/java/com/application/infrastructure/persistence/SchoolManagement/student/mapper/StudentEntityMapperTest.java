package com.application;

import com.application.domain.SchoolManagement.student.domain.Student;
import com.application.domain.SchoolManagement.student.domain.LegalGuardian;
import com.application.domain.SchoolManagement.valueobject.StudentId;
import com.application.domain.SchoolManagement.valueobject.LegalGuardianId;
import com.application.domain.SchoolManagement.valueobject.ClassroomId;
import com.application.domain.shared.valueobject.PersonName;
import com.application.infrastructure.persistence.SchoolManagement.student.entity.StudentEntity;
import com.application.infrastructure.persistence.SchoolManagement.student.entity.LegalGuardianEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentEntityMapperTest {

    @Mock
    private LegalGuardianEntity legalGuardianEntityMock;

    @InjectMocks
    private StudentEntityMapper mapper;

    @Test
    void toDomain_ShouldMapEntityToDomain() {
        UUID studentUuid = UUID.randomUUID();
        UUID guardianUuid = UUID.randomUUID();
        UUID classroomUuid = UUID.randomUUID();

        StudentEntity entity = new StudentEntity();
        entity.setId(studentUuid);
        entity.setLegalGuardianId(guardianUuid);
        entity.setFirstName("John");
        entity.setLastName("Doe");
        entity.setDateOfBirth(LocalDate.of(2015, 5, 10));
        entity.setIdentificationNumber("ID12345");
        entity.setEnrollmentDate(LocalDate.of(2023, 1, 15));
        entity.setActive(true);
        entity.setCurrentClassroomId(classroomUuid);

        LegalGuardianEntity guardianEntity = new LegalGuardianEntity();
        guardianEntity.setId(guardianUuid);
        guardianEntity.setFirstName("Jane");
        guardianEntity.setLastName("Doe");
        guardianEntity.setEmail("jane@example.com");
        guardianEntity.setPrimaryPhone("123456789");
        entity.setLegalGuardian(guardianEntity);

        Student result = mapper.toDomain(entity);

        assertNotNull(result);
        assertEquals(StudentId.of(studentUuid), result.getId());
        assertEquals(LegalGuardianId.of(guardianUuid), result.getLegalGuardianId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(LocalDate.of(2015, 5, 10), result.getDateOfBirth());
        assertEquals("ID12345", result.getIdentificationNumber());
        assertEquals(LocalDate.of(2023, 1, 15), result.getEnrollmentDate());
        assertTrue(result.isActive());
        assertEquals(ClassroomId.of(classroomUuid), result.getCurrentClassroomId());
        assertNotNull(result.getLegalGuardian());
        assertEquals(LegalGuardianId.of(guardianUuid), result.getLegalGuardian().getId());
        assertEquals(PersonName.of("Jane", "Doe"), result.getLegalGuardian().getName());
    }

    @Test
    void toDomain_ShouldHandleNullClassroomId() {
        UUID studentUuid = UUID.randomUUID();
        UUID guardianUuid = UUID.randomUUID();

        StudentEntity entity = new StudentEntity();
        entity.setId(studentUuid);
        entity.setLegalGuardianId(guardianUuid);
        entity.setFirstName("John");
        entity.setLastName("Doe");
        entity.setDateOfBirth(LocalDate.of(2015, 5, 10));
        entity.setIdentificationNumber("ID12345");
        entity.setEnrollmentDate(LocalDate.of(2023, 1, 15));
        entity.setActive(true);
        entity.setCurrentClassroomId(null);

        LegalGuardianEntity guardianEntity = new LegalGuardianEntity();
        guardianEntity.setId(guardianUuid);
        guardianEntity.setFirstName("Jane");
        guardianEntity.setLastName("Doe");
        guardianEntity.setEmail("jane@example.com");
        guardianEntity.setPrimaryPhone("123456789");
        entity.setLegalGuardian(guardianEntity);

        Student result = mapper.toDomain(entity);

        assertNotNull(result);
        assertNull(result.getCurrentClassroomId());
    }

    @Test
    void toEntity_ShouldMapDomainToEntity() {
        UUID studentUuid = UUID.randomUUID();
        UUID guardianUuid = UUID.randomUUID();
        UUID classroomUuid = UUID.randomUUID();

        StudentId studentId = StudentId.of(studentUuid);
        LegalGuardianId guardianId = LegalGuardianId.of(guardianUuid);
        ClassroomId classroomId = ClassroomId.of(classroomUuid);

        PersonName guardianName = PersonName.of("Jane", "Doe");
        LegalGuardian guardian = LegalGuardian.builder()
                .id(guardianId)
                .name(guardianName)
                .email("jane@example.com")
                .primaryPhone("123456789")
                .build();

        Student domain = Student.builder()
                .id(studentId)
                .legalGuardianId(guardianId)
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(2015, 5, 10))
                .identificationNumber("ID12345")
                .enrollmentDate(LocalDate.of(2023, 1, 15))
                .active(true)
                .currentClassroomId(classroomId)
                .legalGuardian(guardian)
                .build();

        when(legalGuardianEntityMock.getReferenceById(guardianUuid)).thenReturn(new LegalGuardianEntity());

        StudentEntity result = mapper.toEntity(domain);

        assertNotNull(result);
        assertEquals(studentUuid, result.getId());
        assertEquals(guardianUuid, result.getLegalGuardianId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(LocalDate.of(2015, 5, 10), result.getDateOfBirth());
        assertEquals("ID12345", result.getIdentificationNumber());
        assertEquals(LocalDate.of(2023, 1, 15), result.getEnrollmentDate());
        assertTrue(result.isActive());
        assertEquals(classroomUuid, result.getCurrentClassroomId());
        assertNotNull(result.getLegalGuardian());
    }

    @Test
    void toEntity_ShouldHandleNullClassroomId() {
        UUID studentUuid = UUID.randomUUID();
        UUID guardianUuid = UUID.randomUUID();

        StudentId studentId = StudentId.of(studentUuid);
        LegalGuardianId guardianId = LegalGuardianId.of(guardianUuid);

        PersonName guardianName = PersonName.of("Jane", "Doe");
        LegalGuardian guardian = LegalGuardian.builder()
                .id(guardianId)
                .name(guardianName)
                .email("jane@example.com")
                .primaryPhone("123456789")
                .build();

        Student domain = Student.builder()
                .id(studentId)
                .legalGuardianId(guardianId)
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(2015, 5, 10))
                .identificationNumber("ID12345")
                .enrollmentDate(LocalDate.of(2023, 1, 15))
                .active(true)
                .currentClassroomId(null)
                .legalGuardian(guardian)
                .build();

        when(legalGuardianEntityMock.getReferenceById(guardianUuid)).thenReturn(new LegalGuardianEntity());

        StudentEntity result = mapper.toEntity(domain);

        assertNotNull(result);
        assertNull(result.getCurrentClassroomId());
    }

    @Test
    void toEntity_ShouldHandleNullLegalGuardianInDomain() {
        UUID studentUuid = UUID.randomUUID();
        UUID guardianUuid = UUID.randomUUID();
        UUID classroomUuid = UUID.randomUUID();

        StudentId studentId = StudentId.of(studentUuid);
        LegalGuardianId guardianId = LegalGuardianId.of(guardianUuid);
        ClassroomId classroomId = ClassroomId.of(classroomUuid);

        Student domain = Student.builder()
                .id(studentId)
                .legalGuardianId(guardianId)
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(2015, 5, 10))
                .identificationNumber("ID12345")
                .enrollmentDate(LocalDate.of(2023, 1, 15))
                .active(true)
                .currentClassroomId(classroomId)
                .legalGuardian(null)
                .build();

        when(legalGuardianEntityMock.getReferenceById(guardianUuid)).thenReturn(new LegalGuardianEntity());

        StudentEntity result = mapper.toEntity(domain);

        assertNotNull(result);
        assertEquals(studentUuid, result.getId());
        assertEquals(guardianUuid, result.getLegalGuardianId());
        assertNotNull(result.getLegalGuardian());
    }
}