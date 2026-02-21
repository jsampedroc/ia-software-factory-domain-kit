package com.application.infrastructure.persistence.Billing.payment.adapter;

import com.application.infrastructure.persistence.Billing.payment.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, UUID> {
}