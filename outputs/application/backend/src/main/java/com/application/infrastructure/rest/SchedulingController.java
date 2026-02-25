package com.application.infrastructure.rest;

import com.application.application.service.SchedulingService;
import com.application.domain.model.Appointment;
import com.application.domain.valueobject.AppointmentId;
import com.application.domain.valueobject.PatientId;
import com.application.domain.valueobject.UserId;
import com.application.domain.enums.AppointmentStatus;
import com.application.domain.enums.AppointmentType;
import com.application.infrastructure.rest.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/scheduling")
@RequiredArgsConstructor
public class SchedulingController {

    private final SchedulingService schedulingService;

    @PostMapping("/appointments")
    public ResponseEntity<AppointmentResponse> createAppointment(@RequestBody CreateAppointmentRequest request) {
        Appointment appointment = schedulingService.createAppointment(
                PatientId.of(request.patientId()),
                UserId.of(request.dentistId()),
                request.scheduledTime(),
                request.duration(),
                request.type(),
                request.reason()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toAppointmentResponse(appointment));
    }

    @GetMapping("/appointments/{id}")
    public ResponseEntity<AppointmentResponse> getAppointment(@PathVariable UUID id) {
        Appointment appointment = schedulingService.getAppointment(AppointmentId.of(id));
        return ResponseEntity.ok(toAppointmentResponse(appointment));
    }

    @PutMapping("/appointments/{id}/status")
    public ResponseEntity<AppointmentResponse> updateAppointmentStatus(
            @PathVariable UUID id,
            @RequestBody UpdateAppointmentStatusRequest request) {
        Appointment appointment = schedulingService.updateAppointmentStatus(
                AppointmentId.of(id),
                request.status(),
                request.notes()
        );
        return ResponseEntity.ok(toAppointmentResponse(appointment));
    }

    @PutMapping("/appointments/{id}/reschedule")
    public ResponseEntity<AppointmentResponse> rescheduleAppointment(
            @PathVariable UUID id,
            @RequestBody RescheduleAppointmentRequest request) {
        Appointment appointment = schedulingService.rescheduleAppointment(
                AppointmentId.of(id),
                request.newScheduledTime(),
                request.newDuration(),
                request.reason()
        );
        return ResponseEntity.ok(toAppointmentResponse(appointment));
    }

    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<Void> cancelAppointment(
            @PathVariable UUID id,
            @RequestParam(required = false) String reason) {
        schedulingService.cancelAppointment(AppointmentId.of(id), reason);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/appointments/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByPatient(
            @PathVariable UUID patientId,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        List<Appointment> appointments = schedulingService.getAppointmentsByPatient(
                PatientId.of(patientId),
                status,
                fromDate,
                toDate
        );
        List<AppointmentResponse> response = appointments.stream()
                .map(this::toAppointmentResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/appointments/dentist/{dentistId}")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByDentist(
            @PathVariable UUID dentistId,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Appointment> appointments = schedulingService.getAppointmentsByDentist(
                UserId.of(dentistId),
                status,
                date
        );
        List<AppointmentResponse> response = appointments.stream()
                .map(this::toAppointmentResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/availability/dentist/{dentistId}")
    public ResponseEntity<DentistAvailabilityResponse> getDentistAvailability(
            @PathVariable UUID dentistId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        DentistAvailabilityResponse availability = schedulingService.getDentistAvailability(
                UserId.of(dentistId),
                startDate,
                endDate
        );
        return ResponseEntity.ok(availability);
    }

    @PostMapping("/availability/dentist/{dentistId}/time-off")
    public ResponseEntity<Void> addDentistTimeOff(
            @PathVariable UUID dentistId,
            @RequestBody AddTimeOffRequest request) {
        schedulingService.addDentistTimeOff(
                UserId.of(dentistId),
                request.startDate(),
                request.endDate(),
                request.reason()
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/appointments/conflicts")
    public ResponseEntity<List<AppointmentConflictResponse>> checkSchedulingConflicts(
            @RequestParam UUID dentistId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<AppointmentConflictResponse> conflicts = schedulingService.checkSchedulingConflicts(
                UserId.of(dentistId),
                startTime,
                endTime
        );
        return ResponseEntity.ok(conflicts);
    }

    private AppointmentResponse toAppointmentResponse(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId().getValue(),
                appointment.getPatientId().getValue(),
                appointment.getDentistId().getValue(),
                appointment.getScheduledTime(),
                appointment.getDuration(),
                appointment.getType(),
                appointment.getStatus(),
                appointment.getReason(),
                appointment.getCreatedAt()
        );
    }
}