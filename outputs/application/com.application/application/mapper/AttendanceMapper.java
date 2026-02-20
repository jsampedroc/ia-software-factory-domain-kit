package com.application.application.mapper;

import com.application.domain.model.attendance.AttendanceRecord;
import com.application.domain.model.attendance.AttendanceSummary;
import com.application.application.dto.AttendanceRecordDTO;
import com.application.infrastructure.persistence.jpa.AttendanceRecordEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {

    @Mapping(source = "id.value", target = "recordId")
    @Mapping(source = "student.id.value", target = "studentId")
    @Mapping(source = "student.personName.firstName", target = "studentFirstName")
    @Mapping(source = "student.personName.lastName", target = "studentLastName")
    @Mapping(source = "recordedBy.value", target = "recordedById")
    AttendanceRecordDTO toDTO(AttendanceRecord attendanceRecord);

    @Mapping(source = "recordId", target = "id.value")
    @Mapping(source = "studentId", target = "student.id.value")
    @Mapping(source = "recordedById", target = "recordedBy.value")
    AttendanceRecord toDomain(AttendanceRecordDTO attendanceRecordDTO);

    @Mapping(source = "id.value", target = "recordId")
    @Mapping(source = "student.id.value", target = "studentId")
    @Mapping(source = "recordedBy.value", target = "recordedById")
    AttendanceRecordEntity toEntity(AttendanceRecord attendanceRecord);

    @Mapping(source = "recordId", target = "id.value")
    @Mapping(source = "studentId", target = "student.id.value")
    @Mapping(source = "recordedById", target = "recordedBy.value")
    AttendanceRecord toDomain(AttendanceRecordEntity attendanceRecordEntity);

    @Mapping(source = "id.value", target = "summaryId")
    @Mapping(source = "student.id.value", target = "studentId")
    AttendanceSummary toDomainSummary(AttendanceRecordEntity entity);
}