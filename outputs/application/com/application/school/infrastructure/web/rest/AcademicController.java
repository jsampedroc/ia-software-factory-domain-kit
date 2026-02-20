package com.application.school.infrastructure.web.rest;

import com.application.school.application.academic.AcademicService;
import com.application.school.application.dtos.GradeDTO;
import com.application.school.application.dtos.SectionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/academic")
@RequiredArgsConstructor
public class AcademicController {

    private final AcademicService academicService;

    @GetMapping("/grades")
    public ResponseEntity<List<GradeDTO>> getAllGrades() {
        List<GradeDTO> grades = academicService.getAllGrades();
        return ResponseEntity.ok(grades);
    }

    @GetMapping("/grades/{id}")
    public ResponseEntity<GradeDTO> getGradeById(@PathVariable String id) {
        return academicService.getGradeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/grades")
    public ResponseEntity<GradeDTO> createGrade(@RequestBody GradeDTO gradeDTO) {
        GradeDTO createdGrade = academicService.createGrade(gradeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGrade);
    }

    @PutMapping("/grades/{id}")
    public ResponseEntity<GradeDTO> updateGrade(@PathVariable String id, @RequestBody GradeDTO gradeDTO) {
        return academicService.updateGrade(id, gradeDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/grades/{id}")
    public ResponseEntity<Void> deleteGrade(@PathVariable String id) {
        boolean deleted = academicService.deleteGrade(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/sections")
    public ResponseEntity<List<SectionDTO>> getAllSections() {
        List<SectionDTO> sections = academicService.getAllSections();
        return ResponseEntity.ok(sections);
    }

    @GetMapping("/sections/{id}")
    public ResponseEntity<SectionDTO> getSectionById(@PathVariable String id) {
        return academicService.getSectionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/sections")
    public ResponseEntity<SectionDTO> createSection(@RequestBody SectionDTO sectionDTO) {
        SectionDTO createdSection = academicService.createSection(sectionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSection);
    }

    @PutMapping("/sections/{id}")
    public ResponseEntity<SectionDTO> updateSection(@PathVariable String id, @RequestBody SectionDTO sectionDTO) {
        return academicService.updateSection(id, sectionDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/sections/{id}")
    public ResponseEntity<Void> deleteSection(@PathVariable String id) {
        boolean deleted = academicService.deleteSection(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/grades/{gradeId}/sections")
    public ResponseEntity<List<SectionDTO>> getSectionsByGrade(@PathVariable String gradeId) {
        List<SectionDTO> sections = academicService.getSectionsByGrade(gradeId);
        return ResponseEntity.ok(sections);
    }
}