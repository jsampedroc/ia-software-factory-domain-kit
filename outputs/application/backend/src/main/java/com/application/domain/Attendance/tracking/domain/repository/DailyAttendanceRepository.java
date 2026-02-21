package com.application.domain.Attendance.tracking.domain.repository;

import com.application.domain.Attendance.tracking.domain.DailyAttendance;
import com.application.domain.Attendance.tracking.valueobject.DailyAttendanceId;
import com.application.domain.SchoolManagement.valueobject.ClassroomId;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyAttendanceRepository {
    Optional<DailyAttendance> findById(DailyAttendanceId id);
    Optional<DailyAttendance> findByClassroomIdAndDate(ClassroomId classroomId, LocalDate date);
    boolean existsByClassroomIdAndDate(ClassroomId classroomId, LocalDate date);
    DailyAttendance save(DailyAttendance dailyAttendance);
    void delete(DailyAttendance dailyAttendance);
}