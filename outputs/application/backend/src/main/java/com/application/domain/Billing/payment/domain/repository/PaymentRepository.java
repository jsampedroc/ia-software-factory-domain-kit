package com.application.domain.Billing.payment.domain.repository;

import com.application.domain.Billing.payment.domain.Payment;
import com.application.domain.Billing.payment.valueobject.PaymentId;
import com.application.domain.shared.Repository;

import java.util.Optional;

public interface PaymentRepository extends Repository<Payment, PaymentId> {
    Optional<Payment> findById(PaymentId id);
    Payment save(Payment payment);
    void delete(Payment payment);
}