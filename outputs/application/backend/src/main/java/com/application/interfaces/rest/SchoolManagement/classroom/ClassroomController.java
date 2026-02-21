package com.application.interfaces.rest.SchoolManagement.classroom;

import com.application.application.SchoolManagement.classroom.application.create.CreateClassroomCommand;
import com.application.application.SchoolManagement.classroom.application.create.CreateClassroomCommandHandler;
import com.application.application.SchoolManagement.classroom.application.find.FindClassroomQuery;
import com.application.application.SchoolManagement.classroom.application.find.FindClassroomQueryHandler;
import com.application.application.SchoolManagement.classroom.application.list.ListClassroomsQuery;
import com.application.application.SchoolManagement.classroom.application.list.ListClassroomsQueryHandler;
import com.application.application.SchoolManagement.classroom.application.update.UpdateClassroomCommand;
import com.application.application.SchoolManagement.classroom.application.update.UpdateClassroomCommandHandler;
import com.application.interfaces.rest.SchoolManagement.classroom.dto.CreateClassroomRequestDTO;
import com.application.interfaces.rest.SchoolManagement.classroom.dto.UpdateClassroomRequestDTO;
import com.application.interfaces.rest.SchoolManagement.classroom.dto.ClassroomResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classrooms")
@RequiredArgsConstructor
public class ClassroomController {

    private final CreateClassroomCommandHandler createClassroomCommandHandler;
    private final FindClassroomQueryHandler findClassroomQueryHandler;
    private final ListClassroomsQueryHandler listClassroomsQueryHandler;
    private final UpdateClassroomCommandHandler updateClassroomCommandHandler;

    @PostMapping
    public ResponseEntity<ClassroomResponseDTO> create(@RequestBody CreateClassroomRequestDTO request) {
        CreateClassroomCommand command = CreateClassroomCommand.builder()
                .schoolId(request.getSchoolId())
                .gradeLevel(request.getGradeLevel())
                .section(request.getSection())
                .academicYear(request.getAcademicYear())
                .capacity(request.getCapacity())
                .tutorTeacherId(request.getTutorTeacherId())
                .build();

        ClassroomResponseDTO createdClassroom = createClassroomCommandHandler.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdClassroom);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassroomResponseDTO> find(@PathVariable String id) {
        FindClassroomQuery query = new FindClassroomQuery(id);
        ClassroomResponseDTO classroom = findClassroomQueryHandler.handle(query);
        return ResponseEntity.ok(classroom);
    }

    @GetMapping
    public ResponseEntity<List<ClassroomResponseDTO>> list() {
        ListClassroomsQuery query = new ListClassroomsQuery();
        List<ClassroomResponseDTO> classrooms = listClassroomsQueryHandler.handle(query);
        return ResponseEntity.ok(classrooms);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassroomResponseDTO> update(@PathVariable String id, @RequestBody UpdateClassroomRequestDTO request) {
        UpdateClassroomCommand command = UpdateClassroomCommand.builder()
                .id(id)
                .schoolId(request.getSchoolId())
                .gradeLevel(request.getGradeLevel())
                .section(request.getSection())
                .academicYear(request.getAcademicYear())
                .capacity(request.getCapacity())
                .tutorTeacherId(request.getTutorTeacherId())
                .build();

        ClassroomResponseDTO updatedClassroom = updateClassroomCommandHandler.handle(command);
        return ResponseEntity.ok(updatedClassroom);
    }
}