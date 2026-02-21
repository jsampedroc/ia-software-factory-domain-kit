package com.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class SchoolResponseDTOTest {

    @Nested
    @DisplayName("Constructor and Getters")
    class ConstructorAndGetters {
        @Test
        @DisplayName("Should create SchoolResponseDTO with all fields correctly set")
        void shouldCreateSchoolResponseDTOWithAllFields() {
            // Given
            UUID id = UUID.randomUUID();
            String name = "Test School";
            String address = "123 Main St";
            String phoneNumber = "555-1234";
            boolean active = true;

            // When
            SchoolResponseDTO dto = new SchoolResponseDTO(id, name, address, phoneNumber, active);

            // Then
            assertThat(dto.getId()).isEqualTo(id);
            assertThat(dto.getName()).isEqualTo(name);
            assertThat(dto.getAddress()).isEqualTo(address);
            assertThat(dto.getPhoneNumber()).isEqualTo(phoneNumber);
            assertThat(dto.isActive()).isEqualTo(active);
        }
    }

    @Nested
    @DisplayName("Equals and HashCode")
    class EqualsAndHashCode {
        @Test
        @DisplayName("Should be equal when IDs are the same")
        void shouldBeEqualWhenIdsAreSame() {
            UUID id = UUID.randomUUID();
            SchoolResponseDTO dto1 = new SchoolResponseDTO(id, "School A", "Addr A", "111", true);
            SchoolResponseDTO dto2 = new SchoolResponseDTO(id, "School B", "Addr B", "222", false);

            assertThat(dto1).isEqualTo(dto2);
            assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when IDs are different")
        void shouldNotBeEqualWhenIdsAreDifferent() {
            SchoolResponseDTO dto1 = new SchoolResponseDTO(UUID.randomUUID(), "School", "Addr", "111", true);
            SchoolResponseDTO dto2 = new SchoolResponseDTO(UUID.randomUUID(), "School", "Addr", "111", true);

            assertThat(dto1).isNotEqualTo(dto2);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            SchoolResponseDTO dto = new SchoolResponseDTO(UUID.randomUUID(), "School", "Addr", "111", true);
            assertThat(dto).isNotEqualTo(null);
        }

        @Test
        @DisplayName("Should not be equal to object of different class")
        void shouldNotBeEqualToDifferentClass() {
            SchoolResponseDTO dto = new SchoolResponseDTO(UUID.randomUUID(), "School", "Addr", "111", true);
            Object other = new Object();
            assertThat(dto).isNotEqualTo(other);
        }
    }

    @Nested
    @DisplayName("ToString")
    class ToStringTest {
        @Test
        @DisplayName("Should contain all field values in string representation")
        void shouldContainAllFieldValuesInToString() {
            UUID id = UUID.randomUUID();
            String name = "Test School";
            String address = "123 Main St";
            String phoneNumber = "555-1234";
            boolean active = true;

            SchoolResponseDTO dto = new SchoolResponseDTO(id, name, address, phoneNumber, active);
            String result = dto.toString();

            assertThat(result).contains(id.toString());
            assertThat(result).contains(name);
            assertThat(result).contains(address);
            assertThat(result).contains(phoneNumber);
            assertThat(result).contains(String.valueOf(active));
        }
    }

    @Nested
    @DisplayName("Builder")
    class BuilderTest {
        @Test
        @DisplayName("Should build SchoolResponseDTO using builder pattern")
        void shouldBuildUsingBuilder() {
            UUID id = UUID.randomUUID();
            String name = "Builder School";
            String address = "456 Builder Ave";
            String phoneNumber = "555-9999";
            boolean active = false;

            SchoolResponseDTO dto = SchoolResponseDTO.builder()
                    .id(id)
                    .name(name)
                    .address(address)
                    .phoneNumber(phoneNumber)
                    .active(active)
                    .build();

            assertThat(dto.getId()).isEqualTo(id);
            assertThat(dto.getName()).isEqualTo(name);
            assertThat(dto.getAddress()).isEqualTo(address);
            assertThat(dto.getPhoneNumber()).isEqualTo(phoneNumber);
            assertThat(dto.isActive()).isEqualTo(active);
        }

        @Test
        @DisplayName("Should create builder via toBuilder method")
        void shouldCreateBuilderViaToBuilder() {
            UUID originalId = UUID.randomUUID();
            SchoolResponseDTO original = new SchoolResponseDTO(originalId, "Original", "Addr", "111", true);

            UUID newId = UUID.randomUUID();
            SchoolResponseDTO modified = original.toBuilder()
                    .id(newId)
                    .name("Modified")
                    .active(false)
                    .build();

            assertThat(modified.getId()).isEqualTo(newId);
            assertThat(modified.getName()).isEqualTo("Modified");
            assertThat(modified.getAddress()).isEqualTo("Addr");
            assertThat(modified.getPhoneNumber()).isEqualTo("111");
            assertThat(modified.isActive()).isFalse();
        }
    }
}