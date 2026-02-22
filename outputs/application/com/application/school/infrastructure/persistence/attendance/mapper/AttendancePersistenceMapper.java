package com.application.school.infrastructure.persistence.attendance.mapper;

import com.application.school.domain.attendance.model.AttendanceRecord;
import com.application.school.domain.attendance.model.AttendanceRecordId;
import com.application.school.domain.shared.enumeration.AttendanceStatus;
import com.application.school.infrastructure.persistence.attendance.entity.AttendanceRecordEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface AttendancePersistenceMapper {

    AttendancePersistenceMapper INSTANCE = Mappers.getMapper(AttendancePersistenceMapper.class);

    @Mapping(target = "id", source = "recordId.value")
    @Mapping(target = "studentId", source = "studentId.value")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "checkInTime", source = "checkInTime")
    @Mapping(target = "checkOutTime", source = "checkOutTime")
    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    AttendanceRecordEntity toEntity(AttendanceRecord domain);

    @Mapping(target = "recordId", source = "id", qualifiedByName = "uuidToAttendanceRecordId")
    @Mapping(target = "studentId.value", source = "studentId")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "checkInTime", source = "checkInTime")
    @Mapping(target = "checkOutTime", source = "checkOutTime")
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    AttendanceRecord toDomain(AttendanceRecordEntity entity);

    @Named("uuidToAttendanceRecordId")
    static AttendanceRecordId uuidToAttendanceRecordId(UUID id) {
        return id != null ? new AttendanceRecordId(id) : null;
    }

    @Named("statusToString")
    static String statusToString(AttendanceStatus status) {
        return status != null ? status.name() : null;
    }

    @Named("stringToStatus")
    static AttendanceStatus stringToStatus(String status) {
        return status != null ? AttendanceStatus.valueOf(status) : null;
    }
}