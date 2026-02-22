package com.application.school.infrastructure.api.student;

import com.application.school.application.student.StudentService;
import com.application.school.application.student.dto.CreateStudentCommand;
import com.application.school.application.student.dto.StudentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@RequestBody CreateStudentCommand command) {
        StudentResponse createdStudent = studentService.createStudent(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable UUID studentId) {
        return studentService.getStudentById(studentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        List<StudentResponse> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable UUID studentId,
            @RequestBody CreateStudentCommand command) {
        return studentService.updateStudent(studentId, command)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable UUID studentId) {
        boolean deleted = studentService.deleteStudent(studentId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<StudentResponse>> searchStudents(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String status) {
        List<StudentResponse> results = studentService.searchStudents(firstName, lastName, status);
        return ResponseEntity.ok(results);
    }
}