package com.application.infrastructure.persistence.Attendance.tracking.entity;

import com.application.domain.Attendance.tracking.domain.DailyAttendance;
import com.application.domain.Attendance.tracking.domain.DailyAttendanceId;
import com.application.domain.SchoolManagement.school.domain.ClassroomId;
import com.application.domain.SchoolManagement.school.domain.SchoolId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "daily_attendance")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyAttendanceEntity {
    @Id
    private UUID id;

    @Column(name = "classroom_id", nullable = false)
    private UUID classroomId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "school_id", nullable = false)
    private UUID schoolId;

    public static DailyAttendanceEntity fromDomain(DailyAttendance dailyAttendance) {
        return DailyAttendanceEntity.builder()
                .id(dailyAttendance.getId().getValue())
                .classroomId(dailyAttendance.getClassroomId().getValue())
                .date(dailyAttendance.getDate())
                .schoolId(dailyAttendance.getSchoolId().getValue())
                .build();
    }

    public DailyAttendance toDomain() {
        return DailyAttendance.builder()
                .id(new DailyAttendanceId(this.id))
                .classroomId(new ClassroomId(this.classroomId))
                .date(this.date)
                .schoolId(new SchoolId(this.schoolId))
                .build();
    }
}