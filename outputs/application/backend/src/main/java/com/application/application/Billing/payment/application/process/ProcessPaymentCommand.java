package com.application.application.Billing.payment.application.process;

import com.application.domain.Billing.valueobject.MonthlyInvoiceId;
import com.application.domain.Billing.valueobject.PaymentId;
import com.application.domain.shared.valueobject.Money;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ProcessPaymentCommand {
    private final PaymentId paymentId;
    private final MonthlyInvoiceId monthlyInvoiceId;
    private final Money amount;
    private final LocalDate paymentDate;
    private final String paymentMethod;
    private final String transactionReference;
    private final boolean confirmed;
}