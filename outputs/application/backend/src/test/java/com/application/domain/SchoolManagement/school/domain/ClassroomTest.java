package com.application.domain.SchoolManagement.school.domain;

import com.application.domain.SchoolManagement.valueobject.ClassroomId;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import com.application.domain.shared.Entity;
import com.application.domain.exception.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ClassroomTest {

    private ClassroomId classroomId;
    private SchoolId schoolId;
    private final String validGradeLevel = "1";
    private final String validSection = "A";
    private final Year validAcademicYear = Year.now();
    private final int validCapacity = 25;
    private final String validTutorTeacherId = "teacher-123";

    @BeforeEach
    void setUp() {
        classroomId = new ClassroomId();
        schoolId = new SchoolId();
    }

    @Test
    void createClassroom_WithValidData_ShouldSucceed() {
        Classroom classroom = Classroom.create(
                classroomId,
                validGradeLevel,
                validSection,
                validAcademicYear,
                validCapacity,
                validTutorTeacherId,
                schoolId
        );

        assertNotNull(classroom);
        assertEquals(classroomId, classroom.getId());
        assertEquals(validGradeLevel, classroom.getGradeLevel());
        assertEquals(validSection, classroom.getSection());
        assertEquals(validAcademicYear, classroom.getAcademicYear());
        assertEquals(validCapacity, classroom.getCapacity());
        assertEquals(validTutorTeacherId, classroom.getTutorTeacherId());
        assertEquals(schoolId, classroom.getSchoolId());
        assertTrue(classroom.isActive());
    }

    @Test
    void createClassroom_WithNullId_ShouldThrowException() {
        assertThrows(DomainException.class, () ->
                Classroom.create(
                        null,
                        validGradeLevel,
                        validSection,
                        validAcademicYear,
                        validCapacity,
                        validTutorTeacherId,
                        schoolId
                )
        );
    }

    @Test
    void createClassroom_WithNullGradeLevel_ShouldThrowException() {
        assertThrows(DomainException.class, () ->
                Classroom.create(
                        classroomId,
                        null,
                        validSection,
                        validAcademicYear,
                        validCapacity,
                        validTutorTeacherId,
                        schoolId
                )
        );
    }

    @Test
    void createClassroom_WithEmptyGradeLevel_ShouldThrowException() {
        assertThrows(DomainException.class, () ->
                Classroom.create(
                        classroomId,
                        "",
                        validSection,
                        validAcademicYear,
                        validCapacity,
                        validTutorTeacherId,
                        schoolId
                )
        );
    }

    @Test
    void createClassroom_WithNullSection_ShouldThrowException() {
        assertThrows(DomainException.class, () ->
                Classroom.create(
                        classroomId,
                        validGradeLevel,
                        null,
                        validAcademicYear,
                        validCapacity,
                        validTutorTeacherId,
                        schoolId
                )
        );
    }

    @Test
    void createClassroom_WithEmptySection_ShouldThrowException() {
        assertThrows(DomainException.class, () ->
                Classroom.create(
                        classroomId,
                        validGradeLevel,
                        "",
                        validAcademicYear,
                        validCapacity,
                        validTutorTeacherId,
                        schoolId
                )
        );
    }

    @Test
    void createClassroom_WithNullAcademicYear_ShouldThrowException() {
        assertThrows(DomainException.class, () ->
                Classroom.create(
                        classroomId,
                        validGradeLevel,
                        validSection,
                        null,
                        validCapacity,
                        validTutorTeacherId,
                        schoolId
                )
        );
    }

    @Test
    void createClassroom_WithZeroCapacity_ShouldThrowException() {
        assertThrows(DomainException.class, () ->
                Classroom.create(
                        classroomId,
                        validGradeLevel,
                        validSection,
                        validAcademicYear,
                        0,
                        validTutorTeacherId,
                        schoolId
                )
        );
    }

    @Test
    void createClassroom_WithNegativeCapacity_ShouldThrowException() {
        assertThrows(DomainException.class, () ->
                Classroom.create(
                        classroomId,
                        validGradeLevel,
                        validSection,
                        validAcademicYear,
                        -5,
                        validTutorTeacherId,
                        schoolId
                )
        );
    }

    @Test
    void createClassroom_WithNullTutorTeacherId_ShouldThrowException() {
        assertThrows(DomainException.class, () ->
                Classroom.create(
                        classroomId,
                        validGradeLevel,
                        validSection,
                        validAcademicYear,
                        validCapacity,
                        null,
                        schoolId
                )
        );
    }

    @Test
    void createClassroom_WithEmptyTutorTeacherId_ShouldThrowException() {
        assertThrows(DomainException.class, () ->
                Classroom.create(
                        classroomId,
                        validGradeLevel,
                        validSection,
                        validAcademicYear,
                        validCapacity,
                        "",
                        schoolId
                )
        );
    }

    @Test
    void createClassroom_WithNullSchoolId_ShouldThrowException() {
        assertThrows(DomainException.class, () ->
                Classroom.create(
                        classroomId,
                        validGradeLevel,
                        validSection,
                        validAcademicYear,
                        validCapacity,
                        validTutorTeacherId,
                        null
                )
        );
    }

    @Test
    void deactivate_WhenClassroomIsActive_ShouldSetActiveToFalse() {
        Classroom classroom = Classroom.create(
                classroomId,
                validGradeLevel,
                validSection,
                validAcademicYear,
                validCapacity,
                validTutorTeacherId,
                schoolId
        );

        classroom.deactivate();

        assertFalse(classroom.isActive());
    }

    @Test
    void deactivate_WhenClassroomIsAlreadyInactive_ShouldRemainInactive() {
        Classroom classroom = Classroom.create(
                classroomId,
                validGradeLevel,
                validSection,
                validAcademicYear,
                validCapacity,
                validTutorTeacherId,
                schoolId
        );
        classroom.deactivate();

        classroom.deactivate();

        assertFalse(classroom.isActive());
    }

    @Test
    void activate_WhenClassroomIsInactive_ShouldSetActiveToTrue() {
        Classroom classroom = Classroom.create(
                classroomId,
                validGradeLevel,
                validSection,
                validAcademicYear,
                validCapacity,
                validTutorTeacherId,
                schoolId
        );
        classroom.deactivate();

        classroom.activate();

        assertTrue(classroom.isActive());
    }

    @Test
    void activate_WhenClassroomIsAlreadyActive_ShouldRemainActive() {
        Classroom classroom = Classroom.create(
                classroomId,
                validGradeLevel,
                validSection,
                validAcademicYear,
                validCapacity,
                validTutorTeacherId,
                schoolId
        );

        classroom.activate();

        assertTrue(classroom.isActive());
    }

    @Test
    void updateCapacity_WithValidPositiveCapacity_ShouldUpdate() {
        Classroom classroom = Classroom.create(
                classroomId,
                validGradeLevel,
                validSection,
                validAcademicYear,
                validCapacity,
                validTutorTeacherId,
                schoolId
        );
        int newCapacity = 30;

        classroom.updateCapacity(newCapacity);

        assertEquals(newCapacity, classroom.getCapacity());
    }

    @Test
    void updateCapacity_WithZeroCapacity_ShouldThrowException() {
        Classroom classroom = Classroom.create(
                classroomId,
                validGradeLevel,
                validSection,
                validAcademicYear,
                validCapacity,
                validTutorTeacherId,
                schoolId
        );

        assertThrows(DomainException.class, () -> classroom.updateCapacity(0));
    }

    @Test
    void updateCapacity_WithNegativeCapacity_ShouldThrowException() {
        Classroom classroom = Classroom.create(
                classroomId,
                validGradeLevel,
                validSection,
                validAcademicYear,
                validCapacity,
                validTutorTeacherId,
                schoolId
        );

        assertThrows(DomainException.class, () -> classroom.updateCapacity(-10));
    }

    @Test
    void updateTutorTeacher_WithValidId_ShouldUpdate() {
        Classroom classroom = Classroom.create(
                classroomId,
                validGradeLevel,
                validSection,
                validAcademicYear,
                validCapacity,
                validTutorTeacherId,
                schoolId
        );
        String newTeacherId = "teacher-456";

        classroom.updateTutorTeacher(newTeacherId);

        assertEquals(newTeacherId, classroom.getTutorTeacherId());
    }

    @Test
    void updateTutorTeacher_WithNullId_ShouldThrowException() {
        Classroom classroom = Classroom.create(
                classroomId,
                validGradeLevel,
                validSection,
                validAcademicYear,
                validCapacity,
                validTutorTeacherId,
                schoolId
        );

        assertThrows(DomainException.class, () -> classroom.updateTutorTeacher(null));
    }

    @Test
    void updateTutorTeacher_WithEmptyId_ShouldThrowException() {
        Classroom classroom = Classroom.create(
                classroomId,
                validGradeLevel,
                validSection,
                validAcademicYear,
                validCapacity,
                validTutorTeacherId,
                schoolId
        );

        assertThrows(DomainException.class, () -> classroom.updateTutorTeacher(""));
    }

    @Test
    void canAcceptNewStudent_WhenActiveAndBelowCapacity_ShouldReturnTrue() {
        Classroom classroom = Classroom.create(
                classroomId,
                validGradeLevel,
                validSection,
                validAcademicYear,
                validCapacity,
                validTutorTeacherId,
                schoolId
        );

        assertTrue(classroom.canAcceptNewStudent());
    }

    @Test
    void canAcceptNewStudent_WhenInactive_ShouldReturnFalse() {
        Classroom classroom = Classroom.create(
                classroomId,
                validGradeLevel,
                validSection,
                validAcademicYear,
                validCapacity,
                validTutorTeacherId,
                schoolId
        );
        classroom.deactivate();

        assertFalse(classroom.canAcceptNewStudent());
    }

    @Test
    void canAcceptNewStudent_WhenAtCapacity_ShouldReturnFalse() {
        Classroom classroom = Classroom.create(
                classroomId,
                validGradeLevel,
                validSection,
                validAcademicYear,
                validCapacity,
                validTutorTeacherId,
                schoolId
        );
        classroom.updateCurrentOccupancy(validCapacity);

        assertFalse(classroom.canAcceptNewStudent());
    }

    @Test
    void updateCurrentOccupancy_WithValidNumber_ShouldUpdate() {
        Classroom classroom = Classroom.create(
                classroomId,
                validGradeLevel,
                validSection,
                validAcademicYear,
                validCapacity,
                validTutorTeacherId,
                schoolId
        );
        int occupancy = 10;

        classroom.updateCurrentOccupancy(occupancy);

        assertEquals(occupancy, classroom.getCurrentOccupancy());
    }

    @Test
    void updateCurrentOccupancy_WithNegativeNumber_ShouldThrowException() {
        Classroom classroom = Classroom.create(
                classroomId,
                validGradeLevel,
                validSection,
                validAcademicYear,
                validCapacity,
                validTutorTeacherId,
                schoolId
        );

        assertThrows(DomainException.class, () -> classroom.updateCurrentOccupancy(-1));
    }

    @Test
    void updateCurrentOccupancy_WithNumberExceedingCapacity_ShouldThrowException() {
        Classroom classroom = Classroom.create(
                classroomId,
                validGradeLevel,
                validSection,
                validAcademicYear,
                validCapacity,
                validTutorTeacherId,
                schoolId
        );

        assertThrows(DomainException.class, () -> classroom.updateCurrentOccupancy(validCapacity + 1));
    }

    @Test
    void equals_WithSameId_ShouldReturnTrue() {
        Classroom classroom1 = Classroom.create(
                classroomId,
                validGradeLevel,
                validSection,
                validAcademicYear,
                validCapacity,
                validTutorTeacherId,
                schoolId
        );
        Classroom classroom2 = Classroom.create(
                classroomId,
                "2",
                "B",
                Year.now().plusYears(1),
                20,
                "teacher-999",
                new SchoolId()
        );

        assertEquals(classroom1, classroom2);
        assertTrue(classroom1.equals(classroom2));
    }

    @Test
    void equals_WithDifferentId_ShouldReturnFalse() {
        Classroom classroom1 = Classroom.create(
                classroomId,
                validGradeLevel,
                validSection,
                validAcademicYear,
                validCapacity,
                validTutorTeacherId,
                schoolId
        );
        Classroom classroom2 = Classroom.create(
                new ClassroomId(),
                validGradeLevel,
                validSection,
                validAcademicYear,
                validCapacity,
                validTutorTeacherId,
                schoolId
        );

        assertNotEquals(classroom1, classroom2);
        assertFalse(classroom1.equals(classroom2));
    }

    @Test
    void hashCode_WithSameId_ShouldBeEqual() {
        Classroom classroom1 = Classroom.create(
                classroomId,
                validGradeLevel,
                validSection,
                validAcademicYear,
                validCapacity,
                validTutorTeacherId,
                schoolId
        );
        Classroom classroom2 = Classroom.create(
                classroomId,
                "2",
                "B",
                Year.now().plusYears(1),
                20,
                "teacher-999",
                new SchoolId()
        );

        assertEquals(classroom1.hashCode(), classroom2.hashCode());
    }

    @Test
    void hashCode_WithDifferentId_ShouldNotBeEqual() {
        Classroom classroom1 = Classroom.create(
                classroomId,
                validGradeLevel,
                validSection,
                validAcademicYear,
                validCapacity,
                validTutorTeacherId,
                schoolId
        );
        Classroom classroom2 = Classroom.create(
                new ClassroomId(),
                validGradeLevel,
                validSection,
                validAcademicYear,
                validCapacity,
                validTutorTeacherId,
                schoolId
        );

        assertNotEquals(classroom1.hashCode(), classroom2.hashCode());
    }
}