package com.application.infrastructure.persistence.SchoolManagement.school.entity;

import com.application.domain.SchoolManagement.school.domain.Classroom;
import com.application.domain.SchoolManagement.valueobject.ClassroomId;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "classrooms")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomEntity {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "grade_level", nullable = false)
    private String gradeLevel;

    @Column(name = "section", nullable = false)
    private String section;

    @Column(name = "academic_year", nullable = false)
    private String academicYear;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "tutor_teacher_id")
    private UUID tutorTeacherId;

    @Column(name = "school_id", nullable = false)
    private UUID schoolId;

    @Column(name = "active", nullable = false)
    private Boolean active;

    public static ClassroomEntity fromDomain(Classroom classroom) {
        return ClassroomEntity.builder()
                .id(classroom.getId().getValue())
                .gradeLevel(classroom.getGradeLevel())
                .section(classroom.getSection())
                .academicYear(classroom.getAcademicYear())
                .capacity(classroom.getCapacity())
                .tutorTeacherId(classroom.getTutorTeacherId() != null ? classroom.getTutorTeacherId() : null)
                .schoolId(classroom.getSchoolId().getValue())
                .active(classroom.getActive())
                .build();
    }

    public Classroom toDomain() {
        return Classroom.builder()
                .id(new ClassroomId(this.id))
                .gradeLevel(this.gradeLevel)
                .section(this.section)
                .academicYear(this.academicYear)
                .capacity(this.capacity)
                .tutorTeacherId(this.tutorTeacherId)
                .schoolId(new SchoolId(this.schoolId))
                .active(this.active)
                .build();
    }
}