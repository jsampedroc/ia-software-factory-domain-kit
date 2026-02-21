package com.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MonthlyInvoiceEntityTest {

    @Test
    void testEntityCreationAndGetters() {
        UUID id = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID schoolId = UUID.randomUUID();
        YearMonth billingPeriod = YearMonth.of(2024, 1);
        LocalDate issueDate = LocalDate.of(2024, 1, 1);
        LocalDate dueDate = LocalDate.of(2024, 1, 15);
        BigDecimal totalAmount = new BigDecimal("1500.50");
        String status = "PENDING";

        MonthlyInvoiceEntity entity = new MonthlyInvoiceEntity();
        entity.setId(id);
        entity.setStudentId(studentId);
        entity.setSchoolId(schoolId);
        entity.setBillingPeriod(billingPeriod);
        entity.setIssueDate(issueDate);
        entity.setDueDate(dueDate);
        entity.setTotalAmount(totalAmount);
        entity.setStatus(status);

        assertEquals(id, entity.getId());
        assertEquals(studentId, entity.getStudentId());
        assertEquals(schoolId, entity.getSchoolId());
        assertEquals(billingPeriod, entity.getBillingPeriod());
        assertEquals(issueDate, entity.getIssueDate());
        assertEquals(dueDate, entity.getDueDate());
        assertEquals(totalAmount, entity.getTotalAmount());
        assertEquals(status, entity.getStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        MonthlyInvoiceEntity entity1 = new MonthlyInvoiceEntity();
        entity1.setId(id1);
        MonthlyInvoiceEntity entity2 = new MonthlyInvoiceEntity();
        entity2.setId(id1);
        MonthlyInvoiceEntity entity3 = new MonthlyInvoiceEntity();
        entity3.setId(id2);

        assertEquals(entity1, entity2);
        assertNotEquals(entity1, entity3);
        assertEquals(entity1.hashCode(), entity2.hashCode());
        assertNotEquals(entity1.hashCode(), entity3.hashCode());
    }

    @Test
    void testToString() {
        UUID id = UUID.randomUUID();
        MonthlyInvoiceEntity entity = new MonthlyInvoiceEntity();
        entity.setId(id);
        entity.setStatus("PENDING");

        String toString = entity.toString();
        assertTrue(toString.contains(id.toString()));
        assertTrue(toString.contains("PENDING"));
    }

    @Test
    void testNoArgsConstructor() {
        MonthlyInvoiceEntity entity = new MonthlyInvoiceEntity();
        assertNotNull(entity);
        assertNull(entity.getId());
        assertNull(entity.getStatus());
    }

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID schoolId = UUID.randomUUID();
        YearMonth billingPeriod = YearMonth.of(2024, 1);
        LocalDate issueDate = LocalDate.of(2024, 1, 1);
        LocalDate dueDate = LocalDate.of(2024, 1, 15);
        BigDecimal totalAmount = new BigDecimal("1500.50");
        String status = "PENDING";

        MonthlyInvoiceEntity entity = new MonthlyInvoiceEntity(id, studentId, schoolId, billingPeriod, issueDate, dueDate, totalAmount, status, null);

        assertEquals(id, entity.getId());
        assertEquals(studentId, entity.getStudentId());
        assertEquals(schoolId, entity.getSchoolId());
        assertEquals(billingPeriod, entity.getBillingPeriod());
        assertEquals(issueDate, entity.getIssueDate());
        assertEquals(dueDate, entity.getDueDate());
        assertEquals(totalAmount, entity.getTotalAmount());
        assertEquals(status, entity.getStatus());
        assertNull(entity.getInvoiceItems());
    }

    @Test
    void testBuilder() {
        UUID id = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID schoolId = UUID.randomUUID();
        YearMonth billingPeriod = YearMonth.of(2024, 1);
        LocalDate issueDate = LocalDate.of(2024, 1, 1);
        LocalDate dueDate = LocalDate.of(2024, 1, 15);
        BigDecimal totalAmount = new BigDecimal("1500.50");
        String status = "PENDING";

        MonthlyInvoiceEntity entity = MonthlyInvoiceEntity.builder()
                .id(id)
                .studentId(studentId)
                .schoolId(schoolId)
                .billingPeriod(billingPeriod)
                .issueDate(issueDate)
                .dueDate(dueDate)
                .totalAmount(totalAmount)
                .status(status)
                .build();

        assertEquals(id, entity.getId());
        assertEquals(studentId, entity.getStudentId());
        assertEquals(schoolId, entity.getSchoolId());
        assertEquals(billingPeriod, entity.getBillingPeriod());
        assertEquals(issueDate, entity.getIssueDate());
        assertEquals(dueDate, entity.getDueDate());
        assertEquals(totalAmount, entity.getTotalAmount());
        assertEquals(status, entity.getStatus());
    }
}