package com.application.school.application.mappers;

import com.application.school.application.dtos.AttendanceRecordDTO;
import com.application.school.domain.attendance.model.AttendanceRecord;
import com.application.school.domain.attendance.model.AttendanceRecordId;
import com.application.school.domain.attendance.model.AttendanceStatus;
import com.application.school.domain.student.model.StudentId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {

    AttendanceMapper INSTANCE = Mappers.getMapper(AttendanceMapper.class);

    @Mapping(source = "id.value", target = "recordId")
    @Mapping(source = "studentId.value", target = "studentId")
    @Mapping(source = "status", target = "status")
    AttendanceRecordDTO toDTO(AttendanceRecord attendanceRecord);

    @Mapping(source = "recordId", target = "id", qualifiedByName = "toAttendanceRecordId")
    @Mapping(source = "studentId", target = "studentId", qualifiedByName = "toStudentId")
    @Mapping(source = "status", target = "status")
    AttendanceRecord toDomain(AttendanceRecordDTO attendanceRecordDTO);

    @Named("toAttendanceRecordId")
    default AttendanceRecordId toAttendanceRecordId(UUID recordId) {
        return recordId != null ? new AttendanceRecordId(recordId) : null;
    }

    @Named("toStudentId")
    default StudentId toStudentId(UUID studentId) {
        return studentId != null ? new StudentId(studentId) : null;
    }

    default LocalDate map(String value) {
        return value != null ? LocalDate.parse(value) : null;
    }

    default String map(LocalDate value) {
        return value != null ? value.toString() : null;
    }

    default LocalTime mapTime(String value) {
        return value != null ? LocalTime.parse(value) : null;
    }

    default String map(LocalTime value) {
        return value != null ? value.toString() : null;
    }

    default AttendanceStatus map(String value) {
        return value != null ? AttendanceStatus.valueOf(value) : null;
    }

    default String map(AttendanceStatus value) {
        return value != null ? value.name() : null;
    }
}