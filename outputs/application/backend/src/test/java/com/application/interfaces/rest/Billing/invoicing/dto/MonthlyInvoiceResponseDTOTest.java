package com.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MonthlyInvoiceResponseDTOTest {

    @Test
    void testBuilderAndAccessors() {
        UUID invoiceId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID schoolId = UUID.randomUUID();
        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(15);
        String billingPeriod = "2024-01";
        BigDecimal totalAmount = new BigDecimal("1500.75");
        String status = "PAID";

        MonthlyInvoiceResponseDTO.InvoiceItemResponseDTO item1 = MonthlyInvoiceResponseDTO.InvoiceItemResponseDTO.builder()
                .id(UUID.randomUUID())
                .billingConceptId(UUID.randomUUID())
                .description("Monthly Tuition")
                .quantity(1)
                .unitPrice(new BigDecimal("1500.75"))
                .subtotal(new BigDecimal("1500.75"))
                .build();

        List<MonthlyInvoiceResponseDTO.InvoiceItemResponseDTO> items = List.of(item1);

        MonthlyInvoiceResponseDTO dto = MonthlyInvoiceResponseDTO.builder()
                .id(invoiceId)
                .studentId(studentId)
                .schoolId(schoolId)
                .billingPeriod(billingPeriod)
                .issueDate(issueDate)
                .dueDate(dueDate)
                .totalAmount(totalAmount)
                .status(status)
                .items(items)
                .build();

        assertEquals(invoiceId, dto.getId());
        assertEquals(studentId, dto.getStudentId());
        assertEquals(schoolId, dto.getSchoolId());
        assertEquals(billingPeriod, dto.getBillingPeriod());
        assertEquals(issueDate, dto.getIssueDate());
        assertEquals(dueDate, dto.getDueDate());
        assertEquals(totalAmount, dto.getTotalAmount());
        assertEquals(status, dto.getStatus());
        assertEquals(items, dto.getItems());
        assertEquals(1, dto.getItems().size());
        assertEquals("Monthly Tuition", dto.getItems().get(0).getDescription());
    }

    @Test
    void testNoArgsConstructor() {
        MonthlyInvoiceResponseDTO dto = new MonthlyInvoiceResponseDTO();
        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getStudentId());
        assertNull(dto.getItems());
    }

    @Test
    void testAllArgsConstructor() {
        UUID invoiceId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID schoolId = UUID.randomUUID();
        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(15);
        String billingPeriod = "2024-01";
        BigDecimal totalAmount = new BigDecimal("1500.75");
        String status = "PENDING";
        List<MonthlyInvoiceResponseDTO.InvoiceItemResponseDTO> items = List.of();

        MonthlyInvoiceResponseDTO dto = new MonthlyInvoiceResponseDTO(
                invoiceId, studentId, schoolId, billingPeriod,
                issueDate, dueDate, totalAmount, status, items
        );

        assertEquals(invoiceId, dto.getId());
        assertEquals(studentId, dto.getStudentId());
        assertEquals(schoolId, dto.getSchoolId());
        assertEquals(billingPeriod, dto.getBillingPeriod());
        assertEquals(issueDate, dto.getIssueDate());
        assertEquals(dueDate, dto.getDueDate());
        assertEquals(totalAmount, dto.getTotalAmount());
        assertEquals(status, dto.getStatus());
        assertEquals(items, dto.getItems());
    }

    @Test
    void testInvoiceItemResponseDTOBuilderAndAccessors() {
        UUID itemId = UUID.randomUUID();
        UUID conceptId = UUID.randomUUID();
        String description = "Activity Fee";
        Integer quantity = 1;
        BigDecimal unitPrice = new BigDecimal("200.00");
        BigDecimal subtotal = new BigDecimal("200.00");

        MonthlyInvoiceResponseDTO.InvoiceItemResponseDTO itemDto = MonthlyInvoiceResponseDTO.InvoiceItemResponseDTO.builder()
                .id(itemId)
                .billingConceptId(conceptId)
                .description(description)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .subtotal(subtotal)
                .build();

        assertEquals(itemId, itemDto.getId());
        assertEquals(conceptId, itemDto.getBillingConceptId());
        assertEquals(description, itemDto.getDescription());
        assertEquals(quantity, itemDto.getQuantity());
        assertEquals(unitPrice, itemDto.getUnitPrice());
        assertEquals(subtotal, itemDto.getSubtotal());
    }

    @Test
    void testInvoiceItemResponseDTONoArgsConstructor() {
        MonthlyInvoiceResponseDTO.InvoiceItemResponseDTO itemDto = new MonthlyInvoiceResponseDTO.InvoiceItemResponseDTO();
        assertNotNull(itemDto);
        assertNull(itemDto.getId());
        assertNull(itemDto.getDescription());
    }

    @Test
    void testInvoiceItemResponseDTOAllArgsConstructor() {
        UUID itemId = UUID.randomUUID();
        UUID conceptId = UUID.randomUUID();
        String description = "Book";
        Integer quantity = 2;
        BigDecimal unitPrice = new BigDecimal("50.00");
        BigDecimal subtotal = new BigDecimal("100.00");

        MonthlyInvoiceResponseDTO.InvoiceItemResponseDTO itemDto = new MonthlyInvoiceResponseDTO.InvoiceItemResponseDTO(
                itemId, conceptId, description, quantity, unitPrice, subtotal
        );

        assertEquals(itemId, itemDto.getId());
        assertEquals(conceptId, itemDto.getBillingConceptId());
        assertEquals(description, itemDto.getDescription());
        assertEquals(quantity, itemDto.getQuantity());
        assertEquals(unitPrice, itemDto.getUnitPrice());
        assertEquals(subtotal, itemDto.getSubtotal());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID invoiceId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        LocalDate issueDate = LocalDate.now();

        MonthlyInvoiceResponseDTO dto1 = MonthlyInvoiceResponseDTO.builder()
                .id(invoiceId)
                .studentId(studentId)
                .schoolId(UUID.randomUUID())
                .billingPeriod("2024-01")
                .issueDate(issueDate)
                .dueDate(issueDate.plusDays(10))
                .totalAmount(new BigDecimal("1000.00"))
                .status("PENDING")
                .items(List.of())
                .build();

        MonthlyInvoiceResponseDTO dto2 = MonthlyInvoiceResponseDTO.builder()
                .id(invoiceId)
                .studentId(studentId)
                .schoolId(UUID.randomUUID())
                .billingPeriod("2024-01")
                .issueDate(issueDate)
                .dueDate(issueDate.plusDays(10))
                .totalAmount(new BigDecimal("1000.00"))
                .status("PENDING")
                .items(List.of())
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        MonthlyInvoiceResponseDTO dto = MonthlyInvoiceResponseDTO.builder()
                .id(UUID.randomUUID())
                .studentId(UUID.randomUUID())
                .billingPeriod("2024-01")
                .totalAmount(new BigDecimal("500.00"))
                .status("PENDING")
                .build();

        String toStringResult = dto.toString();
        assertTrue(toStringResult.contains("MonthlyInvoiceResponseDTO"));
        assertTrue(toStringResult.contains("billingPeriod=2024-01"));
        assertTrue(toStringResult.contains("status=PENDING"));
    }
}