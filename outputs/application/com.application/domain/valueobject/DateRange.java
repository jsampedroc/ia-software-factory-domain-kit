package com.application.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class DateRange {
    private LocalDate startDate;
    private LocalDate endDate;

    public boolean contains(LocalDate date) {
        if (date == null) {
            return false;
        }
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    public boolean overlaps(DateRange other) {
        if (other == null) {
            return false;
        }
        return !this.startDate.isAfter(other.endDate) && !this.endDate.isBefore(other.startDate);
    }

    public boolean isValid() {
        return startDate != null && endDate != null && !startDate.isAfter(endDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateRange dateRange = (DateRange) o;
        return Objects.equals(startDate, dateRange.startDate) && Objects.equals(endDate, dateRange.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate);
    }
}