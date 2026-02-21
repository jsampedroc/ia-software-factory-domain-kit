package com.application;

import com.application.domain.SchoolManagement.classroom.domain.Classroom;
import com.application.domain.SchoolManagement.valueobject.ClassroomId;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClassroomRepositoryTest {

    @Mock
    private ClassroomRepository classroomRepository;

    private ClassroomId classroomId;
    private SchoolId schoolId;
    private Classroom classroom;

    @BeforeEach
    void setUp() {
        classroomId = new ClassroomId();
        schoolId = new SchoolId();
        classroom = mock(Classroom.class);
        when(classroom.getId()).thenReturn(classroomId);
    }

    @Test
    void save_ShouldPersistClassroom() {
        when(classroomRepository.save(classroom)).thenReturn(classroom);

        Classroom savedClassroom = classroomRepository.save(classroom);

        assertNotNull(savedClassroom);
        assertEquals(classroomId, savedClassroom.getId());
        verify(classroomRepository, times(1)).save(classroom);
    }

    @Test
    void findById_WhenClassroomExists_ShouldReturnClassroom() {
        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(classroom));

        Optional<Classroom> foundClassroom = classroomRepository.findById(classroomId);

        assertTrue(foundClassroom.isPresent());
        assertEquals(classroomId, foundClassroom.get().getId());
        verify(classroomRepository, times(1)).findById(classroomId);
    }

    @Test
    void findById_WhenClassroomDoesNotExist_ShouldReturnEmpty() {
        when(classroomRepository.findById(classroomId)).thenReturn(Optional.empty());

        Optional<Classroom> foundClassroom = classroomRepository.findById(classroomId);

        assertFalse(foundClassroom.isPresent());
        verify(classroomRepository, times(1)).findById(classroomId);
    }

    @Test
    void findAllBySchoolId_ShouldReturnListOfClassrooms() {
        List<Classroom> classroomList = List.of(classroom);
        when(classroomRepository.findAllBySchoolId(schoolId)).thenReturn(classroomList);

        List<Classroom> result = classroomRepository.findAllBySchoolId(schoolId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(classroomId, result.get(0).getId());
        verify(classroomRepository, times(1)).findAllBySchoolId(schoolId);
    }

    @Test
    void findAllBySchoolIdAndAcademicYear_ShouldReturnListOfClassrooms() {
        Year academicYear = Year.now();
        List<Classroom> classroomList = List.of(classroom);
        when(classroomRepository.findAllBySchoolIdAndAcademicYear(schoolId, academicYear)).thenReturn(classroomList);

        List<Classroom> result = classroomRepository.findAllBySchoolIdAndAcademicYear(schoolId, academicYear);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(classroomId, result.get(0).getId());
        verify(classroomRepository, times(1)).findAllBySchoolIdAndAcademicYear(schoolId, academicYear);
    }

    @Test
    void existsBySchoolIdAndGradeLevelAndSectionAndAcademicYear_WhenExists_ShouldReturnTrue() {
        Year academicYear = Year.now();
        when(classroomRepository.existsBySchoolIdAndGradeLevelAndSectionAndAcademicYear(schoolId, "1", "A", academicYear)).thenReturn(true);

        boolean exists = classroomRepository.existsBySchoolIdAndGradeLevelAndSectionAndAcademicYear(schoolId, "1", "A", academicYear);

        assertTrue(exists);
        verify(classroomRepository, times(1)).existsBySchoolIdAndGradeLevelAndSectionAndAcademicYear(schoolId, "1", "A", academicYear);
    }

    @Test
    void existsBySchoolIdAndGradeLevelAndSectionAndAcademicYear_WhenNotExists_ShouldReturnFalse() {
        Year academicYear = Year.now();
        when(classroomRepository.existsBySchoolIdAndGradeLevelAndSectionAndAcademicYear(schoolId, "1", "A", academicYear)).thenReturn(false);

        boolean exists = classroomRepository.existsBySchoolIdAndGradeLevelAndSectionAndAcademicYear(schoolId, "1", "A", academicYear);

        assertFalse(exists);
        verify(classroomRepository, times(1)).existsBySchoolIdAndGradeLevelAndSectionAndAcademicYear(schoolId, "1", "A", academicYear);
    }

    @Test
    void countActiveStudentsInClassroom_ShouldReturnCount() {
        int expectedCount = 5;
        when(classroomRepository.countActiveStudentsInClassroom(classroomId)).thenReturn(expectedCount);

        int count = classroomRepository.countActiveStudentsInClassroom(classroomId);

        assertEquals(expectedCount, count);
        verify(classroomRepository, times(1)).countActiveStudentsInClassroom(classroomId);
    }

    @Test
    void delete_ShouldRemoveClassroom() {
        doNothing().when(classroomRepository).delete(classroom);

        classroomRepository.delete(classroom);

        verify(classroomRepository, times(1)).delete(classroom);
    }
}