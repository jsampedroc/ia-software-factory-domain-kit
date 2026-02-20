package com.application.infrastructure.web;

import com.application.application.dto.StudentDTO;
import com.application.domain.ports.in.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@RequestBody StudentDTO studentDTO) {
        StudentDTO createdStudent = studentService.createStudent(studentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable String id) {
        return studentService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        List<StudentDTO> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable String id, @RequestBody StudentDTO studentDTO) {
        return studentService.updateStudent(id, studentDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String id) {
        boolean deleted = studentService.deleteStudent(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<StudentDTO> updateStudentStatus(@PathVariable String id, @RequestParam String status) {
        return studentService.updateStudentStatus(id, status)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{studentId}/guardians")
    public ResponseEntity<List<StudentDTO.LegalGuardianDTO>> getStudentGuardians(@PathVariable String studentId) {
        List<StudentDTO.LegalGuardianDTO> guardians = studentService.getStudentGuardians(studentId);
        return ResponseEntity.ok(guardians);
    }

    @PostMapping("/{studentId}/guardians")
    public ResponseEntity<StudentDTO.LegalGuardianDTO> addGuardianToStudent(@PathVariable String studentId,
                                                                            @RequestBody StudentDTO.LegalGuardianDTO guardianDTO) {
        StudentDTO.LegalGuardianDTO addedGuardian = studentService.addGuardianToStudent(studentId, guardianDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedGuardian);
    }

    @GetMapping("/active")
    public ResponseEntity<List<StudentDTO>> getActiveStudents() {
        List<StudentDTO> activeStudents = studentService.getActiveStudents();
        return ResponseEntity.ok(activeStudents);
    }

    @GetMapping("/search")
    public ResponseEntity<List<StudentDTO>> searchStudents(@RequestParam(required = false) String firstName,
                                                           @RequestParam(required = false) String lastName,
                                                           @RequestParam(required = false) String nationalId) {
        List<StudentDTO> results = studentService.searchStudents(firstName, lastName, nationalId);
        return ResponseEntity.ok(results);
    }
}