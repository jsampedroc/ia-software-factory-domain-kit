package com.application.school.infrastructure.api.academic;

import com.application.school.application.academic.AcademicService;
import com.application.school.application.academic.dto.CreateGradeCommand;
import com.application.school.application.academic.dto.GradeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/academic")
@RequiredArgsConstructor
public class AcademicController {

    private final AcademicService academicService;

    @PostMapping("/grades")
    public ResponseEntity<GradeResponse> createGrade(@RequestBody CreateGradeCommand command) {
        GradeResponse createdGrade = academicService.createGrade(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGrade);
    }

    @GetMapping("/grades/{gradeId}")
    public ResponseEntity<GradeResponse> getGradeById(@PathVariable String gradeId) {
        GradeResponse grade = academicService.getGradeById(gradeId);
        return ResponseEntity.ok(grade);
    }

    @GetMapping("/grades")
    public ResponseEntity<List<GradeResponse>> getAllGrades() {
        List<GradeResponse> grades = academicService.getAllGrades();
        return ResponseEntity.ok(grades);
    }

    @PutMapping("/grades/{gradeId}")
    public ResponseEntity<GradeResponse> updateGrade(
            @PathVariable String gradeId,
            @RequestBody CreateGradeCommand command) {
        GradeResponse updatedGrade = academicService.updateGrade(gradeId, command);
        return ResponseEntity.ok(updatedGrade);
    }

    @DeleteMapping("/grades/{gradeId}")
    public ResponseEntity<Void> deleteGrade(@PathVariable String gradeId) {
        academicService.deleteGrade(gradeId);
        return ResponseEntity.noContent().build();
    }
}