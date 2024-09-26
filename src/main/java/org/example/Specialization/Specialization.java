package org.example.Specialization;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Category.Category;
import org.example.Course.Course;
import org.example.Offer.Offer;
import org.example.University.University;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "specialization_id")
@Table(name = "Specialization")
public class Specialization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "specialization_id")
    private Integer specialization_id;

    @NotNull(message = "Specialization name cannot be null")
    @Size(min = 2, max = 100, message = "Specialization name must be between 2 and 100 characters")
    @Column(name = "specialization_name")
    private String specialization_name;

    @NotNull(message = "University cannot be null")
    @ManyToOne
    @JoinColumn(name = "university_id", referencedColumnName = "university_id")
    private University universityId;

    @NotNull(message = "Category cannot be null")
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category_id;

    @ManyToMany(mappedBy = "specializations")
    private List<Offer> offers;

    @ManyToMany
    @JoinTable(
            name = "Specialization_Course", // Name of the join table
            joinColumns = @JoinColumn(name = "specialization_id"), // Foreign key for Specialization in join table
            inverseJoinColumns = @JoinColumn(name = "course_id") // Foreign key for Course in join table
    )
    private List<Course> courses;
}
