package com.application.school.infrastructure.persistence.student.mapper;

import com.application.school.domain.student.model.Student;
import com.application.school.domain.student.model.StudentId;
import com.application.school.domain.student.model.Guardian;
import com.application.school.domain.student.model.GuardianId;
import com.application.school.domain.shared.valueobject.PersonalName;
import com.application.school.domain.shared.enumeration.StudentStatus;
import com.application.school.infrastructure.persistence.student.entity.StudentEntity;
import com.application.school.infrastructure.persistence.student.entity.GuardianEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface StudentPersistenceMapper {

    StudentPersistenceMapper INSTANCE = Mappers.getMapper(StudentPersistenceMapper.class);

    @Mapping(target = "id", source = "studentId.value")
    @Mapping(target = "legalId", source = "legalId")
    @Mapping(target = "firstName", source = "name.firstName")
    @Mapping(target = "lastName", source = "name.lastName")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    @Mapping(target = "enrollmentDate", source = "enrollmentDate")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "guardians", source = "guardians", qualifiedByName = "guardianSetToEntityList")
    StudentEntity toEntity(Student student);

    @Mapping(target = "studentId", source = "id", qualifiedByName = "uuidToStudentId")
    @Mapping(target = "legalId", source = "legalId")
    @Mapping(target = "name", source = ".", qualifiedByName = "entityToPersonalName")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    @Mapping(target = "enrollmentDate", source = "enrollmentDate")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "guardians", source = "guardians", qualifiedByName = "guardianEntityListToSet")
    Student toDomain(StudentEntity studentEntity);

    @Mapping(target = "id", source = "guardianId.value")
    @Mapping(target = "firstName", source = "name.firstName")
    @Mapping(target = "lastName", source = "name.lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "relationship", source = "relationship")
    @Mapping(target = "student", ignore = true)
    GuardianEntity guardianToEntity(Guardian guardian);

    @Mapping(target = "guardianId", source = "id", qualifiedByName = "uuidToGuardianId")
    @Mapping(target = "name", source = ".", qualifiedByName = "guardianEntityToPersonalName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "relationship", source = "relationship")
    Guardian guardianToDomain(GuardianEntity guardianEntity);

    @Named("uuidToStudentId")
    static StudentId uuidToStudentId(UUID id) {
        return id != null ? new StudentId(id) : null;
    }

    @Named("uuidToGuardianId")
    static GuardianId uuidToGuardianId(UUID id) {
        return id != null ? new GuardianId(id) : null;
    }

    @Named("entityToPersonalName")
    static PersonalName entityToPersonalName(StudentEntity entity) {
        if (entity == null || entity.getFirstName() == null || entity.getLastName() == null) {
            return null;
        }
        return PersonalName.builder()
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .build();
    }

    @Named("guardianEntityToPersonalName")
    static PersonalName guardianEntityToPersonalName(GuardianEntity entity) {
        if (entity == null || entity.getFirstName() == null || entity.getLastName() == null) {
            return null;
        }
        return PersonalName.builder()
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .build();
    }

    @Named("guardianSetToEntityList")
    static List<GuardianEntity> guardianSetToEntityList(Set<Guardian> guardians) {
        if (guardians == null) {
            return null;
        }
        return guardians.stream()
                .map(guardian -> {
                    GuardianEntity entity = guardianToEntity(guardian);
                    return entity;
                })
                .collect(Collectors.toList());
    }

    @Named("guardianEntityListToSet")
    static Set<Guardian> guardianEntityListToSet(List<GuardianEntity> guardianEntities) {
        if (guardianEntities == null) {
            return null;
        }
        return guardianEntities.stream()
                .map(StudentPersistenceMapper.INSTANCE::guardianToDomain)
                .collect(Collectors.toSet());
    }
}