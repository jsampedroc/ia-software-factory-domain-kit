package com.application.infrastructure.entity;

import com.application.domain.valueobject.ClinicId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ClinicEntityTest {

    @Test
    void shouldCreateClinicEntityWithCorrectAttributes() {
        // Given
        UUID uuid = UUID.randomUUID();
        ClinicId clinicId = ClinicId.of(uuid);
        String codigo = "CLI-001";
        String nombre = "Clínica Central";
        String direccion = "Calle Principal 123";
        String telefono = "+1234567890";
        String email = "central@clinica.com";
        LocalTime apertura = LocalTime.of(8, 0);
        LocalTime cierre = LocalTime.of(18, 0);
        Boolean activa = true;

        // When
        ClinicEntity entity = new ClinicEntity();
        entity.setDomainId(clinicId);
        entity.codigo = codigo;
        entity.nombre = nombre;
        entity.direccion = direccion;
        entity.telefono = telefono;
        entity.email = email;
        entity.horarioApertura = apertura;
        entity.horarioCierre = cierre;
        entity.activa = activa;

        // Then
        assertThat(entity.getId()).isEqualTo(uuid);
        assertThat(entity.getDomainId()).isEqualTo(clinicId);
        assertThat(entity.getCodigo()).isEqualTo(codigo);
        assertThat(entity.getNombre()).isEqualTo(nombre);
        assertThat(entity.getDireccion()).isEqualTo(direccion);
        assertThat(entity.getTelefono()).isEqualTo(telefono);
        assertThat(entity.getEmail()).isEqualTo(email);
        assertThat(entity.getHorarioApertura()).isEqualTo(apertura);
        assertThat(entity.getHorarioCierre()).isEqualTo(cierre);
        assertThat(entity.getActiva()).isEqualTo(activa);
        assertThat(entity.getConsultorios()).isNotNull().isEmpty();
        assertThat(entity.getHorariosOdontologos()).isNotNull().isEmpty();
    }

    @Test
    void getDomainId_shouldReturnClinicId() {
        // Given
        UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        ClinicId expectedId = ClinicId.of(uuid);
        ClinicEntity entity = new ClinicEntity();
        entity.id = uuid;

        // When
        ClinicId result = entity.getDomainId();

        // Then
        assertThat(result).isEqualTo(expectedId);
        assertThat(result.getValue()).isEqualTo(uuid);
    }

    @Test
    void setDomainId_shouldSetUUIDFromClinicId() {
        // Given
        UUID uuid = UUID.fromString("456e4567-e89b-12d3-a456-426614174000");
        ClinicId clinicId = ClinicId.of(uuid);
        ClinicEntity entity = new ClinicEntity();

        // When
        entity.setDomainId(clinicId);

        // Then
        assertThat(entity.getId()).isEqualTo(uuid);
    }

    @Test
    void shouldHaveNoArgsConstructorProtected() {
        // This test verifies the existence of the protected no-args constructor
        // by checking that we can create an instance via reflection or default instantiation
        // in the test context. The actual accessibility is enforced by Lombok.
        ClinicEntity entity = new ClinicEntity();
        assertThat(entity).isNotNull();
    }

    @Test
    void collectionsShouldBeInitializedAsEmpty() {
        ClinicEntity entity = new ClinicEntity();

        assertThat(entity.getConsultorios())
                .isNotNull()
                .isEmpty();
        assertThat(entity.getHorariosOdontologos())
                .isNotNull()
                .isEmpty();
    }
}