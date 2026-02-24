package com.application.infrastructure.entity;

import com.application.domain.valueobject.DentistId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DentistEntityTest {

    @Mock
    private ClinicEntity mockClinic;

    @Test
    void givenValidParameters_whenCreatingDentistEntity_thenEntityIsCreatedWithCorrectValues() {
        // Given
        DentistId id = new DentistId(UUID.randomUUID());
        String licenciaMedica = "LM-12345";
        String nombre = "Juan";
        String apellido = "Pérez";
        String telefono = "+123456789";
        String email = "juan.perez@clinica.com";
        LocalDate fechaContratacion = LocalDate.of(2023, 1, 15);
        Boolean activo = true;

        // When
        DentistEntity dentist = new DentistEntity(id, licenciaMedica, nombre, apellido,
                telefono, email, fechaContratacion, activo, mockClinic);

        // Then
        assertNotNull(dentist);
        assertEquals(id, dentist.getId());
        assertEquals(licenciaMedica, dentist.getLicenciaMedica());
        assertEquals(nombre, dentist.getNombre());
        assertEquals(apellido, dentist.getApellido());
        assertEquals(telefono, dentist.getTelefono());
        assertEquals(email, dentist.getEmail());
        assertEquals(fechaContratacion, dentist.getFechaContratacion());
        assertEquals(activo, dentist.getActivo());
        assertEquals(mockClinic, dentist.getClinic());
        assertNotNull(dentist.getAppointments());
        assertTrue(dentist.getAppointments().isEmpty());
        assertNotNull(dentist.getDentistSpecialties());
        assertTrue(dentist.getDentistSpecialties().isEmpty());
        assertNotNull(dentist.getDentistClinicSchedules());
        assertTrue(dentist.getDentistClinicSchedules().isEmpty());
    }

    @Test
    void givenNoArgsConstructor_whenAccessingProtectedConstructor_thenNoExceptionThrown() {
        // This test verifies the existence of the protected no-args constructor
        // required by JPA and Lombok. We rely on reflection or the fact that
        // the class can be extended.
        assertDoesNotThrow(() -> {
            // Simulating framework access via reflection
            Class<DentistEntity> clazz = DentistEntity.class;
            var constructor = clazz.getDeclaredConstructor();
            assertNotNull(constructor);
        });
    }

    @Test
    void givenDentistEntity_whenCheckingInheritance_thenExtendsEntity() {
        DentistId id = new DentistId(UUID.randomUUID());
        DentistEntity dentist = new DentistEntity(id, "LM-111", "Ana", "Gómez",
                "+111", "ana@email.com", LocalDate.now(), true, mockClinic);

        assertTrue(dentist instanceof com.application.domain.shared.Entity);
    }

    @Test
    void givenDentistEntityWithClinic_whenClinicProvidesId_thenAssociationIsCorrect() {
        // Given
        DentistId dentistId = new DentistId(UUID.randomUUID());
        when(mockClinic.getId()).thenReturn(new com.application.domain.valueobject.ClinicId(UUID.randomUUID()));

        // When
        DentistEntity dentist = new DentistEntity(dentistId, "LM-999", "Carlos", "López",
                "555", "carlos@email.com", LocalDate.of(2022, 5, 10), false, mockClinic);

        // Then
        assertNotNull(dentist.getClinic());
        assertNotNull(dentist.getClinic().getId());
    }

    @Test
    void givenDentistEntity_whenModifyingCollections_thenCollectionsAreMutable() {
        // Given
        DentistId id = new DentistId(UUID.randomUUID());
        DentistEntity dentist = new DentistEntity(id, "LM-555", "Luis", "Ramírez",
                "123", "luis@email.com", LocalDate.now(), true, mockClinic);

        // When & Then - Appointments collection
        assertDoesNotThrow(() -> dentist.getAppointments().add(null)); // Testing mutability
        assertEquals(1, dentist.getAppointments().size());

        // When & Then - DentistSpecialties collection
        assertDoesNotThrow(() -> dentist.getDentistSpecialties().add(null));
        assertEquals(1, dentist.getDentistSpecialties().size());

        // When & Then - DentistClinicSchedules collection
        assertDoesNotThrow(() -> dentist.getDentistClinicSchedules().add(null));
        assertEquals(1, dentist.getDentistClinicSchedules().size());
    }
}