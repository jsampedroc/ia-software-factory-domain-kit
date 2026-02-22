package com.application.infrastructure.persistence.jpa.colegio;

import com.application.domain.model.colegio.Colegio;
import com.application.domain.shared.EntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColegioJpaRepository extends JpaRepository<Colegio, EntityId> {
}