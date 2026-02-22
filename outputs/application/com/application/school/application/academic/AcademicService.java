package com.application.school.application.academic;

import com.application.school.application.academic.dto.CreateGradeCommand;
import com.application.school.application.academic.dto.GradeResponse;
import com.application.school.domain.academic.model.Grade;
import com.application.school.domain.academic.model.GradeId;
import com.application.school.domain.academic.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AcademicService {

    private final GradeRepository gradeRepository;

    public GradeResponse createGrade(CreateGradeCommand command) {
        log.info("Creating grade with name: {}", command.getName());

        // Check for uniqueness (business rule: grade name must be unique)
        gradeRepository.findByName(command.getName())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Grade with name '" + command.getName() + "' already exists.");
                });

        Grade grade = Grade.builder()
                .gradeId(GradeId.generate())
                .name(command.getName())
                .level(command.getLevel())
                .build();

        Grade savedGrade = gradeRepository.save(grade);
        log.info("Grade created with ID: {}", savedGrade.getGradeId().getValue());

        return mapToResponse(savedGrade);
    }

    @Transactional(readOnly = true)
    public GradeResponse getGradeById(String gradeId) {
        log.debug("Fetching grade with ID: {}", gradeId);
        Grade grade = gradeRepository.findById(GradeId.fromString(gradeId))
                .orElseThrow(() -> new IllegalArgumentException("Grade not found with ID: " + gradeId));
        return mapToResponse(grade);
    }

    @Transactional(readOnly = true)
    public List<GradeResponse> getAllGrades() {
        log.debug("Fetching all grades");
        return gradeRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public void deleteGrade(String gradeId) {
        log.info("Deleting grade with ID: {}", gradeId);
        GradeId id = GradeId.fromString(gradeId);
        // Optional: add business rules before deletion (e.g., check if grade has associated class groups)
        gradeRepository.deleteById(id);
        log.info("Grade deleted successfully");
    }

    private GradeResponse mapToResponse(Grade grade) {
        return GradeResponse.builder()
                .gradeId(grade.getGradeId().getValue())
                .name(grade.getName())
                .level(grade.getLevel())
                .build();
    }
}