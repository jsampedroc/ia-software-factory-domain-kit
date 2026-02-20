package com.application.infrastructure.persistence.adapter;

import com.application.domain.model.schooladministration.School;
import com.application.domain.model.schooladministration.SchoolId;
import com.application.domain.ports.out.SchoolRepository;
import com.application.infrastructure.persistence.jpa.SchoolEntity;
import com.application.infrastructure.persistence.jpa.SchoolJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SchoolRepositoryAdapter implements SchoolRepository {

    private final SchoolJpaRepository schoolJpaRepository;
    private final SchoolMapper schoolMapper;

    @Override
    public School save(School school) {
        SchoolEntity entity = schoolMapper.toEntity(school);
        SchoolEntity savedEntity = schoolJpaRepository.save(entity);
        return schoolMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<School> findById(SchoolId schoolId) {
        return schoolJpaRepository.findById(schoolId.getValue())
                .map(schoolMapper::toDomain);
    }

    @Override
    public List<School> findAll() {
        return schoolJpaRepository.findAll()
                .stream()
                .map(schoolMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(SchoolId schoolId) {
        schoolJpaRepository.deleteById(schoolId.getValue());
    }

    @Override
    public boolean existsById(SchoolId schoolId) {
        return schoolJpaRepository.existsById(schoolId.getValue());
    }
}