package com.application.infrastructure.persistence.Attendance.tracking.entity;

import com.application.domain.Attendance.tracking.domain.DailyAttendance;
import com.application.domain.Attendance.valueobject.DailyAttendanceId;
import com.application.domain.SchoolManagement.valueobject.ClassroomId;
import com.application.domain.SchoolManagement.valueobject.SchoolId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DailyAttendanceEntityTest {

    private DailyAttendanceEntity dailyAttendanceEntity;
    private UUID testId;
    private UUID testClassroomId;
    private UUID testSchoolId;
    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testClassroomId = UUID.randomUUID();
        testSchoolId = UUID.randomUUID();
        testDate = LocalDate.now();

        dailyAttendanceEntity = new DailyAttendanceEntity();
        dailyAttendanceEntity.setId(testId);
        dailyAttendanceEntity.setClassroomId(testClassroomId);
        dailyAttendanceEntity.setSchoolId(testSchoolId);
        dailyAttendanceEntity.setDate(testDate);
    }

    @Test
    void testToDomain_ShouldReturnDailyAttendanceDomainObject() {
        DailyAttendance domainObject = dailyAttendanceEntity.toDomain();

        assertNotNull(domainObject);
        assertEquals(DailyAttendanceId.of(testId), domainObject.getId());
        assertEquals(ClassroomId.of(testClassroomId), domainObject.getClassroomId());
        assertEquals(SchoolId.of(testSchoolId), domainObject.getSchoolId());
        assertEquals(testDate, domainObject.getDate());
    }

    @Test
    void testFromDomain_ShouldCreateEntityFromDomainObject() {
        DailyAttendance domainObject = DailyAttendance.create(
                DailyAttendanceId.of(testId),
                ClassroomId.of(testClassroomId),
                SchoolId.of(testSchoolId),
                testDate
        );

        DailyAttendanceEntity newEntity = DailyAttendanceEntity.fromDomain(domainObject);

        assertNotNull(newEntity);
        assertEquals(testId, newEntity.getId());
        assertEquals(testClassroomId, newEntity.getClassroomId());
        assertEquals(testSchoolId, newEntity.getSchoolId());
        assertEquals(testDate, newEntity.getDate());
    }

    @Test
    void testGettersAndSetters() {
        UUID newId = UUID.randomUUID();
        UUID newClassroomId = UUID.randomUUID();
        UUID newSchoolId = UUID.randomUUID();
        LocalDate newDate = LocalDate.now().plusDays(1);

        dailyAttendanceEntity.setId(newId);
        dailyAttendanceEntity.setClassroomId(newClassroomId);
        dailyAttendanceEntity.setSchoolId(newSchoolId);
        dailyAttendanceEntity.setDate(newDate);

        assertEquals(newId, dailyAttendanceEntity.getId());
        assertEquals(newClassroomId, dailyAttendanceEntity.getClassroomId());
        assertEquals(newSchoolId, dailyAttendanceEntity.getSchoolId());
        assertEquals(newDate, dailyAttendanceEntity.getDate());
    }

    @Test
    void testEqualsAndHashCode() {
        DailyAttendanceEntity sameEntity = new DailyAttendanceEntity();
        sameEntity.setId(testId);
        sameEntity.setClassroomId(testClassroomId);
        sameEntity.setSchoolId(testSchoolId);
        sameEntity.setDate(testDate);

        DailyAttendanceEntity differentEntity = new DailyAttendanceEntity();
        differentEntity.setId(UUID.randomUUID());
        differentEntity.setClassroomId(testClassroomId);
        differentEntity.setSchoolId(testSchoolId);
        differentEntity.setDate(testDate);

        assertEquals(dailyAttendanceEntity, sameEntity);
        assertEquals(dailyAttendanceEntity.hashCode(), sameEntity.hashCode());
        assertNotEquals(dailyAttendanceEntity, differentEntity);
        assertNotEquals(dailyAttendanceEntity.hashCode(), differentEntity.hashCode());
    }

    @Test
    void testToString() {
        String entityString = dailyAttendanceEntity.toString();

        assertNotNull(entityString);
        assertTrue(entityString.contains(testId.toString()));
        assertTrue(entityString.contains(testClassroomId.toString()));
        assertTrue(entityString.contains(testSchoolId.toString()));
        assertTrue(entityString.contains(testDate.toString()));
    }
}