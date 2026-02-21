package com.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GenerateInvoiceRequestDTOTest {

    @Test
    void testNoArgsConstructor() {
        GenerateInvoiceRequestDTO dto = new GenerateInvoiceRequestDTO();
        assertNotNull(dto);
        assertNull(dto.getStudentId());
        assertNull(dto.getBillingPeriod());
        assertNull(dto.getSchoolId());
    }

    @Test
    void testAllArgsConstructor() {
        String studentId = "student-123";
        YearMonth billingPeriod = YearMonth.of(2024, 10);
        String schoolId = "school-456";

        GenerateInvoiceRequestDTO dto = new GenerateInvoiceRequestDTO(studentId, billingPeriod, schoolId);

        assertEquals(studentId, dto.getStudentId());
        assertEquals(billingPeriod, dto.getBillingPeriod());
        assertEquals(schoolId, dto.getSchoolId());
    }

    @Test
    void testBuilder() {
        String studentId = "student-789";
        YearMonth billingPeriod = YearMonth.of(2024, 11);
        String schoolId = "school-999";

        GenerateInvoiceRequestDTO dto = GenerateInvoiceRequestDTO.builder()
                .studentId(studentId)
                .billingPeriod(billingPeriod)
                .schoolId(schoolId)
                .build();

        assertEquals(studentId, dto.getStudentId());
        assertEquals(billingPeriod, dto.getBillingPeriod());
        assertEquals(schoolId, dto.getSchoolId());
    }

    @Test
    void testSettersAndGetters() {
        GenerateInvoiceRequestDTO dto = new GenerateInvoiceRequestDTO();

        String studentId = "student-111";
        YearMonth billingPeriod = YearMonth.of(2024, 12);
        String schoolId = "school-222";

        dto.setStudentId(studentId);
        dto.setBillingPeriod(billingPeriod);
        dto.setSchoolId(schoolId);

        assertEquals(studentId, dto.getStudentId());
        assertEquals(billingPeriod, dto.getBillingPeriod());
        assertEquals(schoolId, dto.getSchoolId());
    }

    @Test
    void testEqualsAndHashCode() {
        String studentId = "student-555";
        YearMonth billingPeriod = YearMonth.of(2024, 9);
        String schoolId = "school-777";

        GenerateInvoiceRequestDTO dto1 = new GenerateInvoiceRequestDTO(studentId, billingPeriod, schoolId);
        GenerateInvoiceRequestDTO dto2 = new GenerateInvoiceRequestDTO(studentId, billingPeriod, schoolId);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        String studentId = "student-888";
        YearMonth billingPeriod = YearMonth.of(2024, 8);
        String schoolId = "school-333";

        GenerateInvoiceRequestDTO dto = new GenerateInvoiceRequestDTO(studentId, billingPeriod, schoolId);
        String toStringResult = dto.toString();

        assertTrue(toStringResult.contains(studentId));
        assertTrue(toStringResult.contains(billingPeriod.toString()));
        assertTrue(toStringResult.contains(schoolId));
        assertTrue(toStringResult.contains("GenerateInvoiceRequestDTO"));
    }
}