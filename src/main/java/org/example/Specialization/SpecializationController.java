package org.example.Specialization;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/specializations")
public class SpecializationController {
    private final SpecializationService specializationService;

    @Autowired
    public SpecializationController(SpecializationService specializationService) {
        this.specializationService = specializationService;
    }

    @GetMapping("/univ_id:{university_id}")
    public ResponseEntity<List<SpecializationDTO>> getSpecializationsByUniversityId(@Valid @PathVariable("university_id") Integer universityId) {
        List<SpecializationDTO> specializationDTOs = specializationService.getSpecializationsByUniversityId(universityId);
        return ResponseEntity.ok(specializationDTOs);
    }
    @GetMapping("/{id}")
    public SpecializationResponse getSpecializationById(@Valid @PathVariable("id") Integer offerSpecializationId,
                                                        @Valid @RequestParam(value = "specialization_id", required = false) Integer mineSpecializationId,
                                                        @Valid @RequestParam(value = "sem", required = false) Integer semester) {
        return specializationService.getSpecializationDetails(mineSpecializationId, offerSpecializationId, semester);
    }
}
