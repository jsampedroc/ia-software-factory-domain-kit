package com.application.infrastructure.persistence.SchoolManagement.student.adapter;

import com.application.domain.SchoolManagement.student.domain.Student;
import com.application.domain.SchoolManagement.student.domain.StudentId;
import com.application.infrastructure.persistence.SchoolManagement.student.entity.StudentEntity;
import com.application.infrastructure.persistence.SchoolManagement.student.mapper.StudentEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentJpaRepositoryTest {

    @Mock
    private StudentEntityMapper studentEntityMapper;

    @Mock
    private StudentJpaRepository studentJpaRepository;

    private StudentEntity studentEntity;
    private Student studentDomain;
    private StudentId studentId;

    @BeforeEach
    void setUp() {
        UUID uuid = UUID.randomUUID();
        studentId = new StudentId(uuid);
        studentDomain = mock(Student.class);
        studentEntity = new StudentEntity();
        studentEntity.setId(uuid);
        studentEntity.setFirstName("John");
        studentEntity.setLastName("Doe");
        studentEntity.setIdentificationNumber("ID123");
        studentEntity.setEnrollmentDate(LocalDate.now());
        studentEntity.setActive(true);
    }

    @Test
    void findById_WhenEntityExists_ReturnsOptionalOfEntity() {
        when(studentJpaRepository.findById(studentId.getValue())).thenReturn(Optional.of(studentEntity));

        Optional<StudentEntity> result = studentJpaRepository.findById(studentId.getValue());

        assertThat(result).isPresent().contains(studentEntity);
        verify(studentJpaRepository, times(1)).findById(studentId.getValue());
    }

    @Test
    void findById_WhenEntityDoesNotExist_ReturnsEmptyOptional() {
        when(studentJpaRepository.findById(studentId.getValue())).thenReturn(Optional.empty());

        Optional<StudentEntity> result = studentJpaRepository.findById(studentId.getValue());

        assertThat(result).isEmpty();
        verify(studentJpaRepository, times(1)).findById(studentId.getValue());
    }

    @Test
    void findAll_ReturnsPageOfEntities() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<StudentEntity> entityPage = new PageImpl<>(List.of(studentEntity));
        when(studentJpaRepository.findAll(pageable)).thenReturn(entityPage);

        Page<StudentEntity> result = studentJpaRepository.findAll(pageable);

        assertThat(result).isEqualTo(entityPage);
        verify(studentJpaRepository, times(1)).findAll(pageable);
    }

    @Test
    void findAll_WithSpecification_ReturnsPageOfEntities() {
        Pageable pageable = PageRequest.of(0, 10);
        Specification<StudentEntity> spec = mock(Specification.class);
        Page<StudentEntity> entityPage = new PageImpl<>(List.of(studentEntity));
        when(studentJpaRepository.findAll(eq(spec), eq(pageable))).thenReturn(entityPage);

        Page<StudentEntity> result = studentJpaRepository.findAll(spec, pageable);

        assertThat(result).isEqualTo(entityPage);
        verify(studentJpaRepository, times(1)).findAll(spec, pageable);
    }

    @Test
    void save_Entity_SavesAndReturnsEntity() {
        when(studentJpaRepository.save(studentEntity)).thenReturn(studentEntity);

        StudentEntity result = studentJpaRepository.save(studentEntity);

        assertThat(result).isEqualTo(studentEntity);
        verify(studentJpaRepository, times(1)).save(studentEntity);
    }

    @Test
    void deleteById_DeletesEntity() {
        doNothing().when(studentJpaRepository).deleteById(studentId.getValue());

        studentJpaRepository.deleteById(studentId.getValue());

        verify(studentJpaRepository, times(1)).deleteById(studentId.getValue());
    }

    @Test
    void existsById_WhenEntityExists_ReturnsTrue() {
        when(studentJpaRepository.existsById(studentId.getValue())).thenReturn(true);

        boolean result = studentJpaRepository.existsById(studentId.getValue());

        assertThat(result).isTrue();
        verify(studentJpaRepository, times(1)).existsById(studentId.getValue());
    }

    @Test
    void existsById_WhenEntityDoesNotExist_ReturnsFalse() {
        when(studentJpaRepository.existsById(studentId.getValue())).thenReturn(false);

        boolean result = studentJpaRepository.existsById(studentId.getValue());

        assertThat(result).isFalse();
        verify(studentJpaRepository, times(1)).existsById(studentId.getValue());
    }

    @Test
    void findByIdentificationNumber_WhenEntityExists_ReturnsOptionalOfEntity() {
        String identificationNumber = "ID123";
        when(studentJpaRepository.findByIdentificationNumber(identificationNumber)).thenReturn(Optional.of(studentEntity));

        Optional<StudentEntity> result = studentJpaRepository.findByIdentificationNumber(identificationNumber);

        assertThat(result).isPresent().contains(studentEntity);
        verify(studentJpaRepository, times(1)).findByIdentificationNumber(identificationNumber);
    }

    @Test
    void findByIdentificationNumber_WhenEntityDoesNotExist_ReturnsEmptyOptional() {
        String identificationNumber = "ID999";
        when(studentJpaRepository.findByIdentificationNumber(identificationNumber)).thenReturn(Optional.empty());

        Optional<StudentEntity> result = studentJpaRepository.findByIdentificationNumber(identificationNumber);

        assertThat(result).isEmpty();
        verify(studentJpaRepository, times(1)).findByIdentificationNumber(identificationNumber);
    }

    @Test
    void findByCurrentClassroomId_ReturnsListOfEntities() {
        UUID classroomId = UUID.randomUUID();
        List<StudentEntity> entityList = List.of(studentEntity);
        when(studentJpaRepository.findByCurrentClassroomId(classroomId)).thenReturn(entityList);

        List<StudentEntity> result = studentJpaRepository.findByCurrentClassroomId(classroomId);

        assertThat(result).isEqualTo(entityList);
        verify(studentJpaRepository, times(1)).findByCurrentClassroomId(classroomId);
    }

    @Test
    void findByLegalGuardianId_ReturnsListOfEntities() {
        UUID guardianId = UUID.randomUUID();
        List<StudentEntity> entityList = List.of(studentEntity);
        when(studentJpaRepository.findByLegalGuardianId(guardianId)).thenReturn(entityList);

        List<StudentEntity> result = studentJpaRepository.findByLegalGuardianId(guardianId);

        assertThat(result).isEqualTo(entityList);
        verify(studentJpaRepository, times(1)).findByLegalGuardianId(guardianId);
    }

    @Test
    void countByCurrentClassroomId_ReturnsCount() {
        UUID classroomId = UUID.randomUUID();
        long expectedCount = 5L;
        when(studentJpaRepository.countByCurrentClassroomId(classroomId)).thenReturn(expectedCount);

        long result = studentJpaRepository.countByCurrentClassroomId(classroomId);

        assertThat(result).isEqualTo(expectedCount);
        verify(studentJpaRepository, times(1)).countByCurrentClassroomId(classroomId);
    }
}