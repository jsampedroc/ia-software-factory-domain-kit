package com.application.infrastructure.persistence.Billing.payment.adapter;

import com.application.domain.Billing.payment.domain.Payment;
import com.application.domain.Billing.payment.domain.repository.PaymentRepository;
import com.application.domain.Billing.valueobject.PaymentId;
import com.application.infrastructure.persistence.Billing.payment.entity.PaymentEntity;
import com.application.infrastructure.persistence.Billing.payment.mapper.PaymentEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;
    private final PaymentEntityMapper paymentEntityMapper;

    @Override
    public Payment save(Payment payment) {
        PaymentEntity entity = paymentEntityMapper.toEntity(payment);
        PaymentEntity savedEntity = paymentJpaRepository.save(entity);
        return paymentEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Payment> findById(PaymentId paymentId) {
        return paymentJpaRepository.findById(paymentId.getValue())
                .map(paymentEntityMapper::toDomain);
    }

    @Override
    public boolean existsById(PaymentId paymentId) {
        return paymentJpaRepository.existsById(paymentId.getValue());
    }

    @Override
    public void delete(Payment payment) {
        PaymentEntity entity = paymentEntityMapper.toEntity(payment);
        paymentJpaRepository.delete(entity);
    }
}