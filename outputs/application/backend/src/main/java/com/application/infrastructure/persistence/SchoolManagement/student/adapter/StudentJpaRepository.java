package com.application.infrastructure.persistence.SchoolManagement.student.adapter;

import com.application.infrastructure.persistence.SchoolManagement.student.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentJpaRepository extends JpaRepository<StudentEntity, UUID> {
    Optional<StudentEntity> findByIdentificationNumber(String identificationNumber);
    boolean existsByIdentificationNumber(String identificationNumber);
}