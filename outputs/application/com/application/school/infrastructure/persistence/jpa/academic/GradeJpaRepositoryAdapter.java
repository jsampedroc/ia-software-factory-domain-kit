package com.application.school.infrastructure.persistence.jpa.academic;

import com.application.school.domain.academic.model.Grade;
import com.application.school.domain.academic.model.GradeId;
import com.application.school.domain.academic.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GradeJpaRepositoryAdapter implements GradeRepository {

    private final JpaGradeRepository jpaGradeRepository;
    private final GradeJpaMapper mapper;

    @Override
    public Optional<Grade> findById(GradeId gradeId) {
        return jpaGradeRepository.findById(gradeId.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public List<Grade> findAll() {
        return jpaGradeRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Grade save(Grade grade) {
        GradeJpaEntity entity = mapper.toEntity(grade);
        GradeJpaEntity savedEntity = jpaGradeRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(GradeId gradeId) {
        jpaGradeRepository.deleteById(gradeId.getValue());
    }

    @Override
    public boolean existsById(GradeId gradeId) {
        return jpaGradeRepository.existsById(gradeId.getValue());
    }

    @Override
    public Optional<Grade> findByName(String name) {
        return jpaGradeRepository.findByName(name)
                .map(mapper::toDomain);
    }
}