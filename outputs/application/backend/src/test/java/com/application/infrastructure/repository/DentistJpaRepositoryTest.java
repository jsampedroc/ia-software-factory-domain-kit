package com.application.infrastructure.repository;

import com.application.infrastructure.entity.DentistEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DentistJpaRepositoryTest {

    @Mock
    private DentistJpaRepository dentistJpaRepository;

    private DentistEntity dentist1;
    private DentistEntity dentist2;
    private DentistEntity dentistInactive;
    private UUID dentistId1;
    private UUID dentistId2;
    private UUID specialtyId;
    private UUID clinicId;

    @BeforeEach
    void setUp() {
        dentistId1 = UUID.randomUUID();
        dentistId2 = UUID.randomUUID();
        specialtyId = UUID.randomUUID();
        clinicId = UUID.randomUUID();

        dentist1 = DentistEntity.create(
                dentistId1,
                "LM12345",
                "Carlos",
                "García",
                "555-0101",
                "carlos.garcia@clinica.com",
                LocalDate.of(2020, 5, 15),
                true
        );

        dentist2 = DentistEntity.create(
                dentistId2,
                "LM67890",
                "Ana",
                "López",
                "555-0102",
                "ana.lopez@clinica.com",
                LocalDate.of(2021, 8, 22),
                true
        );

        dentistInactive = DentistEntity.create(
                UUID.randomUUID(),
                "LM11111",
                "Pedro",
                "Martínez",
                "555-0103",
                "pedro.martinez@clinica.com",
                LocalDate.of(2019, 3, 10),
                false
        );
    }

    @Test
    void findByLicenciaMedica_ShouldReturnDentist_WhenLicenciaExists() {
        String licenciaMedica = "LM12345";
        when(dentistJpaRepository.findByLicenciaMedica(licenciaMedica))
                .thenReturn(Optional.of(dentist1));

        Optional<DentistEntity> result = dentistJpaRepository.findByLicenciaMedica(licenciaMedica);

        assertThat(result).isPresent();
        assertThat(result.get().getLicenciaMedica()).isEqualTo(licenciaMedica);
        assertThat(result.get().getNombre()).isEqualTo("Carlos");
    }

    @Test
    void findByLicenciaMedica_ShouldReturnEmpty_WhenLicenciaNotExists() {
        String licenciaMedica = "LM99999";
        when(dentistJpaRepository.findByLicenciaMedica(licenciaMedica))
                .thenReturn(Optional.empty());

        Optional<DentistEntity> result = dentistJpaRepository.findByLicenciaMedica(licenciaMedica);

        assertThat(result).isEmpty();
    }

    @Test
    void findByActivoTrue_ShouldReturnOnlyActiveDentists() {
        List<DentistEntity> activeDentists = Arrays.asList(dentist1, dentist2);
        when(dentistJpaRepository.findByActivoTrue()).thenReturn(activeDentists);

        List<DentistEntity> result = dentistJpaRepository.findByActivoTrue();

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(DentistEntity::isActivo);
        assertThat(result).extracting(DentistEntity::getNombre).containsExactly("Carlos", "Ana");
    }

    @Test
    void findByActivoFalse_ShouldReturnOnlyInactiveDentists() {
        List<DentistEntity> inactiveDentists = Collections.singletonList(dentistInactive);
        when(dentistJpaRepository.findByActivoFalse()).thenReturn(inactiveDentists);

        List<DentistEntity> result = dentistJpaRepository.findByActivoFalse();

        assertThat(result).hasSize(1);
        assertThat(result).allMatch(dentist -> !dentist.isActivo());
        assertThat(result.get(0).getNombre()).isEqualTo("Pedro");
    }

    @Test
    void findByFechaContratacionAfter_ShouldReturnDentistsHiredAfterDate() {
        LocalDate cutoffDate = LocalDate.of(2021, 1, 1);
        List<DentistEntity> dentistsAfter = Collections.singletonList(dentist2);
        when(dentistJpaRepository.findByFechaContratacionAfter(cutoffDate)).thenReturn(dentistsAfter);

        List<DentistEntity> result = dentistJpaRepository.findByFechaContratacionAfter(cutoffDate);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFechaContratacion()).isAfter(cutoffDate);
        assertThat(result.get(0).getNombre()).isEqualTo("Ana");
    }

    @Test
    void findByFechaContratacionBefore_ShouldReturnDentistsHiredBeforeDate() {
        LocalDate cutoffDate = LocalDate.of(2021, 1, 1);
        List<DentistEntity> dentistsBefore = Arrays.asList(dentist1, dentistInactive);
        when(dentistJpaRepository.findByFechaContratacionBefore(cutoffDate)).thenReturn(dentistsBefore);

        List<DentistEntity> result = dentistJpaRepository.findByFechaContratacionBefore(cutoffDate);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(dentist -> dentist.getFechaContratacion().isBefore(cutoffDate));
        assertThat(result).extracting(DentistEntity::getNombre).containsExactly("Carlos", "Pedro");
    }

    @Test
    void findByEmail_ShouldReturnDentist_WhenEmailExists() {
        String email = "carlos.garcia@clinica.com";
        when(dentistJpaRepository.findByEmail(email)).thenReturn(Optional.of(dentist1));

        Optional<DentistEntity> result = dentistJpaRepository.findByEmail(email);

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(email);
        assertThat(result.get().getNombre()).isEqualTo("Carlos");
    }

    @Test
    void findByEmail_ShouldReturnEmpty_WhenEmailNotExists() {
        String email = "noexiste@clinica.com";
        when(dentistJpaRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<DentistEntity> result = dentistJpaRepository.findByEmail(email);

        assertThat(result).isEmpty();
    }

    @Test
    void findByTelefono_ShouldReturnDentist_WhenTelefonoExists() {
        String telefono = "555-0101";
        when(dentistJpaRepository.findByTelefono(telefono)).thenReturn(Optional.of(dentist1));

        Optional<DentistEntity> result = dentistJpaRepository.findByTelefono(telefono);

        assertThat(result).isPresent();
        assertThat(result.get().getTelefono()).isEqualTo(telefono);
        assertThat(result.get().getNombre()).isEqualTo("Carlos");
    }

    @Test
    void findByTelefono_ShouldReturnEmpty_WhenTelefonoNotExists() {
        String telefono = "555-9999";
        when(dentistJpaRepository.findByTelefono(telefono)).thenReturn(Optional.empty());

        Optional<DentistEntity> result = dentistJpaRepository.findByTelefono(telefono);

        assertThat(result).isEmpty();
    }

    @Test
    void findByNombreAndApellido_ShouldReturnDentistsWithMatchingName() {
        String nombre = "Carlos";
        String apellido = "García";
        List<DentistEntity> matchingDentists = Collections.singletonList(dentist1);
        when(dentistJpaRepository.findByNombreAndApellido(nombre, apellido)).thenReturn(matchingDentists);

        List<DentistEntity> result = dentistJpaRepository.findByNombreAndApellido(nombre, apellido);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombre()).isEqualTo(nombre);
        assertThat(result.get(0).getApellido()).isEqualTo(apellido);
    }

    @Test
    void findBySpecialtyId_ShouldReturnDentistsWithSpecialty() {
        List<DentistEntity> dentistsWithSpecialty = Collections.singletonList(dentist1);
        when(dentistJpaRepository.findBySpecialtyId(specialtyId)).thenReturn(dentistsWithSpecialty);

        List<DentistEntity> result = dentistJpaRepository.findBySpecialtyId(specialtyId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(dentistId1);
    }

    @Test
    void findActiveByClinicId_ShouldReturnActiveDentistsInClinic() {
        List<DentistEntity> activeDentistsInClinic = Arrays.asList(dentist1, dentist2);
        when(dentistJpaRepository.findActiveByClinicId(clinicId)).thenReturn(activeDentistsInClinic);

        List<DentistEntity> result = dentistJpaRepository.findActiveByClinicId(clinicId);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(DentistEntity::isActivo);
        assertThat(result).extracting(DentistEntity::getNombre).containsExactly("Carlos", "Ana");
    }

    @Test
    void existsByLicenciaMedica_ShouldReturnTrue_WhenLicenciaExists() {
        String licenciaMedica = "LM12345";
        when(dentistJpaRepository.existsByLicenciaMedica(licenciaMedica)).thenReturn(true);

        boolean result = dentistJpaRepository.existsByLicenciaMedica(licenciaMedica);

        assertThat(result).isTrue();
    }

    @Test
    void existsByLicenciaMedica_ShouldReturnFalse_WhenLicenciaNotExists() {
        String licenciaMedica = "LM99999";
        when(dentistJpaRepository.existsByLicenciaMedica(licenciaMedica)).thenReturn(false);

        boolean result = dentistJpaRepository.existsByLicenciaMedica(licenciaMedica);

        assertThat(result).isFalse();
    }

    @Test
    void existsByEmail_ShouldReturnTrue_WhenEmailExists() {
        String email = "carlos.garcia@clinica.com";
        when(dentistJpaRepository.existsByEmail(email)).thenReturn(true);

        boolean result = dentistJpaRepository.existsByEmail(email);

        assertThat(result).isTrue();
    }

    @Test
    void existsByEmail_ShouldReturnFalse_WhenEmailNotExists() {
        String email = "noexiste@clinica.com";
        when(dentistJpaRepository.existsByEmail(email)).thenReturn(false);

        boolean result = dentistJpaRepository.existsByEmail(email);

        assertThat(result).isFalse();
    }

    @Test
    void existsByTelefono_ShouldReturnTrue_WhenTelefonoExists() {
        String telefono = "555-0101";
        when(dentistJpaRepository.existsByTelefono(telefono)).thenReturn(true);

        boolean result = dentistJpaRepository.existsByTelefono(telefono);

        assertThat(result).isTrue();
    }

    @Test
    void existsByTelefono_ShouldReturnFalse_WhenTelefonoNotExists() {
        String telefono = "555-9999";
        when(dentistJpaRepository.existsByTelefono(telefono)).thenReturn(false);

        boolean result = dentistJpaRepository.existsByTelefono(telefono);

        assertThat(result).isFalse();
    }

    @Test
    void save_ShouldReturnSavedDentist() {
        DentistEntity newDentist = DentistEntity.create(
                UUID.randomUUID(),
                "LM55555",
                "Nuevo",
                "Dentista",
                "555-0104",
                "nuevo.dentista@clinica.com",
                LocalDate.now(),
                true
        );
        when(dentistJpaRepository.save(any(DentistEntity.class))).thenReturn(newDentist);

        DentistEntity result = dentistJpaRepository.save(newDentist);

        assertThat(result).isNotNull();
        assertThat(result.getLicenciaMedica()).isEqualTo("LM55555");
        assertThat(result.getNombre()).isEqualTo("Nuevo");
    }

    @Test
    void findById_ShouldReturnDentist_WhenIdExists() {
        when(dentistJpaRepository.findById(dentistId1)).thenReturn(Optional.of(dentist1));

        Optional<DentistEntity> result = dentistJpaRepository.findById(dentistId1);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(dentistId1);
        assertThat(result.get().getNombre()).isEqualTo("Carlos");
    }

    @Test
    void findById_ShouldReturnEmpty_WhenIdNotExists() {
        UUID nonExistentId = UUID.randomUUID();
        when(dentistJpaRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        Optional<DentistEntity> result = dentistJpaRepository.findById(nonExistentId);

        assertThat(result).isEmpty();
    }

    @Test
    void findAll_ShouldReturnAllDentists() {
        List<DentistEntity> allDentists = Arrays.asList(dentist1, dentist2, dentistInactive);
        when(dentistJpaRepository.findAll()).thenReturn(allDentists);

        List<DentistEntity> result = dentistJpaRepository.findAll();

        assertThat(result).hasSize(3);
        assertThat(result).extracting(DentistEntity::getNombre).containsExactly("Carlos", "Ana", "Pedro");
    }
}