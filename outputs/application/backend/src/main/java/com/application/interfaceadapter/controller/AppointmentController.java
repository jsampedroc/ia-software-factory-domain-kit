package com.application.interfaceadapter.controller;

import com.application.application.dto.AppointmentDTO;
import com.application.application.service.AppointmentService;
import com.application.domain.valueobject.AppointmentId;
import com.application.domain.valueobject.AppointmentStatus;
import com.application.domain.valueobject.DentistId;
import com.application.domain.valueobject.PatientId;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getById(@PathVariable UUID id) {
        AppointmentId appointmentId = new AppointmentId(id);
        return appointmentService.findById(appointmentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> getAll() {
        List<AppointmentDTO> appointments = appointmentService.findAll();
        return ResponseEntity.ok(appointments);
    }

    @PostMapping
    public ResponseEntity<AppointmentDTO> create(@RequestBody AppointmentDTO appointmentDTO) {
        AppointmentDTO createdAppointment = appointmentService.create(appointmentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAppointment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDTO> update(@PathVariable UUID id, @RequestBody AppointmentDTO appointmentDTO) {
        AppointmentId appointmentId = new AppointmentId(id);
        AppointmentDTO updatedAppointment = appointmentService.update(appointmentId, appointmentDTO);
        return ResponseEntity.ok(updatedAppointment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        AppointmentId appointmentId = new AppointmentId(id);
        appointmentService.delete(appointmentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentDTO>> getByPatient(@PathVariable UUID patientId) {
        PatientId id = new PatientId(patientId);
        List<AppointmentDTO> appointments = appointmentService.findByPatient(id);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/dentist/{dentistId}")
    public ResponseEntity<List<AppointmentDTO>> getByDentist(@PathVariable UUID dentistId) {
        DentistId id = new DentistId(dentistId);
        List<AppointmentDTO> appointments = appointmentService.findByDentist(id);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AppointmentDTO>> getByStatus(@PathVariable AppointmentStatus status) {
        List<AppointmentDTO> appointments = appointmentService.findByStatus(status);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<AppointmentDTO>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<AppointmentDTO> appointments = appointmentService.findByDateRange(start, end);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<AppointmentDTO>> getByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<AppointmentDTO> appointments = appointmentService.findByDate(date);
        return ResponseEntity.ok(appointments);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentDTO> updateStatus(@PathVariable UUID id, @RequestParam AppointmentStatus status) {
        AppointmentId appointmentId = new AppointmentId(id);
        AppointmentDTO updatedAppointment = appointmentService.updateStatus(appointmentId, status);
        return ResponseEntity.ok(updatedAppointment);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<AppointmentDTO> cancel(@PathVariable UUID id, @RequestParam(required = false) String reason) {
        AppointmentId appointmentId = new AppointmentId(id);
        AppointmentDTO cancelledAppointment = appointmentService.cancel(appointmentId, reason);
        return ResponseEntity.ok(cancelledAppointment);
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<AppointmentDTO> confirm(@PathVariable UUID id) {
        AppointmentId appointmentId = new AppointmentId(id);
        AppointmentDTO confirmedAppointment = appointmentService.confirm(appointmentId);
        return ResponseEntity.ok(confirmedAppointment);
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<AppointmentDTO> start(@PathVariable UUID id) {
        AppointmentId appointmentId = new AppointmentId(id);
        AppointmentDTO startedAppointment = appointmentService.start(appointmentId);
        return ResponseEntity.ok(startedAppointment);
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<AppointmentDTO> complete(@PathVariable UUID id) {
        AppointmentId appointmentId = new AppointmentId(id);
        AppointmentDTO completedAppointment = appointmentService.complete(appointmentId);
        return ResponseEntity.ok(completedAppointment);
    }

    @GetMapping("/patient/{patientId}/pending-count")
    public ResponseEntity<Long> countPendingByPatient(@PathVariable UUID patientId) {
        PatientId id = new PatientId(patientId);
        long count = appointmentService.countPendingByPatient(id);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/dentist/{dentistId}/overlapping")
    public ResponseEntity<List<AppointmentDTO>> findOverlappingForDentist(
            @PathVariable UUID dentistId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        DentistId id = new DentistId(dentistId);
        List<AppointmentDTO> appointments = appointmentService.findOverlappingForDentist(id, start, end);
        return ResponseEntity.ok(appointments);
    }
}