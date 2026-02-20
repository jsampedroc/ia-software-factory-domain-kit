package com.application.school.application.academic;

import com.application.school.domain.academic.model.Grade;
import com.application.school.domain.academic.model.GradeId;
import com.application.school.domain.academic.model.Section;
import com.application.school.domain.academic.model.SectionId;
import com.application.school.domain.academic.repository.GradeRepository;
import com.application.school.application.dtos.GradeDTO;
import com.application.school.application.dtos.SectionDTO;
import com.application.school.application.mappers.AcademicMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AcademicService {

    private final GradeRepository gradeRepository;
    private final AcademicMapper academicMapper;

    public GradeDTO createGrade(GradeDTO gradeDTO) {
        log.info("Creating grade with name: {}", gradeDTO.getName());
        Grade grade = academicMapper.toDomain(gradeDTO);
        Grade savedGrade = gradeRepository.save(grade);
        return academicMapper.toDTO(savedGrade);
    }

    public Optional<GradeDTO> getGradeById(String gradeId) {
        log.debug("Fetching grade with id: {}", gradeId);
        return gradeRepository.findById(GradeId.of(gradeId))
                .map(academicMapper::toDTO);
    }

    public List<GradeDTO> getAllGrades() {
        log.debug("Fetching all grades");
        return gradeRepository.findAll().stream()
                .map(academicMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<GradeDTO> updateGrade(String gradeId, GradeDTO gradeDTO) {
        log.info("Updating grade with id: {}", gradeId);
        GradeId id = GradeId.of(gradeId);
        return gradeRepository.findById(id)
                .map(existingGrade -> {
                    Grade updatedGrade = academicMapper.toDomain(gradeDTO);
                    updatedGrade = updatedGrade.withId(id);
                    Grade savedGrade = gradeRepository.save(updatedGrade);
                    return academicMapper.toDTO(savedGrade);
                });
    }

    public boolean deleteGrade(String gradeId) {
        log.info("Deleting grade with id: {}", gradeId);
        GradeId id = GradeId.of(gradeId);
        if (gradeRepository.existsById(id)) {
            gradeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public SectionDTO addSectionToGrade(String gradeId, SectionDTO sectionDTO) {
        log.info("Adding section to grade id: {}", gradeId);
        GradeId id = GradeId.of(gradeId);
        return gradeRepository.findById(id)
                .map(grade -> {
                    Section section = academicMapper.toDomain(sectionDTO);
                    grade.addSection(section);
                    Grade updatedGrade = gradeRepository.save(grade);
                    Section newSection = updatedGrade.getSections().stream()
                            .filter(s -> s.getName().equals(sectionDTO.getName()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException("Section not found after add"));
                    return academicMapper.toDTO(newSection);
                })
                .orElseThrow(() -> new IllegalArgumentException("Grade not found with id: " + gradeId));
    }

    public Optional<SectionDTO> getSectionById(String gradeId, String sectionId) {
        log.debug("Fetching section id: {} for grade id: {}", sectionId, gradeId);
        GradeId gId = GradeId.of(gradeId);
        SectionId sId = SectionId.of(sectionId);
        return gradeRepository.findById(gId)
                .flatMap(grade -> grade.getSectionById(sId))
                .map(academicMapper::toDTO);
    }

    public List<SectionDTO> getAllSectionsByGrade(String gradeId) {
        log.debug("Fetching all sections for grade id: {}", gradeId);
        GradeId id = GradeId.of(gradeId);
        return gradeRepository.findById(id)
                .map(grade -> grade.getSections().stream()
                        .map(academicMapper::toDTO)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new IllegalArgumentException("Grade not found with id: " + gradeId));
    }

    public Optional<SectionDTO> updateSection(String gradeId, String sectionId, SectionDTO sectionDTO) {
        log.info("Updating section id: {} for grade id: {}", sectionId, gradeId);
        GradeId gId = GradeId.of(gradeId);
        SectionId sId = SectionId.of(sectionId);
        return gradeRepository.findById(gId)
                .flatMap(grade -> grade.getSectionById(sId)
                        .map(existingSection -> {
                            Section updatedSection = academicMapper.toDomain(sectionDTO);
                            updatedSection = updatedSection.withId(sId);
                            grade.updateSection(updatedSection);
                            Grade savedGrade = gradeRepository.save(grade);
                            return savedGrade.getSectionById(sId)
                                    .map(academicMapper::toDTO)
                                    .orElseThrow(() -> new IllegalStateException("Section not found after update"));
                        })
                );
    }

    public boolean removeSectionFromGrade(String gradeId, String sectionId) {
        log.info("Removing section id: {} from grade id: {}", sectionId, gradeId);
        GradeId gId = GradeId.of(gradeId);
        SectionId sId = SectionId.of(sectionId);
        return gradeRepository.findById(gId)
                .map(grade -> {
                    boolean removed = grade.removeSection(sId);
                    if (removed) {
                        gradeRepository.save(grade);
                    }
                    return removed;
                })
                .orElse(false);
    }
}