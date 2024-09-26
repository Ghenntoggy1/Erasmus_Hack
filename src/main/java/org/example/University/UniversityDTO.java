package org.example.University;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UniversityDTO {

    private Integer university_id;

    @NotNull(message = "University name cannot be null")
    @Size(min = 2, max = 100, message = "University name must be between 2 and 100 characters")
    private String university_name;
}
