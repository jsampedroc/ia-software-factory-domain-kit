package com.application.infrastructure.persistence.SchoolManagement.school.entity;

import com.application.domain.SchoolManagement.school.domain.Classroom;
import com.application.domain.SchoolManagement.valueobject.ClassroomId;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassroomEntityTest {

    private ClassroomEntity classroomEntity;
    private ClassroomId classroomId;
    private SchoolId schoolId;
    private UUID uuid;
    private UUID schoolUuid;

    @BeforeEach
    void setUp() {
        uuid = UUID.randomUUID();
        schoolUuid = UUID.randomUUID();
        classroomId = new ClassroomId(uuid);
        schoolId = new SchoolId(schoolUuid);
        classroomEntity = new ClassroomEntity();
    }

    @Test
    void testToDomain_ShouldReturnClassroomWithCorrectAttributes() {
        classroomEntity.setId(uuid);
        classroomEntity.setGradeLevel("5");
        classroomEntity.setSection("A");
        classroomEntity.setAcademicYear("2024-2025");
        classroomEntity.setCapacity(30);
        classroomEntity.setTutorTeacherId(UUID.randomUUID());
        classroomEntity.setSchoolId(schoolUuid);
        classroomEntity.setActive(true);

        Classroom domainClassroom = classroomEntity.toDomain();

        assertNotNull(domainClassroom);
        assertEquals(classroomId, domainClassroom.getId());
        assertEquals("5", domainClassroom.getGradeLevel());
        assertEquals("A", domainClassroom.getSection());
        assertEquals("2024-2025", domainClassroom.getAcademicYear());
        assertEquals(30, domainClassroom.getCapacity());
        assertEquals(schoolId, domainClassroom.getSchoolId());
        assertTrue(domainClassroom.isActive());
    }

    @Test
    void testToDomain_WhenIdIsNull_ShouldThrowException() {
        classroomEntity.setId(null);
        classroomEntity.setSchoolId(schoolUuid);

        assertThrows(NullPointerException.class, () -> classroomEntity.toDomain());
    }

    @Test
    void testToDomain_WhenSchoolIdIsNull_ShouldThrowException() {
        classroomEntity.setId(uuid);
        classroomEntity.setSchoolId(null);

        assertThrows(NullPointerException.class, () -> classroomEntity.toDomain());
    }

    @Test
    void testFromDomain_ShouldPopulateEntityCorrectly() {
        Classroom mockClassroom = mock(Classroom.class);
        when(mockClassroom.getId()).thenReturn(classroomId);
        when(mockClassroom.getGradeLevel()).thenReturn("3");
        when(mockClassroom.getSection()).thenReturn("B");
        when(mockClassroom.getAcademicYear()).thenReturn("2023-2024");
        when(mockClassroom.getCapacity()).thenReturn(25);
        when(mockClassroom.getTutorTeacherId()).thenReturn(UUID.randomUUID());
        when(mockClassroom.getSchoolId()).thenReturn(schoolId);
        when(mockClassroom.isActive()).thenReturn(false);

        ClassroomEntity resultEntity = ClassroomEntity.fromDomain(mockClassroom);

        assertNotNull(resultEntity);
        assertEquals(uuid, resultEntity.getId());
        assertEquals("3", resultEntity.getGradeLevel());
        assertEquals("B", resultEntity.getSection());
        assertEquals("2023-2024", resultEntity.getAcademicYear());
        assertEquals(25, resultEntity.getCapacity());
        assertEquals(schoolUuid, resultEntity.getSchoolId());
        assertFalse(resultEntity.isActive());
    }

    @Test
    void testFromDomain_WhenClassroomIsNull_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> ClassroomEntity.fromDomain(null));
    }

    @Test
    void testGettersAndSetters() {
        UUID testId = UUID.randomUUID();
        String testGradeLevel = "10";
        String testSection = "C";
        String testAcademicYear = "2025-2026";
        Integer testCapacity = 40;
        UUID testTutorId = UUID.randomUUID();
        UUID testSchoolId = UUID.randomUUID();
        Boolean testActive = true;

        classroomEntity.setId(testId);
        classroomEntity.setGradeLevel(testGradeLevel);
        classroomEntity.setSection(testSection);
        classroomEntity.setAcademicYear(testAcademicYear);
        classroomEntity.setCapacity(testCapacity);
        classroomEntity.setTutorTeacherId(testTutorId);
        classroomEntity.setSchoolId(testSchoolId);
        classroomEntity.setActive(testActive);

        assertEquals(testId, classroomEntity.getId());
        assertEquals(testGradeLevel, classroomEntity.getGradeLevel());
        assertEquals(testSection, classroomEntity.getSection());
        assertEquals(testAcademicYear, classroomEntity.getAcademicYear());
        assertEquals(testCapacity, classroomEntity.getCapacity());
        assertEquals(testTutorId, classroomEntity.getTutorTeacherId());
        assertEquals(testSchoolId, classroomEntity.getSchoolId());
        assertEquals(testActive, classroomEntity.isActive());
    }

    @Test
    void testEqualsAndHashCode() {
        ClassroomEntity entity1 = new ClassroomEntity();
        entity1.setId(uuid);
        ClassroomEntity entity2 = new ClassroomEntity();
        entity2.setId(uuid);
        ClassroomEntity entity3 = new ClassroomEntity();
        entity3.setId(UUID.randomUUID());

        assertEquals(entity1, entity2);
        assertNotEquals(entity1, entity3);
        assertEquals(entity1.hashCode(), entity2.hashCode());
        assertNotEquals(entity1.hashCode(), entity3.hashCode());
    }

    @Test
    void testToString() {
        classroomEntity.setId(uuid);
        classroomEntity.setGradeLevel("1");
        String toString = classroomEntity.toString();
        assertTrue(toString.contains(uuid.toString()));
        assertTrue(toString.contains("1"));
    }
}