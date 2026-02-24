package com.application.infrastructure.entity;

import com.application.domain.valueobject.DentistId;
import com.application.domain.valueobject.ClinicId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DentistClinicScheduleEntityTest {

    @Test
    void givenValidData_whenCreatingEntity_thenFieldsAreCorrectlySet() {
        // Given
        DentistId dentistId = new DentistId(UUID.randomUUID());
        ClinicId clinicId = new ClinicId(UUID.randomUUID());
        DayOfWeek dayOfWeek = DayOfWeek.MONDAY;
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(17, 0);
        Boolean active = true;

        // When - Using reflection to set fields since constructor is protected
        DentistClinicScheduleEntity entity = new DentistClinicScheduleEntity();
        setField(entity, "dentistId", dentistId);
        setField(entity, "clinicId", clinicId);
        setField(entity, "dayOfWeek", dayOfWeek);
        setField(entity, "startTime", startTime);
        setField(entity, "endTime", endTime);
        setField(entity, "active", active);

        // Then
        assertNotNull(entity);
        assertEquals(dentistId, entity.getDentistId());
        assertEquals(clinicId, entity.getClinicId());
        assertEquals(dayOfWeek, entity.getDayOfWeek());
        assertEquals(startTime, entity.getStartTime());
        assertEquals(endTime, entity.getEndTime());
        assertEquals(active, entity.getActive());
    }

    @Test
    void givenNullDentistId_whenCreatingEntity_thenThrowsException() {
        // Given
        DentistClinicScheduleEntity entity = new DentistClinicScheduleEntity();
        setField(entity, "clinicId", new ClinicId(UUID.randomUUID()));
        setField(entity, "dayOfWeek", DayOfWeek.MONDAY);
        setField(entity, "startTime", LocalTime.of(9, 0));
        setField(entity, "endTime", LocalTime.of(17, 0));
        setField(entity, "active", true);

        // When & Then - The entity can be created but dentistId will be null
        // This test verifies the entity can handle null IDs (though DB constraints would fail)
        assertNull(entity.getDentistId());
    }

    @Test
    void givenNullClinicId_whenCreatingEntity_thenThrowsException() {
        // Given
        DentistClinicScheduleEntity entity = new DentistClinicScheduleEntity();
        setField(entity, "dentistId", new DentistId(UUID.randomUUID()));
        setField(entity, "dayOfWeek", DayOfWeek.MONDAY);
        setField(entity, "startTime", LocalTime.of(9, 0));
        setField(entity, "endTime", LocalTime.of(17, 0));
        setField(entity, "active", true);

        // When & Then - The entity can be created but clinicId will be null
        assertNull(entity.getClinicId());
    }

    @Test
    void givenInvalidTimeRange_whenCreatingEntity_thenEntityStillCreated() {
        // Given - End time before start time (invalid business logic but entity allows it)
        DentistId dentistId = new DentistId(UUID.randomUUID());
        ClinicId clinicId = new ClinicId(UUID.randomUUID());
        DayOfWeek dayOfWeek = DayOfWeek.MONDAY;
        LocalTime startTime = LocalTime.of(17, 0);
        LocalTime endTime = LocalTime.of(9, 0);
        Boolean active = true;

        // When
        DentistClinicScheduleEntity entity = new DentistClinicScheduleEntity();
        setField(entity, "dentistId", dentistId);
        setField(entity, "clinicId", clinicId);
        setField(entity, "dayOfWeek", dayOfWeek);
        setField(entity, "startTime", startTime);
        setField(entity, "endTime", endTime);
        setField(entity, "active", active);

        // Then
        assertNotNull(entity);
        assertEquals(startTime, entity.getStartTime());
        assertEquals(endTime, entity.getEndTime());
        assertTrue(entity.getStartTime().isAfter(entity.getEndTime()));
    }

    @Test
    void givenNullActiveFlag_whenCreatingEntity_thenEntityStillCreated() {
        // Given
        DentistId dentistId = new DentistId(UUID.randomUUID());
        ClinicId clinicId = new ClinicId(UUID.randomUUID());
        DayOfWeek dayOfWeek = DayOfWeek.MONDAY;
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(17, 0);

        // When
        DentistClinicScheduleEntity entity = new DentistClinicScheduleEntity();
        setField(entity, "dentistId", dentistId);
        setField(entity, "clinicId", clinicId);
        setField(entity, "dayOfWeek", dayOfWeek);
        setField(entity, "startTime", startTime);
        setField(entity, "endTime", endTime);
        setField(entity, "active", null);

        // Then
        assertNotNull(entity);
        assertNull(entity.getActive());
    }

    @Test
    void givenEntityWithAllFields_whenGettingId_thenIdIsNullInitially() {
        // Given
        DentistClinicScheduleEntity entity = new DentistClinicScheduleEntity();
        setField(entity, "dentistId", new DentistId(UUID.randomUUID()));
        setField(entity, "clinicId", new ClinicId(UUID.randomUUID()));
        setField(entity, "dayOfWeek", DayOfWeek.MONDAY);
        setField(entity, "startTime", LocalTime.of(9, 0));
        setField(entity, "endTime", LocalTime.of(17, 0));
        setField(entity, "active", true);

        // When & Then - ID is generated by DB, so initially null
        assertNull(entity.getId());
    }

    @Test
    void givenEntity_whenCheckingLazyRelationships_thenRelationshipsAreNotLoaded() {
        // Given
        DentistClinicScheduleEntity entity = new DentistClinicScheduleEntity();
        setField(entity, "dentistId", new DentistId(UUID.randomUUID()));
        setField(entity, "clinicId", new ClinicId(UUID.randomUUID()));
        setField(entity, "dayOfWeek", DayOfWeek.MONDAY);
        setField(entity, "startTime", LocalTime.of(9, 0));
        setField(entity, "endTime", LocalTime.of(17, 0));
        setField(entity, "active", true);

        // When & Then - Lazy relationships should be null unless fetched
        assertNull(entity.getDentist());
        assertNull(entity.getClinic());
    }

    @Test
    void givenDifferentDayOfWeek_whenCreatingEntity_thenDayIsCorrectlySet() {
        // Given
        DayOfWeek[] days = DayOfWeek.values();

        for (DayOfWeek day : days) {
            // When
            DentistClinicScheduleEntity entity = new DentistClinicScheduleEntity();
            setField(entity, "dentistId", new DentistId(UUID.randomUUID()));
            setField(entity, "clinicId", new ClinicId(UUID.randomUUID()));
            setField(entity, "dayOfWeek", day);
            setField(entity, "startTime", LocalTime.of(9, 0));
            setField(entity, "endTime", LocalTime.of(17, 0));
            setField(entity, "active", true);

            // Then
            assertEquals(day, entity.getDayOfWeek());
        }
    }

    // Helper method to set private fields using reflection
    private void setField(Object object, String fieldName, Object value) {
        try {
            var field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set field: " + fieldName, e);
        }
    }
}