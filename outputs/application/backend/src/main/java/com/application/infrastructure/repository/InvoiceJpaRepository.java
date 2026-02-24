package com.application.infrastructure.repository;

import com.application.infrastructure.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceJpaRepository extends JpaRepository<InvoiceEntity, Long> {
}