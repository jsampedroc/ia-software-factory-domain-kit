package com.application.infrastructure.web;

import com.application.application.dto.GradeLevelDTO;
import com.application.application.dto.ClassroomDTO;
import com.application.application.dto.SchoolDTO;
import com.application.domain.ports.in.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schools")
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolService schoolService;

    @GetMapping
    public ResponseEntity<List<SchoolDTO>> getAllSchools() {
        List<SchoolDTO> schools = schoolService.getAllSchools();
        return ResponseEntity.ok(schools);
    }

    @GetMapping("/{schoolId}")
    public ResponseEntity<SchoolDTO> getSchoolById(@PathVariable String schoolId) {
        return schoolService.getSchoolById(schoolId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SchoolDTO> createSchool(@RequestBody SchoolDTO schoolDTO) {
        SchoolDTO created = schoolService.createSchool(schoolDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{schoolId}")
    public ResponseEntity<SchoolDTO> updateSchool(@PathVariable String schoolId, @RequestBody SchoolDTO schoolDTO) {
        return ResponseEntity.ok(schoolService.updateSchool(schoolId, schoolDTO));
    }

    @DeleteMapping("/{schoolId}")
    public ResponseEntity<Void> deleteSchool(@PathVariable String schoolId) {
        schoolService.deleteSchool(schoolId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{schoolId}/grade-levels")
    public ResponseEntity<List<GradeLevelDTO>> getGradeLevelsBySchool(@PathVariable String schoolId) {
        List<GradeLevelDTO> gradeLevels = schoolService.getGradeLevelsBySchool(schoolId);
        return ResponseEntity.ok(gradeLevels);
    }

    @PostMapping("/{schoolId}/grade-levels")
    public ResponseEntity<GradeLevelDTO> createGradeLevel(@PathVariable String schoolId, @RequestBody GradeLevelDTO gradeLevelDTO) {
        GradeLevelDTO created = schoolService.createGradeLevel(schoolId, gradeLevelDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/grade-levels/{gradeLevelId}")
    public ResponseEntity<GradeLevelDTO> updateGradeLevel(@PathVariable String gradeLevelId, @RequestBody GradeLevelDTO gradeLevelDTO) {
        return ResponseEntity.ok(schoolService.updateGradeLevel(gradeLevelId, gradeLevelDTO));
    }

    @DeleteMapping("/grade-levels/{gradeLevelId}")
    public ResponseEntity<Void> deleteGradeLevel(@PathVariable String gradeLevelId) {
        schoolService.deleteGradeLevel(gradeLevelId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{schoolId}/classrooms")
    public ResponseEntity<List<ClassroomDTO>> getClassroomsBySchool(@PathVariable String schoolId) {
        List<ClassroomDTO> classrooms = schoolService.getClassroomsBySchool(schoolId);
        return ResponseEntity.ok(classrooms);
    }

    @GetMapping("/grade-levels/{gradeLevelId}/classrooms")
    public ResponseEntity<List<ClassroomDTO>> getClassroomsByGradeLevel(@PathVariable String gradeLevelId) {
        List<ClassroomDTO> classrooms = schoolService.getClassroomsByGradeLevel(gradeLevelId);
        return ResponseEntity.ok(classrooms);
    }

    @PostMapping("/classrooms")
    public ResponseEntity<ClassroomDTO> createClassroom(@RequestBody ClassroomDTO classroomDTO) {
        ClassroomDTO created = schoolService.createClassroom(classroomDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/classrooms/{classroomId}")
    public ResponseEntity<ClassroomDTO> updateClassroom(@PathVariable String classroomId, @RequestBody ClassroomDTO classroomDTO) {
        return ResponseEntity.ok(schoolService.updateClassroom(classroomId, classroomDTO));
    }

    @DeleteMapping("/classrooms/{classroomId}")
    public ResponseEntity<Void> deleteClassroom(@PathVariable String classroomId) {
        schoolService.deleteClassroom(classroomId);
        return ResponseEntity.noContent().build();
    }
}