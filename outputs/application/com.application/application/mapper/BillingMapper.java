package com.application.application.mapper;

import com.application.domain.model.billing.Invoice;
import com.application.domain.model.billing.Payment;
import com.application.application.dto.InvoiceDTO;
import com.application.application.dto.PaymentDTO;
import com.application.infrastructure.persistence.jpa.InvoiceEntity;
import com.application.infrastructure.persistence.jpa.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BillingMapper {

    BillingMapper INSTANCE = Mappers.getMapper(BillingMapper.class);

    @Mapping(source = "invoiceId.value", target = "invoiceId")
    @Mapping(source = "invoiceNumber.value", target = "invoiceNumber")
    @Mapping(source = "student.studentId.value", target = "studentId")
    @Mapping(source = "billingMonth", target = "billingMonth")
    @Mapping(source = "issueDate", target = "issueDate")
    @Mapping(source = "dueDate", target = "dueDate")
    @Mapping(source = "subtotal", target = "subtotal")
    @Mapping(source = "adjustments", target = "adjustments")
    @Mapping(source = "totalAmount", target = "totalAmount")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "paymentMethod", target = "paymentMethod")
    InvoiceDTO toDto(Invoice invoice);

    @Mapping(source = "invoiceId", target = "invoiceId.value")
    @Mapping(source = "invoiceNumber", target = "invoiceNumber.value")
    @Mapping(target = "student", ignore = true)
    Invoice toDomain(InvoiceDTO invoiceDTO);

    @Mapping(source = "id", target = "invoiceId")
    @Mapping(source = "invoiceNumber", target = "invoiceNumber")
    @Mapping(source = "student.studentId", target = "studentId")
    InvoiceDTO toDto(InvoiceEntity entity);

    @Mapping(source = "invoiceId", target = "id")
    @Mapping(source = "invoiceNumber", target = "invoiceNumber")
    @Mapping(target = "student", ignore = true)
    InvoiceEntity toEntity(InvoiceDTO dto);

    @Mapping(source = "paymentId.value", target = "paymentId")
    @Mapping(source = "invoice.invoiceId.value", target = "invoiceId")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "paymentDate", target = "paymentDate")
    @Mapping(source = "receivedBy", target = "receivedBy")
    @Mapping(source = "referenceNumber", target = "referenceNumber")
    PaymentDTO toDto(Payment payment);

    @Mapping(source = "paymentId", target = "paymentId.value")
    @Mapping(target = "invoice", ignore = true)
    Payment toDomain(PaymentDTO paymentDTO);

    @Mapping(source = "id", target = "paymentId")
    @Mapping(source = "invoice.id", target = "invoiceId")
    PaymentDTO toDto(PaymentEntity entity);

    @Mapping(source = "paymentId", target = "id")
    @Mapping(target = "invoice", ignore = true)
    PaymentEntity toEntity(PaymentDTO dto);
}