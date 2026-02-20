package com.application.school.infrastructure.persistence.jpa.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaStudentRepository extends JpaRepository<StudentJpaEntity, String> {
}