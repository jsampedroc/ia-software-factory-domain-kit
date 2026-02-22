package com.application.school.infrastructure.api.mapper;

import com.application.school.application.attendance.dto.RegisterAttendanceCommand;
import com.application.school.application.attendance.dto.AttendanceResponse;
import com.application.school.infrastructure.api.attendance.dto.RegisterAttendanceRequest;
import com.application.school.infrastructure.api.attendance.dto.AttendanceRecordResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AttendanceApiMapper {

    AttendanceApiMapper INSTANCE = Mappers.getMapper(AttendanceApiMapper.class);

    RegisterAttendanceCommand toCommand(RegisterAttendanceRequest request);

    @Mapping(source = "recordId.value", target = "recordId")
    @Mapping(source = "studentId.value", target = "studentId")
    AttendanceRecordResponse toResponse(AttendanceResponse response);
}