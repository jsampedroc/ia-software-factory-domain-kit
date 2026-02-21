package com.application.interfaces.rest.Billing.payment;

import com.application.application.Billing.payment.application.process.ProcessPaymentCommand;
import com.application.application.Billing.payment.application.process.ProcessPaymentCommandHandler;
import com.application.application.Billing.payment.application.retrieve.GetPaymentQuery;
import com.application.application.Billing.payment.application.retrieve.GetPaymentQueryHandler;
import com.application.interfaces.rest.Billing.payment.dto.PaymentResponseDTO;
import com.application.interfaces.rest.Billing.payment.dto.ProcessPaymentRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final ProcessPaymentCommandHandler processPaymentCommandHandler;
    private final GetPaymentQueryHandler getPaymentQueryHandler;

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> processPayment(@RequestBody ProcessPaymentRequestDTO request) {
        ProcessPaymentCommand command = ProcessPaymentCommand.builder()
                .monthlyInvoiceId(request.getMonthlyInvoiceId())
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .transactionReference(request.getTransactionReference())
                .build();

        PaymentResponseDTO response = processPaymentCommandHandler.handle(command);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> getPayment(@PathVariable String id) {
        GetPaymentQuery query = new GetPaymentQuery(id);
        PaymentResponseDTO response = getPaymentQueryHandler.handle(query);
        return ResponseEntity.ok(response);
    }
}