package com.application;

import com.application.domain.SchoolManagement.student.domain.Student;
import com.application.domain.SchoolManagement.valueobject.StudentId;
import com.application.domain.SchoolManagement.valueobject.LegalGuardianId;
import com.application.domain.SchoolManagement.valueobject.ClassroomId;
import com.application.domain.shared.valueobject.PersonName;
import com.application.domain.exception.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StudentTest {

    @Test
    void createStudent_WithValidData_ShouldSucceed() {
        StudentId id = new StudentId(UUID.randomUUID());
        LegalGuardianId guardianId = new LegalGuardianId(UUID.randomUUID());
        PersonName name = new PersonName("John", "Doe");
        LocalDate dateOfBirth = LocalDate.of(2015, 5, 10);
        String identificationNumber = "ID123456";
        LocalDate enrollmentDate = LocalDate.of(2024, 1, 15);
        ClassroomId classroomId = new ClassroomId(UUID.randomUUID());

        Student student = Student.create(id, guardianId, name, dateOfBirth, identificationNumber, enrollmentDate, classroomId);

        assertNotNull(student);
        assertEquals(id, student.getId());
        assertEquals(guardianId, student.getLegalGuardianId());
        assertEquals(name, student.getName());
        assertEquals(dateOfBirth, student.getDateOfBirth());
        assertEquals(identificationNumber, student.getIdentificationNumber());
        assertEquals(enrollmentDate, student.getEnrollmentDate());
        assertEquals(classroomId, student.getCurrentClassroomId());
        assertTrue(student.isActive());
    }

    @Test
    void createStudent_WithBirthDateAfterEnrollment_ShouldThrowDomainException() {
        StudentId id = new StudentId(UUID.randomUUID());
        LegalGuardianId guardianId = new LegalGuardianId(UUID.randomUUID());
        PersonName name = new PersonName("Jane", "Doe");
        LocalDate dateOfBirth = LocalDate.of(2024, 6, 1);
        String identificationNumber = "ID654321";
        LocalDate enrollmentDate = LocalDate.of(2024, 1, 1);
        ClassroomId classroomId = new ClassroomId(UUID.randomUUID());

        DomainException exception = assertThrows(DomainException.class, () ->
                Student.create(id, guardianId, name, dateOfBirth, identificationNumber, enrollmentDate, classroomId)
        );
        assertTrue(exception.getMessage().contains("fecha de nacimiento"));
    }

    @Test
    void createStudent_WithNullIdentificationNumber_ShouldThrowDomainException() {
        StudentId id = new StudentId(UUID.randomUUID());
        LegalGuardianId guardianId = new LegalGuardianId(UUID.randomUUID());
        PersonName name = new PersonName("Alice", "Smith");
        LocalDate dateOfBirth = LocalDate.of(2014, 3, 20);
        LocalDate enrollmentDate = LocalDate.of(2024, 2, 1);
        ClassroomId classroomId = new ClassroomId(UUID.randomUUID());

        DomainException exception = assertThrows(DomainException.class, () ->
                Student.create(id, guardianId, name, dateOfBirth, null, enrollmentDate, classroomId)
        );
        assertTrue(exception.getMessage().contains("identificación"));
    }

    @Test
    void createStudent_WithEmptyIdentificationNumber_ShouldThrowDomainException() {
        StudentId id = new StudentId(UUID.randomUUID());
        LegalGuardianId guardianId = new LegalGuardianId(UUID.randomUUID());
        PersonName name = new PersonName("Bob", "Brown");
        LocalDate dateOfBirth = LocalDate.of(2013, 7, 11);
        LocalDate enrollmentDate = LocalDate.of(2024, 1, 10);
        ClassroomId classroomId = new ClassroomId(UUID.randomUUID());

        DomainException exception = assertThrows(DomainException.class, () ->
                Student.create(id, guardianId, name, dateOfBirth, "", enrollmentDate, classroomId)
        );
        assertTrue(exception.getMessage().contains("identificación"));
    }

    @Test
    void deactivate_ActiveStudent_ShouldSetActiveToFalse() {
        StudentId id = new StudentId(UUID.randomUUID());
        LegalGuardianId guardianId = new LegalGuardianId(UUID.randomUUID());
        PersonName name = new PersonName("Charlie", "Davis");
        LocalDate dateOfBirth = LocalDate.of(2016, 9, 5);
        String identificationNumber = "ID789012";
        LocalDate enrollmentDate = LocalDate.of(2024, 3, 1);
        ClassroomId classroomId = new ClassroomId(UUID.randomUUID());

        Student student = Student.create(id, guardianId, name, dateOfBirth, identificationNumber, enrollmentDate, classroomId);
        assertTrue(student.isActive());

        student.deactivate();

        assertFalse(student.isActive());
    }

    @Test
    void changeClassroom_WithNewClassroomId_ShouldUpdateCurrentClassroomId() {
        StudentId id = new StudentId(UUID.randomUUID());
        LegalGuardianId guardianId = new LegalGuardianId(UUID.randomUUID());
        PersonName name = new PersonName("Diana", "Evans");
        LocalDate dateOfBirth = LocalDate.of(2015, 11, 30);
        String identificationNumber = "ID345678";
        LocalDate enrollmentDate = LocalDate.of(2024, 1, 20);
        ClassroomId initialClassroomId = new ClassroomId(UUID.randomUUID());
        ClassroomId newClassroomId = new ClassroomId(UUID.randomUUID());

        Student student = Student.create(id, guardianId, name, dateOfBirth, identificationNumber, enrollmentDate, initialClassroomId);
        assertEquals(initialClassroomId, student.getCurrentClassroomId());

        student.changeClassroom(newClassroomId);

        assertEquals(newClassroomId, student.getCurrentClassroomId());
    }

    @Test
    void updatePersonalInformation_WithNewNameAndBirthDate_ShouldUpdateFields() {
        StudentId id = new StudentId(UUID.randomUUID());
        LegalGuardianId guardianId = new LegalGuardianId(UUID.randomUUID());
        PersonName originalName = new PersonName("Original", "Name");
        LocalDate originalDateOfBirth = LocalDate.of(2014, 1, 1);
        String identificationNumber = "ID999888";
        LocalDate enrollmentDate = LocalDate.of(2024, 1, 15);
        ClassroomId classroomId = new ClassroomId(UUID.randomUUID());

        Student student = Student.create(id, guardianId, originalName, originalDateOfBirth, identificationNumber, enrollmentDate, classroomId);

        PersonName newName = new PersonName("Updated", "Name");
        LocalDate newDateOfBirth = LocalDate.of(2014, 12, 31);

        student.updatePersonalInformation(newName, newDateOfBirth);

        assertEquals(newName, student.getName());
        assertEquals(newDateOfBirth, student.getDateOfBirth());
    }

    @Test
    void updatePersonalInformation_WithBirthDateAfterEnrollment_ShouldThrowDomainException() {
        StudentId id = new StudentId(UUID.randomUUID());
        LegalGuardianId guardianId = new LegalGuardianId(UUID.randomUUID());
        PersonName originalName = new PersonName("Test", "Student");
        LocalDate originalDateOfBirth = LocalDate.of(2014, 1, 1);
        String identificationNumber = "ID111222";
        LocalDate enrollmentDate = LocalDate.of(2024, 1, 15);
        ClassroomId classroomId = new ClassroomId(UUID.randomUUID());

        Student student = Student.create(id, guardianId, originalName, originalDateOfBirth, identificationNumber, enrollmentDate, classroomId);

        PersonName newName = new PersonName("Test", "Student");
        LocalDate invalidDateOfBirth = LocalDate.of(2025, 1, 1);

        DomainException exception = assertThrows(DomainException.class, () ->
                student.updatePersonalInformation(newName, invalidDateOfBirth)
        );
        assertTrue(exception.getMessage().contains("fecha de nacimiento"));
    }
}