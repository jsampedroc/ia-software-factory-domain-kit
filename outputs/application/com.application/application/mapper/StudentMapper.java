package com.application.application.mapper;

import com.application.domain.model.studentmanagement.Student;
import com.application.domain.model.studentmanagement.StudentId;
import com.application.application.dto.StudentDTO;
import com.application.infrastructure.persistence.jpa.StudentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    @Mapping(source = "id.value", target = "studentId")
    @Mapping(source = "name.firstName", target = "firstName")
    @Mapping(source = "name.lastName", target = "lastName")
    @Mapping(source = "status", target = "status")
    StudentDTO toDTO(Student student);

    @Mapping(source = "studentId", target = "id.value")
    @Mapping(source = "firstName", target = "name.firstName")
    @Mapping(source = "lastName", target = "name.lastName")
    @Mapping(source = "status", target = "status")
    Student toDomain(StudentDTO studentDTO);

    @Mapping(source = "id", target = "id.value")
    @Mapping(source = "firstName", target = "name.firstName")
    @Mapping(source = "lastName", target = "name.lastName")
    @Mapping(source = "status", target = "status")
    Student toDomain(StudentEntity studentEntity);

    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "name.firstName", target = "firstName")
    @Mapping(source = "name.lastName", target = "lastName")
    @Mapping(source = "status", target = "status")
    StudentEntity toEntity(Student student);

    @Named("mapStudentId")
    default String mapStudentId(StudentId studentId) {
        return studentId != null ? studentId.getValue() : null;
    }

    @Named("mapToStudentId")
    default StudentId mapToStudentId(String value) {
        return value != null ? new StudentId(value) : null;
    }
}