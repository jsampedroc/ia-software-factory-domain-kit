package com.application.school.infrastructure.web.rest;

import com.application.school.application.dtos.StudentDTO;
import com.application.school.application.dtos.GuardianDTO;
import com.application.school.application.student.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@Slf4j
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody StudentDTO studentDTO) {
        log.info("REST request to create Student: {}", studentDTO);
        StudentDTO result = studentService.createStudent(studentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<StudentDTO> updateStudent(
            @PathVariable String studentId,
            @Valid @RequestBody StudentDTO studentDTO) {
        log.info("REST request to update Student with id {}: {}", studentId, studentDTO);
        StudentDTO result = studentService.updateStudent(studentId, studentDTO);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{studentId}/status")
    public ResponseEntity<StudentDTO> updateStudentStatus(
            @PathVariable String studentId,
            @RequestParam String status) {
        log.info("REST request to update status of Student with id {} to {}", studentId, status);
        StudentDTO result = studentService.updateStudentStatus(studentId, status);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable String studentId) {
        log.info("REST request to get Student with id: {}", studentId);
        StudentDTO result = studentService.getStudentById(studentId);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents(
            @RequestParam(required = false) String status) {
        log.info("REST request to get all Students with status filter: {}", status);
        List<StudentDTO> result = studentService.getAllStudents(status);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{studentId}/guardians")
    public ResponseEntity<GuardianDTO> addGuardianToStudent(
            @PathVariable String studentId,
            @Valid @RequestBody GuardianDTO guardianDTO) {
        log.info("REST request to add Guardian to Student with id {}: {}", studentId, guardianDTO);
        GuardianDTO result = studentService.addGuardianToStudent(studentId, guardianDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{studentId}/guardians/{guardianId}")
    public ResponseEntity<GuardianDTO> updateGuardian(
            @PathVariable String studentId,
            @PathVariable String guardianId,
            @Valid @RequestBody GuardianDTO guardianDTO) {
        log.info("REST request to update Guardian {} for Student with id {}: {}", guardianId, studentId, guardianDTO);
        GuardianDTO result = studentService.updateGuardian(studentId, guardianId, guardianDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{studentId}/guardians/{guardianId}")
    public ResponseEntity<Void> removeGuardianFromStudent(
            @PathVariable String studentId,
            @PathVariable String guardianId) {
        log.info("REST request to remove Guardian {} from Student with id {}", guardianId, studentId);
        studentService.removeGuardianFromStudent(studentId, guardianId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{studentId}/guardians")
    public ResponseEntity<List<GuardianDTO>> getGuardiansByStudentId(@PathVariable String studentId) {
        log.info("REST request to get all Guardians for Student with id: {}", studentId);
        List<GuardianDTO> result = studentService.getGuardiansByStudentId(studentId);
        return ResponseEntity.ok(result);
    }
}