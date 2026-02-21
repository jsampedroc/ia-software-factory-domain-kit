package com.application;

import com.application.domain.Attendance.tracking.domain.DailyAttendance;
import com.application.domain.Attendance.tracking.valueobject.DailyAttendanceId;
import com.application.domain.SchoolManagement.valueobject.ClassroomId;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import com.application.infrastructure.persistence.Attendance.tracking.entity.DailyAttendanceEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DailyAttendanceEntityMapperTest {

    private DailyAttendanceEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new DailyAttendanceEntityMapper();
    }

    @Test
    void toDomain_ShouldMapEntityToDomain() {
        UUID id = UUID.randomUUID();
        UUID classroomId = UUID.randomUUID();
        UUID schoolId = UUID.randomUUID();
        LocalDate date = LocalDate.now();

        DailyAttendanceEntity entity = new DailyAttendanceEntity();
        entity.setId(id);
        entity.setClassroomId(classroomId);
        entity.setSchoolId(schoolId);
        entity.setDate(date);

        DailyAttendance domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(DailyAttendanceId.of(id), domain.getId());
        assertEquals(ClassroomId.of(classroomId), domain.getClassroomId());
        assertEquals(SchoolId.of(schoolId), domain.getSchoolId());
        assertEquals(date, domain.getDate());
    }

    @Test
    void toDomain_ShouldReturnNullWhenEntityIsNull() {
        DailyAttendance result = mapper.toDomain(null);
        assertNull(result);
    }

    @Test
    void toEntity_ShouldMapDomainToEntity() {
        UUID id = UUID.randomUUID();
        UUID classroomId = UUID.randomUUID();
        UUID schoolId = UUID.randomUUID();
        LocalDate date = LocalDate.now();

        DailyAttendance domain = DailyAttendance.create(
                DailyAttendanceId.of(id),
                ClassroomId.of(classroomId),
                date,
                SchoolId.of(schoolId)
        );

        DailyAttendanceEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals(classroomId, entity.getClassroomId());
        assertEquals(schoolId, entity.getSchoolId());
        assertEquals(date, entity.getDate());
    }

    @Test
    void toEntity_ShouldReturnNullWhenDomainIsNull() {
        DailyAttendanceEntity result = mapper.toEntity(null);
        assertNull(result);
    }
}