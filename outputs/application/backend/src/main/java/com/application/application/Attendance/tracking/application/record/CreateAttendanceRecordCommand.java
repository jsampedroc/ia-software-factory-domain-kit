package com.application.application.Attendance.tracking.application.record;

import com.application.domain.Attendance.valueobject.AttendanceRecordId;
import com.application.domain.Attendance.valueobject.DailyAttendanceId;
import com.application.domain.SchoolManagement.valueobject.StudentId;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateAttendanceRecordCommand {
    private final AttendanceRecordId attendanceRecordId;
    private final DailyAttendanceId dailyAttendanceId;
    private final StudentId studentId;
    private final String status;
    private final String notes;
}