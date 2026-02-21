package com.application.application.Billing.payment.application.process;

import com.application.domain.Billing.payment.domain.Payment;
import com.application.domain.Billing.payment.domain.PaymentRepository;
import com.application.domain.Billing.invoicing.domain.MonthlyInvoice;
import com.application.domain.Billing.invoicing.domain.MonthlyInvoiceRepository;
import com.application.domain.exception.DomainException;
import com.application.domain.shared.valueobject.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProcessPaymentCommandHandler {

    private final PaymentRepository paymentRepository;
    private final MonthlyInvoiceRepository monthlyInvoiceRepository;

    @Transactional
    public Payment handle(ProcessPaymentCommand command) {
        MonthlyInvoice invoice = monthlyInvoiceRepository.findById(command.getMonthlyInvoiceId())
                .orElseThrow(() -> new DomainException("Monthly invoice not found with id: " + command.getMonthlyInvoiceId()));

        validatePaymentAmount(command.getAmount(), invoice);

        Payment payment = Payment.builder()
                .monthlyInvoiceId(command.getMonthlyInvoiceId())
                .amount(new Money(command.getAmount(), invoice.getTotalAmount().currency()))
                .paymentDate(command.getPaymentDate() != null ? command.getPaymentDate() : LocalDateTime.now())
                .paymentMethod(command.getPaymentMethod())
                .transactionReference(command.getTransactionReference())
                .confirmed(command.isConfirmed())
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        if (payment.isConfirmed()) {
            invoice.registerPayment(savedPayment.getAmount());
            monthlyInvoiceRepository.save(invoice);
        }

        return savedPayment;
    }

    private void validatePaymentAmount(Money paymentAmount, MonthlyInvoice invoice) {
        if (paymentAmount.amount().compareTo(invoice.getRemainingBalance().amount()) > 0) {
            throw new DomainException("Payment amount cannot exceed the invoice remaining balance.");
        }
        if (paymentAmount.amount().signum() <= 0) {
            throw new DomainException("Payment amount must be greater than zero.");
        }
        if (!paymentAmount.currency().equals(invoice.getTotalAmount().currency())) {
            throw new DomainException("Payment currency must match the invoice currency.");
        }
    }
}