package com.application.interfaces.rest.Billing.invoicing;

import com.application.application.Billing.invoicing.application.generate.GenerateMonthlyInvoiceCommand;
import com.application.application.Billing.invoicing.application.generate.GenerateMonthlyInvoiceCommandHandler;
import com.application.application.Billing.invoicing.application.find.FindInvoiceQuery;
import com.application.application.Billing.invoicing.application.find.FindInvoiceQueryHandler;
import com.application.interfaces.rest.Billing.invoicing.dto.GenerateInvoiceRequestDTO;
import com.application.interfaces.rest.Billing.invoicing.dto.MonthlyInvoiceResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final GenerateMonthlyInvoiceCommandHandler generateMonthlyInvoiceCommandHandler;
    private final FindInvoiceQueryHandler findInvoiceQueryHandler;

    @PostMapping
    public ResponseEntity<MonthlyInvoiceResponseDTO> generateMonthlyInvoice(@Valid @RequestBody GenerateInvoiceRequestDTO request) {
        GenerateMonthlyInvoiceCommand command = new GenerateMonthlyInvoiceCommand(
                request.getStudentId(),
                request.getBillingPeriod(),
                request.getSchoolId()
        );

        var invoice = generateMonthlyInvoiceCommandHandler.handle(command);

        MonthlyInvoiceResponseDTO response = MonthlyInvoiceResponseDTO.builder()
                .id(invoice.getId().getValue())
                .studentId(invoice.getStudentId().getValue())
                .billingPeriod(invoice.getBillingPeriod())
                .issueDate(invoice.getIssueDate())
                .dueDate(invoice.getDueDate())
                .totalAmount(invoice.getTotalAmount())
                .status(invoice.getStatus().name())
                .schoolId(invoice.getSchoolId().getValue())
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MonthlyInvoiceResponseDTO> getInvoice(@PathVariable String id) {
        FindInvoiceQuery query = new FindInvoiceQuery(id);
        var invoice = findInvoiceQueryHandler.handle(query);

        return invoice.map(inv -> {
            MonthlyInvoiceResponseDTO response = MonthlyInvoiceResponseDTO.builder()
                    .id(inv.getId().getValue())
                    .studentId(inv.getStudentId().getValue())
                    .billingPeriod(inv.getBillingPeriod())
                    .issueDate(inv.getIssueDate())
                    .dueDate(inv.getDueDate())
                    .totalAmount(inv.getTotalAmount())
                    .status(inv.getStatus().name())
                    .schoolId(inv.getSchoolId().getValue())
                    .build();
            return ResponseEntity.ok(response);
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}