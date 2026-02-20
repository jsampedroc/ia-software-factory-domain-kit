package com.application.school.infrastructure.persistence.jpa.student;

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
public class StudentJpaRepositoryAdapter implements StudentRepository {

    private final JpaStudentRepository jpaStudentRepository;
    private final StudentJpaMapper mapper;

    @Override
    public Student save(Student student) {
        StudentJpaEntity entity = mapper.toEntity(student);
        StudentJpaEntity savedEntity = jpaStudentRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Student> findById(StudentId studentId) {
        return jpaStudentRepository.findById(studentId.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public List<Student> findAll() {
        return jpaStudentRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(StudentId studentId) {
        jpaStudentRepository.deleteById(studentId.getValue());
    }

    @Override
    public boolean existsById(StudentId studentId) {
        return jpaStudentRepository.existsById(studentId.getValue());
    }

    @Override
    public Optional<Student> findByLegalId(String legalId) {
        return jpaStudentRepository.findByLegalId(legalId)
                .map(mapper::toDomain);
    }

    @Override
    public List<Student> findByStatus(String status) {
        return jpaStudentRepository.findByStatus(status)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}