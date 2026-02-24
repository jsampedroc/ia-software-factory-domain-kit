package com.application.application.service;

import com.application.domain.model.ConsultingRoom;
import com.application.domain.valueobject.ConsultingRoomId;
import com.application.domain.valueobject.ClinicId;
import com.application.domain.port.ConsultingRoomRepositoryPort;
import com.application.application.dto.ConsultingRoomDTO;
import com.application.domain.exception.DomainException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ConsultingRoomService {

    private final ConsultingRoomRepositoryPort consultingRoomRepository;

    public ConsultingRoomService(ConsultingRoomRepositoryPort consultingRoomRepository) {
        this.consultingRoomRepository = consultingRoomRepository;
    }

    @Transactional
    public ConsultingRoomDTO create(ConsultingRoomDTO consultingRoomDTO) {
        validateConsultingRoomDTO(consultingRoomDTO);

        ConsultingRoom consultingRoom = mapToDomain(consultingRoomDTO);
        ConsultingRoom savedConsultingRoom = consultingRoomRepository.save(consultingRoom);
        return mapToDTO(savedConsultingRoom);
    }

    @Transactional
    public ConsultingRoomDTO update(ConsultingRoomId id, ConsultingRoomDTO consultingRoomDTO) {
        validateConsultingRoomDTO(consultingRoomDTO);
        ConsultingRoom existingConsultingRoom = consultingRoomRepository.findById(id)
                .orElseThrow(() -> new DomainException("ConsultingRoom not found with id: " + id.value()));

        ConsultingRoom updatedConsultingRoom = mapToDomain(consultingRoomDTO);
        updatedConsultingRoom.setId(id); // Preserve the original ID

        ConsultingRoom savedConsultingRoom = consultingRoomRepository.save(updatedConsultingRoom);
        return mapToDTO(savedConsultingRoom);
    }

    @Transactional
    public void delete(ConsultingRoomId id) {
        if (!consultingRoomRepository.existsById(id)) {
            throw new DomainException("ConsultingRoom not found with id: " + id.value());
        }
        consultingRoomRepository.deleteById(id);
    }

    public ConsultingRoomDTO findById(ConsultingRoomId id) {
        ConsultingRoom consultingRoom = consultingRoomRepository.findById(id)
                .orElseThrow(() -> new DomainException("ConsultingRoom not found with id: " + id.value()));
        return mapToDTO(consultingRoom);
    }

    public List<ConsultingRoomDTO> findAll() {
        return consultingRoomRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ConsultingRoomDTO> findByClinicId(ClinicId clinicId) {
        return consultingRoomRepository.findByClinicId(clinicId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ConsultingRoomDTO> findAvailableByClinicId(ClinicId clinicId) {
        return consultingRoomRepository.findAvailableByClinicId(clinicId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ConsultingRoomDTO markAsAvailable(ConsultingRoomId id) {
        ConsultingRoom consultingRoom = consultingRoomRepository.findById(id)
                .orElseThrow(() -> new DomainException("ConsultingRoom not found with id: " + id.value()));
        consultingRoom.markAsAvailable();
        ConsultingRoom savedConsultingRoom = consultingRoomRepository.save(consultingRoom);
        return mapToDTO(savedConsultingRoom);
    }

    @Transactional
    public ConsultingRoomDTO markAsUnavailable(ConsultingRoomId id) {
        ConsultingRoom consultingRoom = consultingRoomRepository.findById(id)
                .orElseThrow(() -> new DomainException("ConsultingRoom not found with id: " + id.value()));
        consultingRoom.markAsUnavailable();
        ConsultingRoom savedConsultingRoom = consultingRoomRepository.save(consultingRoom);
        return mapToDTO(savedConsultingRoom);
    }

    private void validateConsultingRoomDTO(ConsultingRoomDTO dto) {
        if (dto == null) {
            throw new DomainException("ConsultingRoomDTO cannot be null");
        }
        if (dto.numero() == null || dto.numero().isBlank()) {
            throw new DomainException("ConsultingRoom number is required");
        }
        if (dto.nombre() == null || dto.nombre().isBlank()) {
            throw new DomainException("ConsultingRoom name is required");
        }
        if (dto.clinicId() == null) {
            throw new DomainException("Clinic ID is required");
        }
    }

    private ConsultingRoom mapToDomain(ConsultingRoomDTO dto) {
        return new ConsultingRoom(
                dto.id() != null ? new ConsultingRoomId(dto.id()) : null,
                new ClinicId(dto.clinicId()),
                dto.numero(),
                dto.nombre(),
                dto.equipamiento(),
                dto.disponible()
        );
    }

    private ConsultingRoomDTO mapToDTO(ConsultingRoom consultingRoom) {
        return new ConsultingRoomDTO(
                consultingRoom.getId() != null ? consultingRoom.getId().value() : null,
                consultingRoom.getClinicId().value(),
                consultingRoom.getNumero(),
                consultingRoom.getNombre(),
                consultingRoom.getEquipamiento(),
                consultingRoom.isDisponible()
        );
    }
}