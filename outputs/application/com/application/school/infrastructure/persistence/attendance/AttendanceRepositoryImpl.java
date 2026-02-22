package com.application.school.infrastructure.persistence.attendance;

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
public class AttendanceRepositoryImpl implements AttendanceRepository {

    private final AttendanceJpaRepository attendanceJpaRepository;
    private final AttendancePersistenceMapper attendancePersistenceMapper;

    @Override
    public AttendanceRecord save(AttendanceRecord attendanceRecord) {
        var entity = attendancePersistenceMapper.toEntity(attendanceRecord);
        var savedEntity = attendanceJpaRepository.save(entity);
        return attendancePersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<AttendanceRecord> findById(AttendanceRecordId attendanceRecordId) {
        return attendanceJpaRepository.findById(attendanceRecordId.getValue())
                .map(attendancePersistenceMapper::toDomain);
    }

    @Override
    public List<AttendanceRecord> findByStudentIdAndDateBetween(StudentId studentId, LocalDate startDate, LocalDate endDate) {
        return attendanceJpaRepository.findByStudentEntityIdAndDateBetween(studentId.getValue(), startDate, endDate)
                .stream()
                .map(attendancePersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AttendanceRecord> findByStudentIdAndDate(StudentId studentId, LocalDate date) {
        return attendanceJpaRepository.findByStudentEntityIdAndDate(studentId.getValue(), date)
                .map(attendancePersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByStudentIdAndDate(StudentId studentId, LocalDate date) {
        return attendanceJpaRepository.existsByStudentEntityIdAndDate(studentId.getValue(), date);
    }

    @Override
    public void delete(AttendanceRecord attendanceRecord) {
        attendanceJpaRepository.deleteById(attendanceRecord.getId().getValue());
    }
}