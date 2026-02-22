package com.application.school.infrastructure.persistence.academic;

import com.application.school.infrastructure.persistence.academic.entity.GradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GradeJpaRepository extends JpaRepository<GradeEntity, Long> {
    Optional<GradeEntity> findByName(String name);
}