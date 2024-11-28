package org.example.University;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.City.City;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "University")
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "university_id")
    private Integer university_id;

    @NotNull(message = "University name cannot be null")
    @Size(min = 2, max = 100, message = "University name must be between 2 and 100 characters")
    @Column(name = "university_name")
    private String university_name;

    @NotNull(message = "City cannot be null")
    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "city_id")
    private City city_id;
}
