package com.application.domain.SchoolManagement.student.domain.repository;

import com.application.domain.SchoolManagement.student.domain.Student;
import com.application.domain.SchoolManagement.student.domain.valueobject.StudentId;
import com.application.domain.SchoolManagement.school.domain.valueobject.SchoolId;
import com.application.domain.SchoolManagement.classroom.domain.valueobject.ClassroomId;
import com.application.domain.shared.Entity;
import com.application.domain.exception.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentRepositoryTest {

    @Mock
    private StudentRepository studentRepository;

    private StudentId testStudentId;
    private SchoolId testSchoolId;
    private ClassroomId testClassroomId;
    private Student testStudent;

    @BeforeEach
    void setUp() {
        testStudentId = new StudentId(UUID.randomUUID());
        testSchoolId = new SchoolId(UUID.randomUUID());
        testClassroomId = new ClassroomId(UUID.randomUUID());

        testStudent = Student.builder()
                .id(testStudentId)
                .legalGuardianId(new com.application.domain.SchoolManagement.student.domain.valueobject.LegalGuardianId(UUID.randomUUID()))
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(2015, 5, 10))
                .identificationNumber("ID-123456")
                .enrollmentDate(LocalDate.now())
                .active(true)
                .currentClassroomId(testClassroomId)
                .build();
    }

    @Test
    void save_ValidStudent_ShouldCallRepositorySave() {
        studentRepository.save(testStudent);
        verify(studentRepository, times(1)).save(testStudent);
    }

    @Test
    void findById_ExistingId_ShouldReturnStudent() {
        when(studentRepository.findById(testStudentId)).thenReturn(Optional.of(testStudent));
        Optional<Student> result = studentRepository.findById(testStudentId);
        assertTrue(result.isPresent());
        assertEquals(testStudentId, result.get().getId());
        verify(studentRepository, times(1)).findById(testStudentId);
    }

    @Test
    void findById_NonExistingId_ShouldReturnEmpty() {
        StudentId nonExistingId = new StudentId(UUID.randomUUID());
        when(studentRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        Optional<Student> result = studentRepository.findById(nonExistingId);
        assertFalse(result.isPresent());
        verify(studentRepository, times(1)).findById(nonExistingId);
    }

    @Test
    void findById_NullId_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> studentRepository.findById(null));
    }

    @Test
    void findBySchoolId_ExistingSchool_ShouldReturnStudentList() {
        List<Student> studentList = List.of(testStudent);
        when(studentRepository.findBySchoolId(testSchoolId)).thenReturn(studentList);
        List<Student> result = studentRepository.findBySchoolId(testSchoolId);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testStudentId, result.get(0).getId());
        verify(studentRepository, times(1)).findBySchoolId(testSchoolId);
    }

    @Test
    void findBySchoolId_NonExistingSchool_ShouldReturnEmptyList() {
        SchoolId nonExistingSchoolId = new SchoolId(UUID.randomUUID());
        when(studentRepository.findBySchoolId(nonExistingSchoolId)).thenReturn(List.of());
        List<Student> result = studentRepository.findBySchoolId(nonExistingSchoolId);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(studentRepository, times(1)).findBySchoolId(nonExistingSchoolId);
    }

    @Test
    void findBySchoolId_NullId_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> studentRepository.findBySchoolId(null));
    }

    @Test
    void findByClassroomId_ExistingClassroom_ShouldReturnStudentList() {
        List<Student> studentList = List.of(testStudent);
        when(studentRepository.findByClassroomId(testClassroomId)).thenReturn(studentList);
        List<Student> result = studentRepository.findByClassroomId(testClassroomId);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testClassroomId, result.get(0).getCurrentClassroomId());
        verify(studentRepository, times(1)).findByClassroomId(testClassroomId);
    }

    @Test
    void findByClassroomId_NonExistingClassroom_ShouldReturnEmptyList() {
        ClassroomId nonExistingClassroomId = new ClassroomId(UUID.randomUUID());
        when(studentRepository.findByClassroomId(nonExistingClassroomId)).thenReturn(List.of());
        List<Student> result = studentRepository.findByClassroomId(nonExistingClassroomId);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(studentRepository, times(1)).findByClassroomId(nonExistingClassroomId);
    }

    @Test
    void findByClassroomId_NullId_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> studentRepository.findByClassroomId(null));
    }

    @Test
    void findByIdentificationNumber_ExistingNumber_ShouldReturnStudent() {
        String testIdentificationNumber = "ID-123456";
        when(studentRepository.findByIdentificationNumber(testIdentificationNumber)).thenReturn(Optional.of(testStudent));
        Optional<Student> result = studentRepository.findByIdentificationNumber(testIdentificationNumber);
        assertTrue(result.isPresent());
        assertEquals(testIdentificationNumber, result.get().getIdentificationNumber());
        verify(studentRepository, times(1)).findByIdentificationNumber(testIdentificationNumber);
    }

    @Test
    void findByIdentificationNumber_NonExistingNumber_ShouldReturnEmpty() {
        String nonExistingNumber = "NON-EXIST";
        when(studentRepository.findByIdentificationNumber(nonExistingNumber)).thenReturn(Optional.empty());
        Optional<Student> result = studentRepository.findByIdentificationNumber(nonExistingNumber);
        assertFalse(result.isPresent());
        verify(studentRepository, times(1)).findByIdentificationNumber(nonExistingNumber);
    }

    @Test
    void findByIdentificationNumber_NullNumber_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> studentRepository.findByIdentificationNumber(null));
    }

    @Test
    void existsByIdentificationNumber_ExistingNumber_ShouldReturnTrue() {
        String testIdentificationNumber = "ID-123456";
        when(studentRepository.existsByIdentificationNumber(testIdentificationNumber)).thenReturn(true);
        boolean result = studentRepository.existsByIdentificationNumber(testIdentificationNumber);
        assertTrue(result);
        verify(studentRepository, times(1)).existsByIdentificationNumber(testIdentificationNumber);
    }

    @Test
    void existsByIdentificationNumber_NonExistingNumber_ShouldReturnFalse() {
        String nonExistingNumber = "NON-EXIST";
        when(studentRepository.existsByIdentificationNumber(nonExistingNumber)).thenReturn(false);
        boolean result = studentRepository.existsByIdentificationNumber(nonExistingNumber);
        assertFalse(result);
        verify(studentRepository, times(1)).existsByIdentificationNumber(nonExistingNumber);
    }

    @Test
    void existsByIdentificationNumber_NullNumber_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> studentRepository.existsByIdentificationNumber(null));
    }

    @Test
    void delete_ExistingStudent_ShouldCallRepositoryDelete() {
        studentRepository.delete(testStudent);
        verify(studentRepository, times(1)).delete(testStudent);
    }

    @Test
    void delete_NullStudent_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> studentRepository.delete(null));
    }

    @Test
    void countByClassroomId_ExistingClassroom_ShouldReturnCount() {
        long expectedCount = 5L;
        when(studentRepository.countByClassroomId(testClassroomId)).thenReturn(expectedCount);
        long result = studentRepository.countByClassroomId(testClassroomId);
        assertEquals(expectedCount, result);
        verify(studentRepository, times(1)).countByClassroomId(testClassroomId);
    }

    @Test
    void countByClassroomId_NonExistingClassroom_ShouldReturnZero() {
        ClassroomId nonExistingClassroomId = new ClassroomId(UUID.randomUUID());
        when(studentRepository.countByClassroomId(nonExistingClassroomId)).thenReturn(0L);
        long result = studentRepository.countByClassroomId(nonExistingClassroomId);
        assertEquals(0L, result);
        verify(studentRepository, times(1)).countByClassroomId(nonExistingClassroomId);
    }

    @Test
    void countByClassroomId_NullId_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> studentRepository.countByClassroomId(null));
    }
}