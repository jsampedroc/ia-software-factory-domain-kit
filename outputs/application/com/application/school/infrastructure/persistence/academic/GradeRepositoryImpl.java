package com.application.school.infrastructure.persistence.academic;

import com.application.school.domain.academic.model.Grade;
import com.application.school.domain.academic.model.GradeId;
import com.application.school.domain.academic.repository.GradeRepository;
import com.application.school.infrastructure.persistence.academic.entity.GradeEntity;
import com.application.school.infrastructure.persistence.academic.mapper.GradePersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GradeRepositoryImpl implements GradeRepository {

    private final GradeJpaRepository gradeJpaRepository;
    private final GradePersistenceMapper gradePersistenceMapper;

    @Override
    public Grade save(Grade grade) {
        GradeEntity entity = gradePersistenceMapper.toEntity(grade);
        GradeEntity savedEntity = gradeJpaRepository.save(entity);
        return gradePersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Grade> findById(GradeId gradeId) {
        return gradeJpaRepository.findById(gradeId.getValue())
                .map(gradePersistenceMapper::toDomain);
    }

    @Override
    public List<Grade> findAll() {
        return gradeJpaRepository.findAll()
                .stream()
                .map(gradePersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(GradeId gradeId) {
        gradeJpaRepository.deleteById(gradeId.getValue());
    }

    @Override
    public boolean existsByName(String name) {
        return gradeJpaRepository.existsByName(name);
    }

    @Override
    public Optional<Grade> findByName(String name) {
        return gradeJpaRepository.findByName(name)
                .map(gradePersistenceMapper::toDomain);
    }
}