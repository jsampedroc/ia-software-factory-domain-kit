package com.application.application.Attendance.tracking.application.record;

import com.application.domain.Attendance.tracking.domain.DailyAttendance;
import com.application.domain.Attendance.tracking.domain.DailyAttendanceId;
import com.application.domain.Attendance.tracking.domain.repository.DailyAttendanceRepository;
import com.application.domain.Attendance.tracking.domain.AttendanceRecord;
import com.application.domain.Attendance.tracking.domain.AttendanceRecordId;
import com.application.domain.Attendance.tracking.domain.repository.AttendanceRecordRepository;
import com.application.domain.SchoolManagement.student.domain.Student;
import com.application.domain.SchoolManagement.student.domain.StudentId;
import com.application.domain.SchoolManagement.student.domain.repository.StudentRepository;
import com.application.domain.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateAttendanceRecordCommandHandler {

    private final AttendanceRecordRepository attendanceRecordRepository;
    private final DailyAttendanceRepository dailyAttendanceRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public void handle(CreateAttendanceRecordCommand command) {
        DailyAttendanceId dailyAttendanceId = new DailyAttendanceId(command.dailyAttendanceId());
        DailyAttendance dailyAttendance = dailyAttendanceRepository.findById(dailyAttendanceId)
                .orElseThrow(() -> new DomainException("DailyAttendance not found with id: " + dailyAttendanceId));

        StudentId studentId = new StudentId(command.studentId());
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new DomainException("Student not found with id: " + studentId));

        AttendanceRecordId recordId = attendanceRecordRepository.nextIdentity();

        AttendanceRecord newRecord = AttendanceRecord.create(
                recordId,
                dailyAttendanceId,
                studentId,
                command.status(),
                command.notes()
        );

        attendanceRecordRepository.save(newRecord);
    }
}