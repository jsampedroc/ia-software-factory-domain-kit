package com.application.school.infrastructure.api.mapper;

import com.application.school.application.billing.dto.GenerateInvoiceCommand;
import com.application.school.application.billing.dto.InvoiceResponse;
import com.application.school.application.billing.dto.RegisterPaymentCommand;
import com.application.school.infrastructure.api.billing.dto.GenerateInvoiceRequest;
import com.application.school.infrastructure.api.billing.dto.InvoiceApiResponse;
import com.application.school.infrastructure.api.billing.dto.RegisterPaymentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BillingApiMapper {

    @Mapping(target = "studentId", source = "studentId")
    @Mapping(target = "monthYear", source = "monthYear")
    @Mapping(target = "items", source = "items")
    GenerateInvoiceCommand toGenerateInvoiceCommand(GenerateInvoiceRequest request);

    @Mapping(target = "invoiceId", source = "invoiceId.value")
    @Mapping(target = "studentId", source = "studentId.value")
    @Mapping(target = "issueDate", source = "issueDate")
    @Mapping(target = "dueDate", source = "dueDate")
    @Mapping(target = "monthYear", source = "monthYear")
    @Mapping(target = "totalAmount", source = "totalAmount")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "paymentDate", source = "paymentDate")
    @Mapping(target = "items", source = "items")
    @Mapping(target = "payments", source = "payments")
    InvoiceApiResponse toInvoiceApiResponse(InvoiceResponse response);

    @Mapping(target = "invoiceId", source = "invoiceId")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "paymentMethod", source = "paymentMethod")
    @Mapping(target = "reference", source = "reference")
    RegisterPaymentCommand toRegisterPaymentCommand(RegisterPaymentRequest request);
}