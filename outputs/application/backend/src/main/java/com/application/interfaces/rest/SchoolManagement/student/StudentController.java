package com.application.interfaces.rest.SchoolManagement.student;

import com.application.application.SchoolManagement.student.application.create.CreateStudentCommand;
import com.application.application.SchoolManagement.student.application.create.CreateStudentCommandHandler;
import com.application.application.SchoolManagement.student.application.find.FindStudentQuery;
import com.application.application.SchoolManagement.student.application.find.FindStudentQueryHandler;
import com.application.interfaces.rest.SchoolManagement.student.dto.CreateStudentRequestDTO;
import com.application.interfaces.rest.SchoolManagement.student.dto.StudentResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final CreateStudentCommandHandler createStudentCommandHandler;
    private final FindStudentQueryHandler findStudentQueryHandler;

    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(@RequestBody CreateStudentRequestDTO request) {
        CreateStudentCommand command = new CreateStudentCommand(
                request.getLegalGuardianId(),
                request.getFirstName(),
                request.getLastName(),
                request.getDateOfBirth(),
                request.getIdentificationNumber(),
                request.getEnrollmentDate(),
                request.getCurrentClassroomId()
        );

        StudentResponseDTO createdStudent = createStudentCommandHandler.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudent(@PathVariable String id) {
        FindStudentQuery query = new FindStudentQuery(id);
        StudentResponseDTO student = findStudentQueryHandler.handle(query);
        return ResponseEntity.ok(student);
    }
}