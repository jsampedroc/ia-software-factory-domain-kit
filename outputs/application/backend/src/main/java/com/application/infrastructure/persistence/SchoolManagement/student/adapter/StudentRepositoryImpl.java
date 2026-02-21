package com.application.infrastructure.persistence.SchoolManagement.student.adapter;

import com.application.domain.SchoolManagement.student.domain.Student;
import com.application.domain.SchoolManagement.student.domain.repository.StudentRepository;
import com.application.domain.SchoolManagement.valueobject.StudentId;
import com.application.infrastructure.persistence.SchoolManagement.student.entity.StudentEntity;
import com.application.infrastructure.persistence.SchoolManagement.student.mapper.StudentEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudentRepositoryImpl implements StudentRepository {

    private final StudentJpaRepository studentJpaRepository;
    private final StudentEntityMapper studentEntityMapper;

    @Override
    public Student save(Student student) {
        StudentEntity entity = studentEntityMapper.toEntity(student);
        StudentEntity savedEntity = studentJpaRepository.save(entity);
        return studentEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Student> findById(StudentId studentId) {
        return studentJpaRepository.findById(studentId.getValue())
                .map(studentEntityMapper::toDomain);
    }

    @Override
    public boolean existsById(StudentId studentId) {
        return studentJpaRepository.existsById(studentId.getValue());
    }

    @Override
    public void delete(Student student) {
        StudentEntity entity = studentEntityMapper.toEntity(student);
        studentJpaRepository.delete(entity);
    }
}