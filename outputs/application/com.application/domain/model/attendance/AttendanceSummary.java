package com.application.domain.model.attendance;

import com.application.domain.model.studentmanagement.Student;
import com.application.domain.model.studentmanagement.StudentId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceSummary {
    private AttendanceSummaryId id;
    private Student student;
    private YearMonth monthYear;
    private Integer daysPresent;
    private Integer daysAbsent;
    private Integer daysLate;
    private Integer daysExcused;

    public StudentId getStudentId() {
        return student != null ? student.getId() : null;
    }

    public void calculateFromRecords(java.util.List<AttendanceRecord> records) {
        if (records == null || records.isEmpty()) {
            this.daysPresent = 0;
            this.daysAbsent = 0;
            this.daysLate = 0;
            this.daysExcused = 0;
            return;
        }

        long presentCount = records.stream()
                .filter(record -> record.getStatus() != null)
                .filter(record -> record.getStatus().isPresent())
                .count();
        long absentCount = records.stream()
                .filter(record -> record.getStatus() != null)
                .filter(record -> record.getStatus().isAbsent())
                .count();
        long lateCount = records.stream()
                .filter(record -> record.getStatus() != null)
                .filter(record -> record.getStatus().isLate())
                .count();
        long excusedCount = records.stream()
                .filter(record -> record.getStatus() != null)
                .filter(record -> record.getStatus().isExcused())
                .count();

        this.daysPresent = Math.toIntExact(presentCount);
        this.daysAbsent = Math.toIntExact(absentCount);
        this.daysLate = Math.toIntExact(lateCount);
        this.daysExcused = Math.toIntExact(excusedCount);
    }

    public Integer getTotalDays() {
        return (daysPresent != null ? daysPresent : 0) +
                (daysAbsent != null ? daysAbsent : 0) +
                (daysLate != null ? daysLate : 0) +
                (daysExcused != null ? daysExcused : 0);
    }

    public Double getAttendanceRate() {
        Integer total = getTotalDays();
        if (total == 0) {
            return 0.0;
        }
        Integer present = daysPresent != null ? daysPresent : 0;
        return (present * 100.0) / total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttendanceSummary that = (AttendanceSummary) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}