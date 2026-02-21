package com.application;

import com.application.domain.SchoolManagement.student.domain.Student;
import com.application.domain.SchoolManagement.student.domain.repository.StudentRepository;
import com.application.infrastructure.persistence.SchoolManagement.student.adapter.StudentJpaRepository;
import com.application.infrastructure.persistence.SchoolManagement.student.entity.StudentEntity;
import com.application.infrastructure.persistence.SchoolManagement.student.mapper.StudentEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentRepositoryImplTest {

    @Mock
    private StudentJpaRepository studentJpaRepository;

    @Mock
    private StudentEntityMapper studentEntityMapper;

    @InjectMocks
    private StudentRepositoryImpl studentRepository;

    private Student student;
    private StudentEntity studentEntity;
    private UUID studentId;

    @BeforeEach
    void setUp() {
        studentId = UUID.randomUUID();
        student = mock(Student.class);
        studentEntity = mock(StudentEntity.class);
    }

    @Test
    void save_ShouldCallJpaRepositoryAndMapper() {
        when(studentEntityMapper.toEntity(student)).thenReturn(studentEntity);
        when(studentJpaRepository.save(studentEntity)).thenReturn(studentEntity);
        when(studentEntityMapper.toDomain(studentEntity)).thenReturn(student);

        Student result = studentRepository.save(student);

        assertNotNull(result);
        verify(studentEntityMapper).toEntity(student);
        verify(studentJpaRepository).save(studentEntity);
        verify(studentEntityMapper).toDomain(studentEntity);
    }

    @Test
    void findById_WhenEntityExists_ShouldReturnStudent() {
        when(studentJpaRepository.findById(studentId)).thenReturn(Optional.of(studentEntity));
        when(studentEntityMapper.toDomain(studentEntity)).thenReturn(student);

        Optional<Student> result = studentRepository.findById(studentId);

        assertTrue(result.isPresent());
        assertEquals(student, result.get());
        verify(studentJpaRepository).findById(studentId);
        verify(studentEntityMapper).toDomain(studentEntity);
    }

    @Test
    void findById_WhenEntityDoesNotExist_ShouldReturnEmpty() {
        when(studentJpaRepository.findById(studentId)).thenReturn(Optional.empty());

        Optional<Student> result = studentRepository.findById(studentId);

        assertFalse(result.isPresent());
        verify(studentJpaRepository).findById(studentId);
        verify(studentEntityMapper, never()).toDomain(any());
    }

    @Test
    void existsById_ShouldDelegateToJpaRepository() {
        when(studentJpaRepository.existsById(studentId)).thenReturn(true);

        boolean result = studentRepository.existsById(studentId);

        assertTrue(result);
        verify(studentJpaRepository).existsById(studentId);
    }

    @Test
    void deleteById_ShouldCallJpaRepository() {
        doNothing().when(studentJpaRepository).deleteById(studentId);

        studentRepository.deleteById(studentId);

        verify(studentJpaRepository).deleteById(studentId);
    }
}