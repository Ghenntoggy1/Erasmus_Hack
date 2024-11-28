package org.example.Specialization;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecializationDTO {

    private Integer specialization_id;

    @NotNull(message = "Specialization name cannot be null")
    @Size(min = 2, max = 100, message = "Specialization name must be between 2 and 100 characters")
    private String specialization_name;
}
