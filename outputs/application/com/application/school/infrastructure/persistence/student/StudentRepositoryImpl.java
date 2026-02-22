package com.application.school.infrastructure.persistence.student;

import com.application.school.domain.student.model.Student;
import com.application.school.domain.student.model.StudentId;
import com.application.school.domain.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class StudentRepositoryImpl implements StudentRepository {

    private final StudentJpaRepository studentJpaRepository;
    private final StudentPersistenceMapper studentPersistenceMapper;

    @Override
    public Student save(Student student) {
        var entity = studentPersistenceMapper.toEntity(student);
        var savedEntity = studentJpaRepository.save(entity);
        return studentPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Student> findById(StudentId studentId) {
        return studentJpaRepository.findById(studentId.getValue())
                .map(studentPersistenceMapper::toDomain);
    }

    @Override
    public List<Student> findAll() {
        return studentJpaRepository.findAll()
                .stream()
                .map(studentPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(StudentId studentId) {
        studentJpaRepository.deleteById(studentId.getValue());
    }

    @Override
    public boolean existsByLegalId(String legalId) {
        return studentJpaRepository.existsByLegalId(legalId);
    }

    @Override
    public Optional<Student> findByLegalId(String legalId) {
        return studentJpaRepository.findByLegalId(legalId)
                .map(studentPersistenceMapper::toDomain);
    }
}