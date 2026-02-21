package com.application.infrastructure.persistence.Attendance.tracking.mapper;

import com.application.domain.Attendance.tracking.domain.DailyAttendance;
import com.application.domain.Attendance.valueobject.DailyAttendanceId;
import com.application.domain.SchoolManagement.valueobject.ClassroomId;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import com.application.infrastructure.persistence.Attendance.tracking.entity.DailyAttendanceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DailyAttendanceEntityMapper {

    public DailyAttendanceEntity toEntity(DailyAttendance domain) {
        if (domain == null) {
            return null;
        }

        DailyAttendanceEntity entity = new DailyAttendanceEntity();
        entity.setId(domain.getId().getValue());
        entity.setClassroomId(domain.getClassroomId().getValue());
        entity.setDate(domain.getDate());
        entity.setSchoolId(domain.getSchoolId().getValue());

        return entity;
    }

    public DailyAttendance toDomain(DailyAttendanceEntity entity) {
        if (entity == null) {
            return null;
        }

        return DailyAttendance.builder()
                .id(new DailyAttendanceId(entity.getId()))
                .classroomId(new ClassroomId(entity.getClassroomId()))
                .date(entity.getDate())
                .schoolId(new SchoolId(entity.getSchoolId()))
                .build();
    }
}