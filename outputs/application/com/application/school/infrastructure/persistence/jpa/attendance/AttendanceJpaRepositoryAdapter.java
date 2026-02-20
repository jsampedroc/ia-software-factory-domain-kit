package com.application.school.infrastructure.persistence.jpa.attendance;

import com.application.school.domain.attendance.model.AttendanceRecord;
import com.application.school.domain.attendance.model.AttendanceRecordId;
import com.application.school.domain.attendance.repository.AttendanceRepository;
import com.application.school.domain.student.model.StudentId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AttendanceJpaRepositoryAdapter implements AttendanceRepository {

    private final JpaAttendanceRepository jpaAttendanceRepository;
    private final AttendanceJpaMapper attendanceJpaMapper;

    @Override
    public AttendanceRecord save(AttendanceRecord attendanceRecord) {
        AttendanceJpaEntity entity = attendanceJpaMapper.toEntity(attendanceRecord);
        AttendanceJpaEntity savedEntity = jpaAttendanceRepository.save(entity);
        return attendanceJpaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<AttendanceRecord> findById(AttendanceRecordId id) {
        return jpaAttendanceRepository.findById(id.getValue())
                .map(attendanceJpaMapper::toDomain);
    }

    @Override
    public void deleteById(AttendanceRecordId id) {
        jpaAttendanceRepository.deleteById(id.getValue());
    }

    @Override
    public List<AttendanceRecord> findByStudentIdAndDateRange(StudentId studentId, LocalDate startDate, LocalDate endDate) {
        return jpaAttendanceRepository.findByStudentIdAndDateBetween(studentId.getValue(), startDate, endDate)
                .stream()
                .map(attendanceJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AttendanceRecord> findByStudentIdAndDate(StudentId studentId, LocalDate date) {
        return jpaAttendanceRepository.findByStudentIdAndDate(studentId.getValue(), date)
                .map(attendanceJpaMapper::toDomain);
    }

    @Override
    public boolean existsByStudentIdAndDate(StudentId studentId, LocalDate date) {
        return jpaAttendanceRepository.existsByStudentIdAndDate(studentId.getValue(), date);
    }
}