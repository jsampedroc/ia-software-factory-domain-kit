package com.application.infrastructure.persistence.Billing.payment.mapper;

import com.application.domain.Billing.payment.domain.Payment;
import com.application.domain.Billing.payment.domain.PaymentId;
import com.application.infrastructure.persistence.Billing.payment.entity.PaymentEntity;
import com.application.domain.shared.valueobject.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEntityMapper {

    public PaymentEntity toEntity(Payment payment) {
        if (payment == null) {
            return null;
        }

        PaymentEntity entity = new PaymentEntity();
        entity.setId(payment.getId().getValue());
        entity.setMonthlyInvoiceId(payment.getMonthlyInvoiceId().getValue());
        entity.setAmount(payment.getAmount().getAmount());
        entity.setCurrency(payment.getAmount().getCurrency());
        entity.setPaymentDate(payment.getPaymentDate());
        entity.setPaymentMethod(payment.getPaymentMethod().name());
        entity.setTransactionReference(payment.getTransactionReference());
        entity.setConfirmed(payment.isConfirmed());
        return entity;
    }

    public Payment toDomain(PaymentEntity entity) {
        if (entity == null) {
            return null;
        }

        PaymentId id = new PaymentId(entity.getId());
        PaymentId monthlyInvoiceId = new PaymentId(entity.getMonthlyInvoiceId());
        Money amount = new Money(entity.getAmount(), entity.getCurrency());
        Payment.PaymentMethod paymentMethod = Payment.PaymentMethod.valueOf(entity.getPaymentMethod());

        return Payment.builder()
                .id(id)
                .monthlyInvoiceId(monthlyInvoiceId)
                .amount(amount)
                .paymentDate(entity.getPaymentDate())
                .paymentMethod(paymentMethod)
                .transactionReference(entity.getTransactionReference())
                .confirmed(entity.isConfirmed())
                .build();
    }
}