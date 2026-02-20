package com.application.infrastructure.persistence.adapter;

import com.application.domain.model.studentmanagement.Student;
import com.application.domain.model.studentmanagement.StudentId;
import com.application.domain.ports.out.StudentRepository;
import com.application.infrastructure.persistence.jpa.StudentJpaRepository;
import com.application.infrastructure.persistence.jpa.StudentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class StudentRepositoryAdapter implements StudentRepository {

    private final StudentJpaRepository studentJpaRepository;
    private final StudentMapperAdapter studentMapperAdapter;

    @Override
    public Student save(Student student) {
        StudentEntity entity = studentMapperAdapter.toEntity(student);
        StudentEntity savedEntity = studentJpaRepository.save(entity);
        return studentMapperAdapter.toDomain(savedEntity);
    }

    @Override
    public Optional<Student> findById(StudentId studentId) {
        return studentJpaRepository.findById(studentId.getValue())
                .map(studentMapperAdapter::toDomain);
    }

    @Override
    public List<Student> findAll() {
        return studentJpaRepository.findAll()
                .stream()
                .map(studentMapperAdapter::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(StudentId studentId) {
        studentJpaRepository.deleteById(studentId.getValue());
    }

    @Override
    public boolean existsById(StudentId studentId) {
        return studentJpaRepository.existsById(studentId.getValue());
    }

    @Override
    public Optional<Student> findByNationalId(String nationalId) {
        return studentJpaRepository.findByNationalId(nationalId)
                .map(studentMapperAdapter::toDomain);
    }

    @Override
    public List<Student> findByStatus(String status) {
        return studentJpaRepository.findByStatus(status)
                .stream()
                .map(studentMapperAdapter::toDomain)
                .collect(Collectors.toList());
    }
}