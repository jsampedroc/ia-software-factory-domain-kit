package com.application.school.infrastructure.api.mapper;

import com.application.school.application.student.dto.CreateStudentCommand;
import com.application.school.application.student.dto.StudentResponse;
import com.application.school.infrastructure.api.student.dto.CreateStudentRequest;
import com.application.school.infrastructure.api.student.dto.StudentApiResponse;
import com.application.school.infrastructure.api.student.dto.GuardianRequest;
import com.application.school.application.student.dto.GuardianCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentApiMapper {

    StudentApiMapper INSTANCE = Mappers.getMapper(StudentApiMapper.class);

    @Mapping(target = "legalId", source = "legalId")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    @Mapping(target = "enrollmentDate", source = "enrollmentDate")
    @Mapping(target = "guardians", source = "guardians")
    CreateStudentCommand toCommand(CreateStudentRequest request);

    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "relationship", source = "relationship")
    GuardianCommand toGuardianCommand(GuardianRequest request);

    List<GuardianCommand> toGuardianCommandList(List<GuardianRequest> guardians);

    @Mapping(target = "studentId", source = "studentId.value")
    @Mapping(target = "legalId", source = "legalId")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    @Mapping(target = "enrollmentDate", source = "enrollmentDate")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "guardians", source = "guardians")
    StudentApiResponse toResponse(StudentResponse studentResponse);
}