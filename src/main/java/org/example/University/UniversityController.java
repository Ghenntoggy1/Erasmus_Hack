package org.example.University;



import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/universities")
public class UniversityController {
    private final UniversityService universityService;

    public UniversityController(UniversityService universityService) {
        this.universityService = universityService;
    }

    @GetMapping()
    public List<UniversityDTO> getUniversities() {
        return universityService.getUniversities();
    }

    @GetMapping("/univ_id:{id}")
    public UniversityDTO getUniversityById(@PathVariable("id") Integer id) {
        return universityService.getUniversityById(id);
    }
}
