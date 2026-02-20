package com.application.school.application.mappers;

import com.application.school.application.dtos.InvoiceDTO;
import com.application.school.application.dtos.InvoiceItemDTO;
import com.application.school.application.dtos.PaymentDTO;
import com.application.school.domain.billing.model.Invoice;
import com.application.school.domain.billing.model.InvoiceItem;
import com.application.school.domain.billing.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BillingMapper {
    BillingMapper INSTANCE = Mappers.getMapper(BillingMapper.class);

    @Mapping(source = "invoiceId.value", target = "invoiceId")
    @Mapping(source = "student.studentId.value", target = "studentId")
    InvoiceDTO toDTO(Invoice invoice);

    @Mapping(source = "invoiceId", target = "invoiceId.value")
    @Mapping(source = "studentId", target = "student.studentId.value")
    Invoice toDomain(InvoiceDTO invoiceDTO);

    @Mapping(source = "itemId.value", target = "itemId")
    InvoiceItemDTO toDTO(InvoiceItem invoiceItem);

    @Mapping(source = "itemId", target = "itemId.value")
    InvoiceItem toDomain(InvoiceItemDTO invoiceItemDTO);

    @Mapping(source = "paymentId.value", target = "paymentId")
    PaymentDTO toDTO(Payment payment);

    @Mapping(source = "paymentId", target = "paymentId.value")
    Payment toDomain(PaymentDTO paymentDTO);
}