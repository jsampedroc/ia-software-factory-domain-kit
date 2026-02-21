package com.application.infrastructure.persistence.SchoolManagement.school.adapter;

import com.application.domain.SchoolManagement.school.domain.School;
import com.application.domain.SchoolManagement.school.domain.repository.SchoolRepository;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import com.application.infrastructure.persistence.SchoolManagement.school.entity.SchoolEntity;
import com.application.infrastructure.persistence.SchoolManagement.school.mapper.SchoolEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SchoolRepositoryImpl implements SchoolRepository {

    private final SchoolJpaRepository schoolJpaRepository;
    private final SchoolEntityMapper schoolEntityMapper;

    @Override
    public School save(School school) {
        SchoolEntity entity = schoolEntityMapper.toEntity(school);
        SchoolEntity savedEntity = schoolJpaRepository.save(entity);
        return schoolEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<School> findById(SchoolId schoolId) {
        return schoolJpaRepository.findById(schoolId.getValue())
                .map(schoolEntityMapper::toDomain);
    }

    @Override
    public boolean existsByName(String name) {
        return schoolJpaRepository.existsByName(name);
    }

    @Override
    public void delete(School school) {
        SchoolEntity entity = schoolEntityMapper.toEntity(school);
        schoolJpaRepository.delete(entity);
    }
}