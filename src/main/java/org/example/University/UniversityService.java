package org.example.University;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UniversityService {
    private final UniversityRepository universityRepository;

    @Autowired
    public UniversityService(UniversityRepository universityRepository) {
        this.universityRepository = universityRepository;
    }

    public List<UniversityDTO> getUniversities() {
        return universityRepository.findAll().stream()
                .map(this::mapUniversityToDTO)
                .collect(Collectors.toList());
    }

    public UniversityDTO getUniversityById(Integer id) {
        return universityRepository.findById(id)
                .map(this::mapUniversityToDTO)
                .orElse(null);
    }

    private UniversityDTO mapUniversityToDTO(University university) {
        return UniversityDTO.builder()
                .university_id(university.getUniversity_id())
                .university_name(university.getUniversity_name())
                .build();
    }
}
