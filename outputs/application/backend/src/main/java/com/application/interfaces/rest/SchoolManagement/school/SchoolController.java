package com.application.interfaces.rest.SchoolManagement.school;

import com.application.application.SchoolManagement.school.application.create.CreateSchoolCommand;
import com.application.application.SchoolManagement.school.application.create.CreateSchoolCommandHandler;
import com.application.application.SchoolManagement.school.application.find.FindSchoolQuery;
import com.application.application.SchoolManagement.school.application.find.FindSchoolQueryHandler;
import com.application.interfaces.rest.SchoolManagement.school.dto.CreateSchoolRequestDTO;
import com.application.interfaces.rest.SchoolManagement.school.dto.SchoolResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/schools")
@RequiredArgsConstructor
public class SchoolController {

    private final CreateSchoolCommandHandler createSchoolCommandHandler;
    private final FindSchoolQueryHandler findSchoolQueryHandler;

    @PostMapping
    public ResponseEntity<SchoolResponseDTO> createSchool(@RequestBody CreateSchoolRequestDTO request) {
        CreateSchoolCommand command = new CreateSchoolCommand(
                request.getName(),
                request.getAddress(),
                request.getPhoneNumber()
        );
        var createdSchool = createSchoolCommandHandler.handle(command);
        SchoolResponseDTO responseDTO = SchoolResponseDTO.fromDomain(createdSchool);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchoolResponseDTO> getSchoolById(@PathVariable String id) {
        FindSchoolQuery query = new FindSchoolQuery(id);
        var school = findSchoolQueryHandler.handle(query);
        return school.map(s -> ResponseEntity.ok(SchoolResponseDTO.fromDomain(s)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<SchoolResponseDTO>> getAllSchools() {
        var schools = findSchoolQueryHandler.handleAll();
        List<SchoolResponseDTO> responseList = schools.stream()
                .map(SchoolResponseDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
    }
}