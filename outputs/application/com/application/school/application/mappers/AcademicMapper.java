package com.application.school.application.mappers;

import com.application.school.application.dtos.GradeDTO;
import com.application.school.application.dtos.SectionDTO;
import com.application.school.domain.academic.model.Grade;
import com.application.school.domain.academic.model.Section;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AcademicMapper {

    @Mapping(source = "gradeId.value", target = "gradeId")
    GradeDTO toDTO(Grade grade);

    @Mapping(source = "gradeId", target = "gradeId.value")
    Grade toDomain(GradeDTO gradeDTO);

    @Mapping(source = "sectionId.value", target = "sectionId")
    @Mapping(source = "grade.gradeId.value", target = "gradeId")
    SectionDTO toDTO(Section section);

    @Mapping(source = "sectionId", target = "sectionId.value")
    @Mapping(source = "gradeId", target = "grade.gradeId.value")
    Section toDomain(SectionDTO sectionDTO);
}