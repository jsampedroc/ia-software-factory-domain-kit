package com.application.school.infrastructure.api.mapper;

import com.application.school.application.academic.dto.CreateGradeCommand;
import com.application.school.application.academic.dto.GradeResponse;
import com.application.school.infrastructure.api.academic.dto.CreateGradeRequest;
import com.application.school.infrastructure.api.academic.dto.GradeApiResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AcademicApiMapper {

    @Mapping(target = "gradeId", ignore = true)
    CreateGradeCommand toCommand(CreateGradeRequest request);

    GradeApiResponse toApiResponse(GradeResponse response);
}