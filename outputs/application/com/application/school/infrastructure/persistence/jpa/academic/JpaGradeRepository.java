package com.application.school.infrastructure.persistence.jpa.academic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaGradeRepository extends JpaRepository<GradeJpaEntity, Long> {
}