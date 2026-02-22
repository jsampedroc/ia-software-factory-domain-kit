package com.application.school.infrastructure.persistence.student;

import com.application.school.infrastructure.persistence.student.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentJpaRepository extends JpaRepository<StudentEntity, UUID> {
    Optional<StudentEntity> findByLegalId(String legalId);
}