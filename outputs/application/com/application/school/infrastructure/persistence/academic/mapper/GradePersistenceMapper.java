package com.application.school.infrastructure.persistence.academic.mapper;

import com.application.school.domain.academic.model.Grade;
import com.application.school.domain.academic.model.GradeId;
import com.application.school.infrastructure.persistence.academic.entity.GradeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface GradePersistenceMapper {

    @Mapping(target = "id", source = "gradeId.value", qualifiedByName = "uuidToString")
    @Mapping(target = "classGroups", ignore = true)
    GradeEntity toEntity(Grade grade);

    @Mapping(target = "gradeId", source = "id", qualifiedByName = "stringToGradeId")
    Grade toDomain(GradeEntity gradeEntity);

    @Named("uuidToString")
    default String uuidToString(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }

    @Named("stringToGradeId")
    default GradeId stringToGradeId(String id) {
        return id != null ? new GradeId(UUID.fromString(id)) : null;
    }
}