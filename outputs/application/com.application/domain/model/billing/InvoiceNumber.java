package com.application.domain.model.billing;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceNumber {
    private String value;

    public static InvoiceNumber generate(String schoolCode, LocalDate issueDate, Integer sequentialNumber) {
        Objects.requireNonNull(schoolCode, "School code cannot be null");
        Objects.requireNonNull(issueDate, "Issue date cannot be null");
        Objects.requireNonNull(sequentialNumber, "Sequential number cannot be null");

        String yearMonth = issueDate.format(DateTimeFormatter.ofPattern("yyyyMM"));
        String formattedSeq = String.format("%04d", sequentialNumber);
        String generatedValue = String.format("%s-%s-%s", schoolCode, yearMonth, formattedSeq);

        return InvoiceNumber.builder()
                .value(generatedValue)
                .build();
    }

    public boolean isValid() {
        if (value == null || value.isBlank()) {
            return false;
        }
        // Basic pattern validation: SCHOOLCODE-YYYYMM-NNNN
        return value.matches("^[A-Z0-9]{2,10}-\\d{6}-\\d{4}$");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceNumber that = (InvoiceNumber) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}