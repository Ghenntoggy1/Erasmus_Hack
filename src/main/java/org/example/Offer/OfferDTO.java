package org.example.Offer;

import jakarta.validation.constraints.*;
import lombok.*;
import org.example.Specialization.SpecializationDTO;

import java.util.List;
import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OfferDTO {

    @NotNull(message = "Sender ID cannot be null")
    private Integer sender_id;

    @NotNull(message = "Receiver ID cannot be null")
    private Integer receiver_id;

    @NotNull(message = "Offer name cannot be null")
    @Size(min = 5, max = 100, message = "Offer name must be between 5 and 100 characters")
    private String offer_name;

    @NotNull(message = "Description cannot be null")
    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    private String description;

    @NotNull(message = "Offer start date cannot be null")
    private Date offer_start_date;

    @NotNull(message = "Offer end date cannot be null")
    private Date offer_end_date;

    @NotNull(message = "Program start date cannot be null")
    private Date program_start;

    @NotNull(message = "Program end date cannot be null")
    private Date program_end;

    @Min(value = 0, message = "Scholarship must be 0 or more")
    private Integer scholarship;

    @NotBlank(message = "Language cannot be blank")
    @Size(max = 50, message = "Language must be less than 50 characters")
    private String language;

    @NotEmpty(message = "Specializations list cannot be empty")
    private List<SpecializationDTO> specializations;

}
