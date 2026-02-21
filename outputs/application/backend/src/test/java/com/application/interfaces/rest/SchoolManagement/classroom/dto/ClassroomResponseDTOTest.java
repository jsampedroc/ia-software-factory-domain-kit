package com.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class ClassroomResponseDTOTest {

    @Test
    @DisplayName("ClassroomResponseDTO builder should create object with correct values")
    void builderShouldCreateObjectWithCorrectValues() {
        ClassroomResponseDTO dto = ClassroomResponseDTO.builder()
                .id("classroom-123")
                .gradeLevel("1st Grade")
                .section("A")
                .academicYear("2024-2025")
                .capacity(25)
                .tutorTeacherId("teacher-456")
                .schoolId("school-789")
                .active(true)
                .build();

        assertEquals("classroom-123", dto.getId());
        assertEquals("1st Grade", dto.getGradeLevel());
        assertEquals("A", dto.getSection());
        assertEquals("2024-2025", dto.getAcademicYear());
        assertEquals(25, dto.getCapacity());
        assertEquals("teacher-456", dto.getTutorTeacherId());
        assertEquals("school-789", dto.getSchoolId());
        assertTrue(dto.isActive());
    }

    @Test
    @DisplayName("ClassroomResponseDTO no-args constructor should create object")
    void noArgsConstructorShouldCreateObject() {
        ClassroomResponseDTO dto = new ClassroomResponseDTO();
        assertNotNull(dto);
    }

    @Test
    @DisplayName("ClassroomResponseDTO setters should update field values")
    void settersShouldUpdateFieldValues() {
        ClassroomResponseDTO dto = new ClassroomResponseDTO();
        dto.setId("test-id");
        dto.setGradeLevel("2nd Grade");
        dto.setSection("B");
        dto.setAcademicYear("2023-2024");
        dto.setCapacity(30);
        dto.setTutorTeacherId("tutor-111");
        dto.setSchoolId("school-222");
        dto.setActive(false);

        assertEquals("test-id", dto.getId());
        assertEquals("2nd Grade", dto.getGradeLevel());
        assertEquals("B", dto.getSection());
        assertEquals("2023-2024", dto.getAcademicYear());
        assertEquals(30, dto.getCapacity());
        assertEquals("tutor-111", dto.getTutorTeacherId());
        assertEquals("school-222", dto.getSchoolId());
        assertFalse(dto.isActive());
    }

    @Test
    @DisplayName("ClassroomResponseDTO equals and hashCode should work based on id")
    void equalsAndHashCodeShouldWorkBasedOnId() {
        ClassroomResponseDTO dto1 = ClassroomResponseDTO.builder().id("id-1").build();
        ClassroomResponseDTO dto2 = ClassroomResponseDTO.builder().id("id-1").build();
        ClassroomResponseDTO dto3 = ClassroomResponseDTO.builder().id("id-2").build();

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    @DisplayName("ClassroomResponseDTO toString should contain class name and fields")
    void toStringShouldContainClassNameAndFields() {
        ClassroomResponseDTO dto = ClassroomResponseDTO.builder()
                .id("id-test")
                .gradeLevel("Grade")
                .build();

        String toStringResult = dto.toString();
        assertTrue(toStringResult.contains("ClassroomResponseDTO"));
        assertTrue(toStringResult.contains("id=id-test"));
        assertTrue(toStringResult.contains("gradeLevel=Grade"));
    }
}