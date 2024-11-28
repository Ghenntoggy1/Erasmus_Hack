package org.example.Offer;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.University.University;
import org.example.Specialization.Specialization;

import java.sql.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Offer")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "offer_id")
    private Integer offer_id;

    @NotNull(message = "Offer name cannot be null")
    @Size(min = 5, max = 100, message = "Offer name must be between 5 and 100 characters")
    @Column(name = "offer_name")
    private String offerName;

    @NotNull(message = "Description cannot be null")
    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    @Column(name = "description")
    private String description;

    @NotNull(message = "Start date cannot be null")
    @Column(name = "start_date")
    private Date offer_start_date;

    @NotNull(message = "End date cannot be null")
    @Column(name = "end_date")
    private Date offer_end_date;

    @NotNull(message = "Program start date cannot be null")
    @Column(name = "program_start")
    private Date program_start;

    @NotNull(message = "Program end date cannot be null")
    @Column(name = "program_end")
    private Date program_end;

    @NotBlank(message = "Language cannot be blank")
    @Size(max = 50, message = "Language must be less than 50 characters")
    @Column(name = "language")
    private String language;

    @Min(value = 0, message = "Scholarship must be 0 or more")
    @Column(name = "scholarship")
    private Integer scholarship;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "receiver_id", referencedColumnName = "university_id")
    private University receiverId;

    @OneToOne
    @JsonBackReference
    @JoinColumn(name = "sender_id", referencedColumnName = "university_id")
    private University senderId;

    @ManyToMany
    @JoinTable(
            name = "Offer_Specialization",
            joinColumns = @JoinColumn(name = "offer_id"),
            inverseJoinColumns = @JoinColumn(name = "specialization_id")
    )
    @JsonManagedReference
    private List<Specialization> specializations;
}
