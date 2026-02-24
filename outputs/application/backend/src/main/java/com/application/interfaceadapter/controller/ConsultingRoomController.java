package com.application.interfaceadapter.controller;

import com.application.application.dto.ConsultingRoomDTO;
import com.application.application.service.ConsultingRoomService;
import com.application.domain.valueobject.ConsultingRoomId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/consulting-rooms")
public class ConsultingRoomController {

    private final ConsultingRoomService consultingRoomService;

    public ConsultingRoomController(ConsultingRoomService consultingRoomService) {
        this.consultingRoomService = consultingRoomService;
    }

    @GetMapping
    public ResponseEntity<List<ConsultingRoomDTO>> getAllConsultingRooms() {
        List<ConsultingRoomDTO> consultingRooms = consultingRoomService.findAll();
        return ResponseEntity.ok(consultingRooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultingRoomDTO> getConsultingRoomById(@PathVariable UUID id) {
        ConsultingRoomId consultingRoomId = new ConsultingRoomId(id);
        return consultingRoomService.findById(consultingRoomId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ConsultingRoomDTO> createConsultingRoom(@RequestBody ConsultingRoomDTO consultingRoomDTO) {
        ConsultingRoomDTO createdConsultingRoom = consultingRoomService.create(consultingRoomDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdConsultingRoom);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultingRoomDTO> updateConsultingRoom(
            @PathVariable UUID id,
            @RequestBody ConsultingRoomDTO consultingRoomDTO) {
        ConsultingRoomId consultingRoomId = new ConsultingRoomId(id);
        ConsultingRoomDTO updatedConsultingRoom = consultingRoomService.update(consultingRoomId, consultingRoomDTO);
        return ResponseEntity.ok(updatedConsultingRoom);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsultingRoom(@PathVariable UUID id) {
        ConsultingRoomId consultingRoomId = new ConsultingRoomId(id);
        consultingRoomService.delete(consultingRoomId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/clinic/{clinicId}")
    public ResponseEntity<List<ConsultingRoomDTO>> getConsultingRoomsByClinic(@PathVariable UUID clinicId) {
        List<ConsultingRoomDTO> consultingRooms = consultingRoomService.findByClinicId(clinicId);
        return ResponseEntity.ok(consultingRooms);
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<ConsultingRoomDTO> updateAvailability(
            @PathVariable UUID id,
            @RequestParam boolean disponible) {
        ConsultingRoomId consultingRoomId = new ConsultingRoomId(id);
        ConsultingRoomDTO updatedConsultingRoom = consultingRoomService.updateAvailability(consultingRoomId, disponible);
        return ResponseEntity.ok(updatedConsultingRoom);
    }
}